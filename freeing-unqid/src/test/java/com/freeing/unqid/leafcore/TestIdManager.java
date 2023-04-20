package com.freeing.unqid.leafcore;

import com.alibaba.druid.pool.DruidDataSource;
import com.freeing.unqid.leafcore.config.DataSourceProperties;
import com.freeing.unqid.leafcore.dao.LeafAllocDao;
import com.freeing.unqid.leafcore.dao.impl.LeafAllocDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author yanggy
 */
public class TestIdManager {
    public static final String LEAF_SEGMENT_ENABLE = "leaf.segment.enable";
    public static final String LEAF_JDBC_URL = "leaf.jdbc.url";
    public static final String LEAF_JDBC_USERNAME = "leaf.jdbc.username";
    public static final String LEAF_JDBC_PASSWORD = "leaf.jdbc.password";

    public static void main(String[] args) throws SQLException, IOException {

        Properties properties = DataSourceProperties.getProperties();
        boolean flag = Boolean.parseBoolean(properties.getProperty(LEAF_SEGMENT_ENABLE, "true"));
        DruidDataSource dataSource = null;
        if (flag) {
            // Config dataSource
            dataSource = new DruidDataSource();
            dataSource.setUrl(properties.getProperty(LEAF_JDBC_URL));
            dataSource.setUsername(properties.getProperty(LEAF_JDBC_USERNAME));
            dataSource.setPassword(properties.getProperty(LEAF_JDBC_PASSWORD));
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setValidationQuery("select 1");
            dataSource.init();
        }
        LeafAllocDao dao = new LeafAllocDaoImpl(dataSource);
        IdManager instance = IdManager.getInstance(dao);
        instance.init();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                new Thread(() -> {
                    System.out.println("tag1：" + instance.get("tag1"));
                }).start();
            }
        }
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                new Thread(() -> {
//                    System.out.println("tag2：" +  instance.get("tag2"));
//                }).start();
//            }
//        }
        System.out.println("-------------");
    }

}
