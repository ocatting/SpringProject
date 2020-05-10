package com.ts.core;

import org.springframework.amqp.core.Message;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface RabbitRejectStrategy {

    public void reject(Message message);
}
