package com.demo.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
@ConfigurationProperties("gateway.limiter")
public class LimiterProperties {

    // 请求超时检查 100ms
    private int checkActionTimeout = 100;

    //前缀
    private String redisKeyPrefix = "#Gateway_RL";

    //配置每一个路径下的数据
    private Map<String,LimitingPolicy> policyMap = Collections.emptyMap();
}
