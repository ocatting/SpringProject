package com.ts.core;

import com.ts.constants.DtsConstants;
import com.ts.exception.RabbitDtsException;
import com.ts.parser.IDtsParser;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Component
public class RabbitDtsSender implements IRabbitDtsSender{

    @Autowired
    private IDtsParser rmtParser;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(Object obj){
        this.send(obj,null);
    }

    @Override
    public void send(Object obj,String id) {
        if(id == null){
            id = UUID.randomUUID().toString();
        }
        String object = null;
        try {
            object = rmtParser.objToString(obj);
            Message message = MessageBuilder.
                    withBody(object.getBytes(StandardCharsets.UTF_8)).
                    setContentType(MessageProperties.CONTENT_TYPE_JSON).
                    setContentEncoding(StandardCharsets.UTF_8.name()).
                    setMessageId(id).build();

            CorrelationData correlationData = new CorrelationData(id);
            correlationData.setReturnedMessage(message);

            rabbitTemplate.convertAndSend(DtsConstants.RABBIT_EXCHANGE,
                    DtsConstants.RABBIT_ROUTINGKEY, message,correlationData);

        } catch (AmqpException e) {
            throw new RabbitDtsException("rabbit send error",e);
        } catch (Exception e) {
            throw new RabbitDtsException("rmtParser parser error",e);
        }
    }
}
