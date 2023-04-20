package com.freeing.unqid.leafcore.segment.model.entity;

/**
 * Leaf-Segment ID 分配详情实体
 *
 * @author yanggy
 */
public class LeafAlloc {
    /**
     * 业务表示
     */
    private String key;

    /**
     * 当前 key 中已经分配的 ID 最大值
     */
    private long maxId;

    /**
     * 步长（ID 申请长度）
     */
    private int step;

    /**
     * 版本号
     */
    private int version;

    private String description;

    private String updateTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
