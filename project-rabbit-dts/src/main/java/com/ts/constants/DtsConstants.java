package com.ts.constants;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface DtsConstants {

    /**
     * 队列配置
     */
    public String RABBIT_EXCHANGE = "dts-rmt-exchange";
    public String RABBIT_QUEUE = "dts-rmt-queue";
    public String RABBIT_ROUTINGKEY = "dts-rmt-routingkey";

    /**
     * 死信队列配置
     */
    public String RABBIT_DEADLETTER_EXCHANGE = "dts-rmt-deadletter-exchange";
    public String RABBIT_DEADLETTER_QUEUE = "dts-rmt-deadletter-queue";
    public String RABBIT_DEADLETTER_ROUTINGKEY = "dts-rmt-deadletter-routingkey";
}
