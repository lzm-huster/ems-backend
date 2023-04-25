package com.ems.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {
    @Bean //redisTemplate注入到Spring容器
    @ConditionalOnSingleCandidate
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(factory);
        //key序列化
        redisTemplate.setKeySerializer(redisSerializer);
        //value序列化
        redisTemplate.setValueSerializer(redisSerializer);
        //value hashmap序列化
        redisTemplate.setHashKeySerializer(redisSerializer);
        //key hashmap序列化
        redisTemplate.setHashValueSerializer(redisSerializer);
        return redisTemplate;
    }
}

