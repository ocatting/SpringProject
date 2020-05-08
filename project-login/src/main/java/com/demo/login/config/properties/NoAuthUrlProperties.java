package com.demo.login.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
@ConfigurationProperties(prefix="member.auth")
public class NoAuthUrlProperties {

    private HashSet<String> shouldSkipUrls;
}
