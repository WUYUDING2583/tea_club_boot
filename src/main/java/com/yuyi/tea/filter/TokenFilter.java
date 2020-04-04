package com.yuyi.tea.filter;

import com.auth0.jwt.interfaces.Claim;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.utils.JwtUtil;
import com.yuyi.tea.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.yuyi.tea.service.LoginService.COOKIE_NAME_TOKEN;

/**
 * JWT过滤器，验证用户token
 * @author 于一
 */
@Slf4j
@Order(1)
@WebFilter(filterName = "TokenFilter", urlPatterns = {"/admin/*","/verifyLogin"})
public class TokenFilter implements Filter {

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/admin/idLogin", "/admin/otpLogin" )));
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(path);
        log.info("校验token");
        response.setCharacterEncoding("UTF-8");
        if (allowedPath) {
           log.info(path+"无需验证token");
        }
        else {
            log.info(path+"验证token");
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length >0) {
                for (Cookie cookie : cookies) {
                    log.info("name:"+cookie.getName()+"value:"+cookie.getValue());
                    if(cookie.getName().equals(COOKIE_NAME_TOKEN)){
                        String token=cookie.getValue();
                        if (token == null) {
                            log.info("token不存在");
                            break;
                        }

                        Map<String, Claim> userData = JwtUtil.verifyToken(token);
                        if (userData == null) {
                            log.info("token无效");
                            break;
                        }else {
                            Integer uid = userData.get("uid").asInt();
                            String name = userData.get("name").asString();
                            String type = userData.get("type").asString();
                            //拦截器 拿到用户信息，放到request中
                            req.setAttribute("uid", uid);
                            req.setAttribute("name", name);
                            req.setAttribute("type", type);
                            log.info("更新token时间");
                            token = JwtUtil.createToken(uid, type, name);
                            cookie = new Cookie(COOKIE_NAME_TOKEN, token);
                            cookie.setMaxAge((int) JwtUtil.EXPIRATION);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    }
                }
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }
}

