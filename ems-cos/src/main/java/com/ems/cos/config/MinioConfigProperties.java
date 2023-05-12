package com.ems.cos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 */
@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfigProperties {
    /**
     * minio服务API访问入口
     */
    private String endpoint;
    /**
     * 桶名称
     */
    private String bucketName;
    /**
     * 公钥
     */
    private String accessKey;
    /**
     * 私钥
     */
    private String secretKey;
}
