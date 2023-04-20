package com.freeing.unqid.leafcore.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.freeing.unqid.leafcore.IdManager;
import com.freeing.unqid.leafcore.config.DataSourceProperties;
import com.freeing.unqid.leafcore.dao.LeafAllocDao;
import com.freeing.unqid.leafcore.dao.impl.LeafAllocDaoImpl;
import com.freeing.unqid.leafcore.segment.enumnew.Status;
import com.freeing.unqid.leafcore.segment.model.ID;
import com.freeing.unqid.leafcore.service.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Properties;

/**
 * segment id 服务
 *
 * @author yanggy
 */
public class SegmentIdGenerator implements IdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(IdManager.class);
    public static final String LEAF_SEGMENT_ENABLE = "leaf.segment.enable";
    public static final String LEAF_JDBC_URL = "leaf.jdbc.url";
    public static final String LEAF_JDBC_USERNAME = "leaf.jdbc.username";
    public static final String LEAF_JDBC_PASSWORD = "leaf.jdbc.password";

    private final IdManager idManager;

    private SegmentIdGenerator() {
        Properties properties = DataSourceProperties.getProperties();
        boolean flag = Boolean.parseBoolean(properties.getProperty(LEAF_SEGMENT_ENABLE, "true"));
        DruidDataSource dataSource = null;
        if (flag) {
            dataSource = new DruidDataSource();
            dataSource.setUrl(properties.getProperty(LEAF_JDBC_URL));
            dataSource.setUsername(properties.getProperty(LEAF_JDBC_USERNAME));
            dataSource.setPassword(properties.getProperty(LEAF_JDBC_PASSWORD));
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setValidationQuery("select 1");
            try {
                dataSource.init();
            } catch (SQLException e) {
                logger.error("Mysql init error -> { }", e);
            }
        }
        LeafAllocDao dao = new LeafAllocDaoImpl(dataSource);
        idManager = IdManager.getInstance(dao);
        idManager.init();
    }

    public static SegmentIdGenerator getInstance() {
        return new SegmentIdGenerator();
    }

    @Override
    public ID get(String key) {
        try {
            long id = idManager.get(key);
            return new ID(id, Status.SUCCESS);
        } catch (Exception e) {
            return new ID(-1, Status.EXCEPTION);
        }
    }

    @Override
    public IdManager getIdManager() {
        return idManager;
    }
}
