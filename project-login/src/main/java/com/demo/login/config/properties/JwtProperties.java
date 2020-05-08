package com.demo.login.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtProperties {

    private String tokenHeader;

    private String secret;

    private Long expiration;

    private String tokenHead;
}
