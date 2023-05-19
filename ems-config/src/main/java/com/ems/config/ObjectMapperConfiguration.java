package com.ems.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ObjectMapperConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}