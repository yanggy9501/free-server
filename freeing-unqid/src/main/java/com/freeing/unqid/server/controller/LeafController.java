package com.freeing.unqid.server.controller;

import com.freeing.unqid.server.service.SegmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yanggy
 */
@Api(tags = "LeafController")
@RestController
public class LeafController {
    private final Logger logger = LoggerFactory.getLogger(LeafController.class);

    @Autowired
    private SegmentService segmentService;

    /**
     * 申请一个 ID
     *
     * @param key 业务 key
     * @return ID
     */
    @ApiOperation(value = "申请全局唯一性 ID")
    @RequestMapping(value = "/segment/id/{key}")
    public String getSegmentId(@PathVariable("key") String key) {
        return segmentService.get(key);
    }

    /**
     * 申请一个 ID
     *
     * @param key 业务 key
     * @return ID
     */
    @ApiOperation(value = "批量申请全局唯一性 ID")
    @RequestMapping(value = "/segment/ids/{key}")
    public List<String> getSegmentIds(@PathVariable("key") String key, @RequestParam int keyNumber) {
        return segmentService.getIds(key, keyNumber);
    }
}
