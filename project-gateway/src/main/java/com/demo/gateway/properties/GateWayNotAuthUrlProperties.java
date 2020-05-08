package com.demo.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashSet;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
@ConfigurationProperties("gateway")
public class GateWayNotAuthUrlProperties {

    private LinkedHashSet<String> shouldSkipUrls;
}
