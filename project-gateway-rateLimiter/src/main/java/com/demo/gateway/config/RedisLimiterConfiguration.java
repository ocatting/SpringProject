package com.demo.gateway.config;

import com.demo.gateway.properties.LimiterProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
@EnableConfigurationProperties(LimiterProperties.class)
public class RedisLimiterConfiguration {

    private final static String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisCacheTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(getObjectMapper());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Jackson2JsonRedisSerializer 设置 ObjectMapper
     * @return
     */
    private ObjectMapper getObjectMapper(){
        return new Jackson2ObjectMapperBuilder()
                .findModulesViaServiceLoader(true)
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)))
                .build();
    }
}
