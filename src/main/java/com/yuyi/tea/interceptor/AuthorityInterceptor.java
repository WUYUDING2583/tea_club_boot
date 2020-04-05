package com.yuyi.tea.interceptor;

import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.AuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限校验
 */
@Slf4j
public class AuthorityInterceptor  implements HandlerInterceptor {

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
        Arrays.asList("/admin/idLogin", "/admin/otpLogin" )));

    @Autowired
    private AuthorityService authorityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("权限校验");
        String url = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(url);
        if(!allowedPath){
            int uid = (int) request.getAttribute("uid");
            String type= (String) request.getAttribute("type");
            String method = request.getMethod();
            if(type.equals("customer")){
                throw new GlobalException(CodeMsg.NO_AUTHORITY);
            }
            boolean hasAuthority = authorityService.checkAuthorityEnd(uid, url, method);
            log.info("校验结果："+hasAuthority);
            if(!hasAuthority){
                throw new GlobalException(CodeMsg.NO_AUTHORITY);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
