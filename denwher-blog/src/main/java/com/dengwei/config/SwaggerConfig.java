package com.dengwei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Denwher
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dengwei.controller"))
                .build();
    }

    @Bean
    public ApiInfo apiInfo(){
        Contact contact = new Contact("Denwher", "http://www.denwher.com", "915106059@qq.com");
        return new ApiInfoBuilder()
                .title("博客前台")
                .description("博客前台接口详情")
                .contact(contact)   // 联系方式
                .version("1.1.1")  // 版本
                .build();
    }
}
