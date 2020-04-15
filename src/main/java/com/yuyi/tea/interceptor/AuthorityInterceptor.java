package com.yuyi.tea.interceptor;

import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.utils.StringUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String[] splitUrl = url.split("/");
            url="";
            //判断url的长度，若长度超过3，则需要判断特殊模式
            if(splitUrl.length>4){
                //判断是否是获取客户的订单列表
                if(splitUrl[2].equals("ordersByCustomer")){
                    url="/admin/ordersByCustomer/{customerId}/{startDate}/{endDate}";
                }else if(splitUrl[2].equals("orders")){
                    url="/admin/orders/{status}/{startDate}/{endDate}";
                }
            }else{
                for(int i=0;i<splitUrl.length-1;i++){
                    url+=splitUrl[i]+"/";
                }
                //判断url是否是/{uid}模式
                if(StringUtil.isInteger(splitUrl[splitUrl.length-1])){
                    url+="{uid}";
                }
                //判断url是否是/{isFetchAll}模式
                else if(splitUrl[splitUrl.length-1].equals("true")||splitUrl[splitUrl.length-1].equals("false")){
                    url+="{isFetchAll}";
                }else{
                    url+=splitUrl[splitUrl.length-1];
                }
            }
            if(type.equals("customer")){
                throw new GlobalException(CodeMsg.NO_AUTHORITY);
            }
            boolean hasAuthority = authorityService.checkAuthorityEnd(uid, url, method);
            if(!hasAuthority){
                log.info("校验结果："+"无权限");
                throw new GlobalException(CodeMsg.NO_AUTHORITY);
            }
            log.info("校验结果："+"有权限");
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
