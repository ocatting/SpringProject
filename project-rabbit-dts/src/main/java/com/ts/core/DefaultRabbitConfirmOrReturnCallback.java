package com.ts.core;

import com.ts.parser.IDtsParser;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Component
public final class DefaultRabbitConfirmOrReturnCallback extends RabbitConfirmOrReturnCallback {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IDtsParser iDtsParser;

    @SneakyThrows
    @Override
    public void reject(Message message) {
        logger.error(iDtsParser.toJSONString(message));
    }
}
