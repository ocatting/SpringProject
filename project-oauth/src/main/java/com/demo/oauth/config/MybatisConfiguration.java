package com.demo.oauth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
@EnableTransactionManagement
@MapperScan("com.demo.oauth.mapper")
public class MybatisConfiguration {
}
