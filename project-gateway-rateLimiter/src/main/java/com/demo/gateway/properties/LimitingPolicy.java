package com.demo.gateway.properties;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class LimitingPolicy {

    private String matchUrl;
    private String timeUnit = TimeUnit.SECONDS.name();
    private int permits = 1000;


}
