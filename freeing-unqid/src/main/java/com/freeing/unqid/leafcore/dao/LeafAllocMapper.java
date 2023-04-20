package com.freeing.unqid.leafcore.dao;

import com.freeing.unqid.leafcore.segment.model.entity.LeafAlloc;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface LeafAllocMapper {

    @Select("SELECT biz_tag, max_id, step, version, description, update_time FROM leaf_alloc")
    @Results(value = {
        @Result(column = "biz_tag", property = "key"),
        @Result(column = "max_id", property = "maxId"),
        @Result(column = "step", property = "step"),
        @Result(column = "version", property = "version"),
        @Result(column = "description", property = "description"),
        @Result(column = "update_time", property = "updateTime")
    })
    List<LeafAlloc> getAllLeafAllocs();

    @Select("SELECT biz_tag, max_id, step, version, description, update_time FROM leaf_alloc WHERE biz_tag = #{tag}")
    @Results(value = {
        @Result(column = "biz_tag", property = "key"),
        @Result(column = "max_id", property = "maxId"),
        @Result(column = "step", property = "step"),
        @Result(column = "version", property = "version"),
        @Result(column = "description", property = "description"),
        @Result(column = "update_time", property = "updateTime")
    })
    LeafAlloc getLeafAlloc(@Param("tag") String tag);

    @Update("UPDATE leaf_alloc SET max_id = max_id + step, version = version + 1 WHERE biz_tag = #{tag}")
    void updateMaxId(@Param("tag") String tag);

    @Update("UPDATE leaf_alloc SET max_id = max_id + #{step}, version = version + 1 WHERE biz_tag = #{key} and version = #{version}")
    void updateMaxIdByCustomStep(@Param("leafAlloc") LeafAlloc leafAlloc);

    @Select("SELECT biz_tag FROM leaf_alloc")
    List<String> getAllTags();
}
