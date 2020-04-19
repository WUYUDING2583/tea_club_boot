package com.yuyi.tea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 为response返回access-control-allow-origin请求头
 * 使前后端分离开发可进行同域请求
 * allowedOrigins(pathname) pathname为允许同域请求的地址
 * @return
 */
@Configuration
public class WebMvcConfig {


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        //可进行同源请求的url一下依次为后台host，移动端host
        String[] urls={"http://localhost:3000","http://localhost:8081"};
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(urls)
                        .allowedMethods("*")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }
}
