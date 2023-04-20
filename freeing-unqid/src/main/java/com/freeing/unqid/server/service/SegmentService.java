package com.freeing.unqid.server.service;

import com.freeing.unqid.leafcore.segment.enumnew.Status;
import com.freeing.unqid.leafcore.segment.model.ID;
import com.freeing.unqid.leafcore.service.IdGenerator;
import com.freeing.unqid.leafcore.service.impl.SegmentIdGenerator;
import com.freeing.unqid.server.exception.LeafServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanggy
 */
@Service
public class SegmentService {
    private final Logger logger = LoggerFactory.getLogger(SegmentService.class);

    private final IdGenerator idGenerator;

    public SegmentService() {
        idGenerator = SegmentIdGenerator.getInstance();
    }

    /**
     * 获取一个 ID
     *
     * @param key 业务 key
     * @return ID
     */
    public String get(String key) {
        ID id = idGenerator.get(key);
        if (id.getStatus() == Status.EXCEPTION) {
            throw new LeafServerException("Get id failure.");
        }
        return String.valueOf(id.getId());
    }

    /**
     * 批量申请ID
     *
     * @param key 业务区分
     * @param keyNumber ID 数量
     * @return ID 集合
     */
    public List<String> getIds(String key, int keyNumber) {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i <= keyNumber; i++){
            ID id = idGenerator.get(key);
            if (id.getStatus() == Status.SUCCESS) {
                ids.add(String.valueOf(id.getId()));
            }
        }
        return ids;
    }

    public SegmentIdGenerator getIdGenerator() {
        if (idGenerator instanceof SegmentIdGenerator) {
            return (SegmentIdGenerator) idGenerator;
        }
        return null;
    }
}
