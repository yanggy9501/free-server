package com.freeing.unqid.leafcore.segment.model;

import com.freeing.unqid.leafcore.segment.enumnew.Status;

/**
 * ID 统一返回结果
 *
 * @author yanggy
 */
public class ID {
    private long id;

    /**
     * 响应状态
     */
    private Status status;

    public ID() {
    }

    public ID(long id, Status status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
