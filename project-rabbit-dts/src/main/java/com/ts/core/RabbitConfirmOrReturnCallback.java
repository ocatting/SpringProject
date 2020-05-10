package com.ts.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Description: 确认Exchange是否接收
 * @Author Yan XinYu
 **/
public abstract class RabbitConfirmOrReturnCallback implements RabbitTemplate.ConfirmCallback
        ,RabbitTemplate.ReturnCallback ,RabbitRejectStrategy{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(logger.isDebugEnabled()){
            logger.debug(correlationData.toString());
        }
        // ack false;
        if(!ack){
            logger.info("confirmCallback,ack:{},cause:{}",ack,cause);
            reject(correlationData.getReturnedMessage());
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.error("Send message failed:",message.toString());
    }

}
