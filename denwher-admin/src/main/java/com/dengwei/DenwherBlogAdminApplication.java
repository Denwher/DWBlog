package com.dengwei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Denwher
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.dengwei.mapper")
@EnableSwagger2
public class DenwherBlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(DenwherBlogAdminApplication.class,args);
    }
}
