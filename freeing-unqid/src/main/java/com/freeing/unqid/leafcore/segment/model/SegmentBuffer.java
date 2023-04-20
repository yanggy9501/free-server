package com.freeing.unqid.leafcore.segment.model;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Id 缓存
 *
 * @author yanggy
 */
public class SegmentBuffer {
    /**
     * id 业务标识
     */
    private String key;

    /**
     * segment 双缓存
     */
    private final Segment[] segments;

    /**
     * 正在使用的 segment 下标
     */
    private volatile int currentPosition;

    /**
     * 当前 segment 是否处于可用状态
     */
    private volatile boolean initOk;

    /**
     * 更新 segment 的线程状态
     */
    private final AtomicBoolean threadRunning;

    /**
     * 锁: 读写 segment 的锁
     */
    private final ReadWriteLock lock;

    /**
     * 更新的时间戳
     */
    private volatile long updateTimestamp;

    public SegmentBuffer() {
        segments = new Segment[]{new Segment(), new Segment()};
        currentPosition = 0;
        initOk = false;
        threadRunning = new AtomicBoolean(false);
        lock = new ReentrantReadWriteLock();
    }

    /**
     * 获取下个 Segment 的下标， + 1 代表切换
     *
     * @return 切换后 Segment 的下标
     */
    public int nextPosition() {
        // 对 2 取模
        return (currentPosition + 1) & 1;
    }

    public void switchSegment() {
        currentPosition = nextPosition();
    }

    public Segment getCurrent() {
        return segments[getCurrentPosition()];
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    /**************************************** getter and setter ***********************************/

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Segment[] getSegments() {
        return segments;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean isInitOk() {
        return initOk;
    }

    public void setInitOk(boolean initOk) {
        this.initOk = initOk;
    }

    public AtomicBoolean getThreadRunning() {
        return threadRunning;
    }

    public ReadWriteLock getLock() {
        return lock;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String toString() {
        return "SegmentBuffer{" +
            "key='" + key + '\'' +
            ", segments=" + Arrays.toString(segments) +
            ", currentPosition=" + currentPosition +
            ", initOk=" + initOk +
            ", threadRunning=" + threadRunning +
            ", lock=" + lock +
            ", updateTimestamp=" + updateTimestamp +
            '}';
    }
}
