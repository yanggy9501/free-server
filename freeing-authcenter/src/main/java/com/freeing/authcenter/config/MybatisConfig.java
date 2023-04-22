package com.freeing.authcenter.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanggy
 */
@Configuration
@MapperScan(basePackages = {"com.freeing.authcenter.mapper"})
public class MybatisConfig {

}
