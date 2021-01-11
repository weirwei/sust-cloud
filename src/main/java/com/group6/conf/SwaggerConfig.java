package com.group6.conf;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
/**
 * @ClassName: SwaggerConfig
 * @Description: 接口测试文档
 * @Author: 直宇恒
 */
// 被SpringBoot扫描到，通知 spring 这是一个配置类
@Configuration
// 引入 Swagger2
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // 方法需要有ApiOperation注解才能生存接口文档
                // .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 默认 api所在的package 都生成
                .apis(RequestHandlerSelectors.basePackage("com.group6.controller"))
                // 页面风格
                .paths(PathSelectors.any())
                // 运行并初始化相关内容
                .build().apiInfo(
                        new ApiInfoBuilder()
                                .title("科大云盘 接口测试")
                                .description("科大云盘相关接口的测试文档，主要测试个人用户模块，管理用户模块，文件操作模块")
                                .version("1.0")
                                .build()
                );
    }
}


