package com.ts.config;

import com.ts.constants.DtsConstants;
import com.ts.core.DefaultRabbitConfirmOrReturnCallback;
import com.ts.core.RabbitConfirmOrReturnCallback;
import com.ts.core.RabbitDtsListener;
import com.ts.listener.IRabbitDtsListener;
import com.ts.parser.JacksonDtsParser;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
@ConditionalOnClass(EnableRabbit.class)
public class RabbitDtsConfiguration {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    protected RabbitAdmin rabbitAdmin;

    @Bean
    @ConditionalOnMissingClass
    public JacksonDtsParser jacksonDtsParser(){
        return new JacksonDtsParser();
    }

    @Bean
    @ConditionalOnMissingBean(RabbitConfirmOrReturnCallback.class)
    public DefaultRabbitConfirmOrReturnCallback rabbitConfirmOrReturnCallback(){
        //不存在 RabbitConfirmOrReturnCallback 则实例化一个默认的 DefaultRabbitConfirmOrReturnCallback
        System.out.println("当Spring IOC中没有 RabbitConfirmOrReturnCallback 的实现时实现一个默认的");
        return new DefaultRabbitConfirmOrReturnCallback();
    }

    @Bean
    @ConditionalOnBean(IRabbitDtsListener.class)
    public RabbitDtsListener rabbitDtsListener(){
        //不存在IRabbitDtsListener则不实例化RabbitDtsListener
        return new RabbitDtsListener();
    }

    @PostConstruct
    protected void init() {

        rabbitTemplate.setConfirmCallback(rabbitConfirmOrReturnCallback());
        rabbitTemplate.setReturnCallback(rabbitConfirmOrReturnCallback());

        // define deadletter exchange and queue
        rabbitAdmin.declareExchange(new DirectExchange(DtsConstants.RABBIT_DEADLETTER_EXCHANGE, true, false));
        rabbitAdmin.declareQueue(new Queue(DtsConstants.RABBIT_DEADLETTER_QUEUE, true, false, false, null));
        rabbitAdmin.declareBinding(new Binding(DtsConstants.RABBIT_DEADLETTER_QUEUE, Binding.DestinationType.QUEUE,
                DtsConstants.RABBIT_DEADLETTER_EXCHANGE, DtsConstants.RABBIT_DEADLETTER_ROUTINGKEY, null));

        // define simple exchange, queue with deadletter support and binding
        rabbitAdmin.declareExchange(new TopicExchange(DtsConstants.RABBIT_EXCHANGE, true, false));
        Map<String, Object> args = new HashMap<>(2);
        args.put("x-dead-letter-exchange", DtsConstants.RABBIT_DEADLETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DtsConstants.RABBIT_DEADLETTER_ROUTINGKEY);
        rabbitAdmin.declareQueue(new Queue(DtsConstants.RABBIT_QUEUE, true, false, true, args));

        // declare binding
        rabbitAdmin.declareBinding(new Binding(DtsConstants.RABBIT_QUEUE, Binding.DestinationType.QUEUE, DtsConstants.RABBIT_EXCHANGE,
                DtsConstants.RABBIT_ROUTINGKEY, null));
    }

}
