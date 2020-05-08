package com.demo.login.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
@ConfigurationProperties(prefix = "redis.key")
public class RedisKeyPrefixConfig {

    private RedisKeyPrefixConfig.Prefix prefix;

    private RedisKeyPrefixConfig.Expire expire;

    @Data
    public static class Prefix{
        private String otpCode;

    }

    @Data
    public static class Expire{

        private Long otpCode;

    }
}
