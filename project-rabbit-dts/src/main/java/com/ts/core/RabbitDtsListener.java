package com.ts.core;

import com.rabbitmq.client.Channel;
import com.ts.constants.DtsConstants;
import com.ts.exception.RabbitRepeatDataException;
import com.ts.listener.IRabbitDtsListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 接收Rabbit消息
 * @Author Yan XinYu
 **/
@Component
public class RabbitDtsListener {

    @Autowired
    private IRabbitDtsListener dtsListenerList;

    @RabbitListener(queues = DtsConstants.RABBIT_QUEUE)
    public void receive(Message message, Channel channel ,Object obj) throws IOException {
        try {
            //幂等处理 ack
            verification(message);

            //结果处理 签收ack
            String str = new String(message.getBody(), StandardCharsets.UTF_8);

            dtsListenerList.process(str);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (RabbitRepeatDataException e){
            //处理幂等数据
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }catch (Exception e){
            //业务异常
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    private boolean verification(Message message) throws Exception {
        //幂等处理 ,若出现幂等，抛出异常 通知rabbit不用上传了。
        boolean result = dtsListenerList.verification(message);
        if(!result){
            throw new RabbitRepeatDataException("repeat data exception");
        }
        return result;
    }
}
