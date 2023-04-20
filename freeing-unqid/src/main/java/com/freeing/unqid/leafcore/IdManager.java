package com.freeing.unqid.leafcore;

import com.freeing.unqid.leafcore.dao.LeafAllocDao;
import com.freeing.unqid.leafcore.segment.enumnew.Event;
import com.freeing.unqid.leafcore.segment.model.Segment;
import com.freeing.unqid.leafcore.segment.model.SegmentBuffer;
import com.freeing.unqid.leafcore.segment.model.entity.LeafAlloc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * ID 管理器
 *
 * @author yanggy
 */
public class IdManager {
    private static final Logger logger = LoggerFactory.getLogger(IdManager.class);

    private static volatile IdManager idManager;

    /**
     * 缓存
     */
    private final Map<String, SegmentBuffer> segmentContextMap = new ConcurrentHashMap<>();

    /**
     * 刷新 tags 的线程池
     */
    private final ScheduledExecutorService refreshTagsService = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("refresh-segment-context-tags-thread");
        t.setDaemon(true);
        return t;
    });

    /**
     * 获取 ID 的线程池
     */
    private final ExecutorService service = new ThreadPoolExecutor(
        5,
        Integer.MAX_VALUE,
        60L,
        TimeUnit.SECONDS,
        // 同步队列
        new SynchronousQueue<>(),
        new UpdateThreadFactory());

    private static class UpdateThreadFactory implements ThreadFactory {

        private static int threadInitNumber = 0;

        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-Segment-Update-" + nextThreadNum());
        }
    }

    /**
     * ID 管理器初始化状态
     * 一写多读，使用 volatile 即可
     */
    private volatile boolean initOK = false;

    private final Object initLock = new Object();

    /**
     * leaf segment dao
     */
    private final LeafAllocDao allocDao;

    public static IdManager getInstance(LeafAllocDao allocDao) {
        if (idManager == null) {
            synchronized (IdManager.class) {
                if (idManager == null) {
                    idManager = new IdManager(allocDao);
                }
            }
        }
        return idManager;
    }

    public static IdManager getIdManager() {
        if (idManager == null) {
            logger.warn("IdManager must be instanced.");
            throw new NullPointerException("Please instance IdManager firstly.");
        }
        return idManager;
    }

    private IdManager(LeafAllocDao allocDao) {
        this.allocDao = allocDao;
    }

    public Map<String, SegmentBuffer> getSegmentContextMap() {
        return segmentContextMap;
    }

    public List<LeafAlloc> getAllLeafAllocs() {
        return allocDao.getAllLeafAllocs();
    }

    /**
     * ID 管理器初始化，经过初始化后 Id 管理器才能对外提供服务
     * 1. segment context 的 key 初始化（IdManager 必须知道所有的 tag）
     * 2. value 的初始化，创建 SegmentBuffer
     * PS: ID 管理器的初始化是完成 tags 的拉取，tag 对应的 segment 的初始化在使用时进行
     */
    public void init() {
        if (!initOK) {
            synchronized (initLock) {
                if (!initOK) {
                    refreshSegmentContextFromDb();
                    finishInit();
                    refreshTagsEveryMinute();
                }
            }
        }
    }

    private void finishInit() {
        initOK = true;
    }

    /**
     * 定时同步数据库中的 tags
     */
    private void refreshTagsEveryMinute() {
        logger.info("Start task to refresh tags from db every minute.");
        refreshTagsService
            .scheduleWithFixedDelay(this::refreshSegmentContextFromDb, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 更新 Segment Context
     */
    private void refreshSegmentContextFromDb() {
        List<String> dbTags = allocDao.getAllTags();
        if (dbTags.isEmpty()) {
            return;
        }
        // 缓存中的 tag
        List<String> segmentContextTags = new ArrayList<>(segmentContextMap.keySet());
        // db 中可能有增删，可能与缓存中的不一致，需要调整
        // 新增的 tag (dbTags - segmentContextTags)
        Set<String> newTagsSet = new HashSet<>(dbTags);
        // 需要删除的（segmentContextTags - dbTags）
        Set<String> removeTagsSet = new HashSet<>(segmentContextTags);

        // db中新加的tags灌进cache(以数据库中的为准)
        for (String tag : segmentContextTags) {
            newTagsSet.remove(tag);
        }

        for (String tag : newTagsSet) {
            SegmentBuffer buffer = new SegmentBuffer();
            buffer.setKey(tag);
            // 增加到缓存中，但是 buffer 中的 segment 还未初始化，在使用时初始
            segmentContextMap.put(tag, buffer);
            logger.info("Add tag {} from db to ache, SegmentBuffer {}", tag, buffer);
        }

        // cache中已失效的tags从cache删除
        for (String tag : dbTags) {
            removeTagsSet.remove(tag);
        }
        for (String tag : removeTagsSet) {
            segmentContextMap.remove(tag);
            logger.info("Remove tag {} from Cache", tag);
        }
    }

    public long get(String key) {
        if (!initOK) {
            logger.warn("Please init id manager when get ids.");
            throw new IllegalStateException("Please init id manager when get ids.");
        }
        SegmentBuffer buffer = segmentContextMap.get(key);
        // 查询和判断不是原子性的，所以先get，根据 get 结果进行判断，并且后续操作都是传递 buffer
        if (buffer == null) {
            logger.warn("key: {} is not exist", key);
            throw new IllegalArgumentException("key " + key + " is not exist.");
        }
        // 当前 SegmentBuffer 没有初始化，即 segment 没有更新数据.
        if (!buffer.isInitOk()) {
            // 初始化、更新 segment
            synchronized (buffer) {
                if (!buffer.isInitOk()) {
                    // 传递 buffer，因为在此过程中，数据库删除操作， buffer 对应的 tag 可能会被定时任务删除掉，避免空指针异常
                    updateSegmentFromDb(Event.INIT_ID_MANAGER, buffer, buffer.getCurrent());
                    // 初始化完成
                    buffer.setInitOk(true);
                }
            }
        }
        return getIdFromSegmentBuffer(buffer);
    }

    /**
     * 更新 buffer 中一个 segment.
     * 保证 updateSegmentFromDb 方法线程安全
     * 1.保证allocDao.updateMaxIdAndGetLeafAlloc 包含了更新和查询操作的一致性
     * 2. 真正操作的 segment 没有被其他线程操作
     *
     * 当 buffer 还未初始化时，一定只有一个线程进入该方法，在 buffer 过程中一定线安全
     *
     * @param event 更新事件
     * @param buffer 缓存
     * @param segment 缓存中一个 segment id 段
     */
    private void updateSegmentFromDb(Event event, SegmentBuffer buffer, Segment segment) {
        LeafAlloc leafAlloc;
        if (event == Event.INIT_ID_MANAGER) {
            // 保证 allocDao#updateMaxIdAndGetLeafAlloc 不会多线程多次执行 ==》 外面调用锁定 buffer
            leafAlloc = allocDao.updateMaxIdAndGetLeafAlloc(buffer.getKey());
        } else if (event == Event.UPDATE_SEGMENT) {
            leafAlloc = allocDao.updateMaxIdAndGetLeafAlloc(buffer.getKey());
        } else {
            leafAlloc = new LeafAlloc();
        }
        long value = leafAlloc.getMaxId() - leafAlloc.getStep();
        if (value < 1) {
            throw new IllegalStateException("Illegal data: " + leafAlloc);
        }
        segment.setMaxId(leafAlloc.getMaxId());
        segment.setStep(leafAlloc.getStep());
        segment.getValue().set(value);
        segment.setReady(true);
    }

    private long getIdFromSegmentBuffer(SegmentBuffer buffer) {
       for (;;) {
           Segment segment = buffer.getCurrent();
           // 当前号段已下发50%时，如果下一个号段未更新，则另启一个更新线程去更新下一个号段
           // PS: 假设当前使用 1 号Segment并且使用到 50%，如果接下来每次都使用一个 ID，那岂不是一直在更新另外一个缓存（浪费），所以buffer.isNextReady来做这个控制
           // buffer.getThreadRunning().compareAndSet(false, true)：能够保证当前buffer只有一个线程取修改下一个的segment
           if ( !buffer.getSegments()[buffer.nextPosition()].isReady()
               && segment.getIdle() < (segment.getStep() >> 1)
               && buffer.getThreadRunning().compareAndSet(false, true)) {
                   service.execute(() -> {
                       // segment 被切换必须是 另外一个segment 的 ready 状态为true
                       Segment next = buffer.getSegments()[buffer.nextPosition()];
                       boolean updateOk = false;
                       try {
                           updateSegmentFromDb(Event.UPDATE_SEGMENT, buffer, next);
                           updateOk = true;
                           logger.info("update segment {} from db {}", buffer.getKey(), next);
                       } catch (Exception e) {
                           logger.warn(buffer.getKey() + " updateSegmentFromDb exception", e);
                       } finally {
                           // 设置状态
                           next.setReady(updateOk);
                           buffer.getThreadRunning().set(false);
                       }
                   });
           }
           // 如果有更新线程在运行，等待
           waitAndSleep(buffer);
           // 获取 id
           long id = segment.getValue().getAndIncrement();
           if (id < segment.getMaxId()) {
               return id;
           }
           // 该 segment 不可用了
           segment.setReady(false);
           logger.info(id + " value is more than max id {}", segment.getMaxId());
           // 切换 segment
           // ps：下个 segment 为完成更新时是无法切换的
           if (buffer.getSegments()[buffer.nextPosition()].isReady()) {
               buffer.switchSegment();
           } else {
               logger.warn("Next segment is not ready when switch segment.");
               throw new IllegalStateException("Next segment is not ready when switch segment.");
           }
           // 切换后检查一下 buffer 对应的 tag 是否还存在缓存中，避免数据库删除操作导致本地也删除而又申请不到id，导致死循环
           if (!segmentContextMap.containsKey(buffer.getKey())) {
               logger.warn("{} was removed from segment context.", buffer.getKey());
               throw new IllegalStateException(buffer + " was removed from segment context.");
           }
       }
    }

    private void waitAndSleep(SegmentBuffer buffer) {
        int roll = 0;
        while (buffer.getThreadRunning().get()) {
            roll += 1;
            if(roll > 10000) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    break;
                } catch (InterruptedException e) {
                    logger.warn("Thread {} Interrupted", Thread.currentThread().getName());
                    break;
                }
            }
        }
    }
}
