package com.freeing.unqid.leafcore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Leaf-Segment 数据库属性工厂
 * 初始化时读取 properties 文件获取数据库属性
 * 配置：
 * leaf.segment.enable=true
 * leaf.jdbc.url=jdbc:mysql://127.0.0.1:3306/leaf?serverTimezone=GMT%2b8&autoReconnect=true&useUnicode=true&characterEncoding=utf-8&autocommit=false&useSSL=false
 * leaf.jdbc.username=username
 * leaf.jdbc.password=password
 */
public class DataSourceProperties {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceProperties.class);

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(DataSourceProperties.class.getClassLoader().getResourceAsStream("leaf.properties"));
        } catch (IOException e) {
            logger.warn("Load Properties Ex", e);
        }
    }
    public static Properties getProperties() {
        return PROPERTIES;
    }
}
