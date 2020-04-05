package com.yuyi.tea.filter;

import com.sun.jndi.toolkit.url.UrlUtil;
import com.yuyi.tea.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Order(1)
@WebFilter(filterName = "ExceptionFilter", urlPatterns = {"/*"})
public class ExceptionFilter implements Filter {

    public static String EXCEPTION="exception";
    //捕获的异常转发到异常处理controller路径
    public static String URL_RETHROW="/error/rethrow";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("exception filter init .......");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 是否已经放有异常栈, 避免循环
        boolean isRethrow = !Objects.isNull(request.getAttribute(EXCEPTION));
        if (isRethrow) {
            chain.doFilter(request, response);
            return;
        }
        try {
            chain.doFilter(request, response);
        } catch (GlobalException e) {
            // 发生异常，保存异常栈
            request.setAttribute(EXCEPTION, e);
            request.getRequestDispatcher(URL_RETHROW).forward(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}

