package com.yuyi.tea.config;

import com.yuyi.tea.interceptor.AuthorityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 将自定义拦截器作为Bean写入配置
     * @return
     */
    @Bean
    public AuthorityInterceptor authorityInterceptor(){
        return new AuthorityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] urls={"/admin/*","/admin/*/*","/admin/*/*/*/*"};
        registry.addInterceptor(authorityInterceptor()).addPathPatterns(urls);
    }
}
