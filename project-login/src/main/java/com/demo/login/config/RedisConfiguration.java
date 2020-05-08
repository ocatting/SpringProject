package com.demo.login.config;

import com.demo.login.config.properties.RedisKeyPrefixConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
@EnableConfigurationProperties(RedisKeyPrefixConfig.class)
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        /*
         * 序列化后会产生java类型说明，如果不需要用“Jackson2JsonRedisSerializer”
         * 和“ObjectMapper ”配合效果更好
         */
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 设定不去读取 redis config 配置文件
     * redis 操作 : config get * #读取redis.conf文件内容
     * 这里主要作用是用来去除 redis.conf 未配置 notify-keyspace-events Ex
     * Redis.conf 打开此配置，其中Ex表示键事件通知里面的key过期事件，每当有过期键被删除时，会发送通知(可用消息订阅处理)
     *
     * @return
     */
    @Bean
    public ConfigureRedisAction configureRedisAction(){
        return ConfigureRedisAction.NO_OP;
    }
}
