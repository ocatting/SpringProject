package com.demo.login.config;

import com.demo.login.util.JwtKit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Description: 开启分布式Session
 * @Author Yan XinYu
 **/
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds=3600)
@Configuration
public class RedisHttpSessionConfiguration {

    @Bean
    public JwtKit jwtKit(){
        return new JwtKit();
    }
}
