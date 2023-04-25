package com.ems.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//http://localhost:8101/api/swagger-ui/index.html
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    /**
     * 用于读取配置文件 application.properties 中 swagger 属性是否开启
     */
    @Value("${spring.swagger.enabled}")
    Boolean swaggerEnabled;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder().build())
                // 是否开启swagger
                .enable(swaggerEnabled)
                .select()
                // 过滤条件，扫描指定注解的接口
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                // 指定路径处理，PathSelectors.any()代表不过滤任何路径
                //.paths(PathSelectors.any())
                .build();
    }
}