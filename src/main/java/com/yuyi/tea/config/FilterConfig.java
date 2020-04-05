package com.yuyi.tea.config;

import com.yuyi.tea.filter.ExceptionFilter;
import com.yuyi.tea.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器注册
 */
@Configuration
public class FilterConfig {
    /**
     * 配置过滤器及其顺序
     * @return
     */
    @Bean
    public FilterRegistrationBean exceptionFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        ExceptionFilter exceptionFilter = new ExceptionFilter();
        filterRegistrationBean.setFilter(exceptionFilter);
        filterRegistrationBean.addUrlPatterns("/*");//配置过滤规则
        filterRegistrationBean.setName("exceptionFilter");//设置过滤器名称
        filterRegistrationBean.setOrder(1);//设置过滤其顺序
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean tokenFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        TokenFilter tokenFilter=new TokenFilter();
        filterRegistrationBean.setFilter(tokenFilter);
        String[] urls={"/admin/*","/verifyToken"};
        filterRegistrationBean.addUrlPatterns(urls);//配置过滤规则
        filterRegistrationBean.setName("tokenFilter");//设置过滤器名称
        filterRegistrationBean.setOrder(2);//设置过滤其顺序
        return filterRegistrationBean;
    }
}
