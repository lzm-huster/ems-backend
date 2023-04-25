package com.ems.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableConfigurationProperties(MinioConfigProperties.class)
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.ems")
@MapperScan(basePackages = {"com.ems.**.mapper"})
public class MesApplication {
    public static void main(String[] args) {
        SpringApplication.run(MesApplication.class, args);
    }
}
