package com.ems.redis;

import com.alibaba.fastjson2.support.spring.data.redis.GenericFastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {
    @Bean(name = "redisTemplate") //redisTemplate注入到Spring容器
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(factory);
        //key序列化
        redisTemplate.setKeySerializer(redisSerializer);
        //value序列化
        redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());
        //value hashmap序列化
        redisTemplate.setHashKeySerializer(redisSerializer);
        //key hashmap序列化
        redisTemplate.setHashValueSerializer(new GenericFastJsonRedisSerializer());
        return redisTemplate;
    }
}

