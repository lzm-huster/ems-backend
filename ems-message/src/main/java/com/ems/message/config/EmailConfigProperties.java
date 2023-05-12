package com.ems.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfigProperties {
    private String host;
    private String port;
    private String username;
    private String password;
}
