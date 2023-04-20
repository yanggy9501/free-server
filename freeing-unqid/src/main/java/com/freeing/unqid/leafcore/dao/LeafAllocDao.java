package com.freeing.unqid.leafcore.dao;

import com.freeing.unqid.leafcore.segment.model.entity.LeafAlloc;

import java.util.List;

public interface LeafAllocDao {
    /**
     * 获取所有 ID 分配情况
     *
     * @return List<LeafAlloc>
     */
    List<LeafAlloc> getAllLeafAllocs();

    /**
     * 更新指定标识的最大 MaxId 并且获取其 ID 分配情况
     *
     * @param tag 区分的标识
     * @return tag 的 ID 分配情况
     */
    LeafAlloc updateMaxIdAndGetLeafAlloc(String tag);

    /**
     * 更新并获取 ID 分配情况
     *
     * @param leafAlloc ID 分配详情实体
     * @return tag 的 ID 分配情况
     */
    LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc);

    /**
     * 获取所有 Tag
     *
     * @return ID 区分的标识集合
     */
    List<String> getAllTags();
}
