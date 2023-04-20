package com.freeing.unqid.leafcore.segment.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ID 段
 *
 * @author yanggy
 */
public class Segment {
    /**
     * 该段已经使用到最大 ID(未被使用)
     */
    private final AtomicLong value = new AtomicLong(0);

    /**
     * 当前段的可分配到的最大 ID，不包含 maxId
     */
    private volatile long maxId;

    /**
     * 每段 segment 的长度
     */
    private volatile int step;

    /**
     * segment 是否处于可用状态
     */
    private volatile boolean isReady;

    /**
     * 闲置的 ID 数量
     *
     * @return 未使用ID数量
     */
    public long getIdle() {
        // value 是下一个将要被使用的ID，maxId 是可以使用的
        return this.getMaxId() - getValue().get() ;
    }

    public AtomicLong getValue() {
        return value;
    }


    public long nextId() {
        return value.getAndIncrement();
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
