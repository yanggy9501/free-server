package com.freeing.unqid.server.module;

public class SegmentBufferView {
    /**
     * 业务标识
     */
    private String key;

    private int currentPosition;

    /**
     * buffer 初始化状态
     */
    private boolean initOk;

    /**
     * 是否正在更新 segment
     */
    private boolean threadRunning;

    private long value0;
    private int step0;
    private long max0;
    private boolean isReady0;

    private long value1;
    private int step1;
    private long max1;
    private boolean isReady1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public boolean isThreadRunning() {
        return threadRunning;
    }

    public void setThreadRunning(boolean threadRunning) {
        this.threadRunning = threadRunning;
    }

    public long getValue0() {
        return value0;
    }

    public void setValue0(long value0) {
        this.value0 = value0;
    }

    public int getStep0() {
        return step0;
    }

    public void setStep0(int step0) {
        this.step0 = step0;
    }

    public long getMax0() {
        return max0;
    }

    public void setMax0(long max0) {
        this.max0 = max0;
    }

    public boolean isReady0() {
        return isReady0;
    }

    public void setReady0(boolean ready0) {
        isReady0 = ready0;
    }

    public long getValue1() {
        return value1;
    }

    public void setValue1(long value1) {
        this.value1 = value1;
    }

    public int getStep1() {
        return step1;
    }

    public void setStep1(int step1) {
        this.step1 = step1;
    }

    public long getMax1() {
        return max1;
    }

    public void setMax1(long max1) {
        this.max1 = max1;
    }

    public boolean isReady1() {
        return isReady1;
    }

    public void setReady1(boolean ready1) {
        isReady1 = ready1;
    }
}
