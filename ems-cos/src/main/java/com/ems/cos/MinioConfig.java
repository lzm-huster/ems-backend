package com.ems.cos;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 */
@Configuration
public class MinioConfig {
    @Autowired
    private MinioConfigProperties properties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(properties.getEndpoint())
                .build();
    }
}