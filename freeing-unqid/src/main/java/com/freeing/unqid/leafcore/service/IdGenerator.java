package com.freeing.unqid.leafcore.service;

import com.freeing.unqid.leafcore.IdManager;
import com.freeing.unqid.leafcore.segment.model.ID;

/**
 * ID 生成接口
 */
public interface IdGenerator {
    /**
     * 获取 ID
     *
     * @param key 业务区分标识
     * @return Result
     */
    ID get(String key);

    IdManager getIdManager();
}
