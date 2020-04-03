package com.yuyi.tea.service;

import com.yuyi.tea.bean.User;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.LoginMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    public static final String COOKIE_NAME_TOKEN = "token";

    /**
     * token过期时间，1小时
     */
    public static final int TOKEN_EXPIRE = 3600;

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private RedisService redisService;


    //判断员工身份证密码登陆是否正确
    public String loginByIdPsw(HttpServletResponse response, String identityId, String password) {
        User clerkByIdentityId = loginMapper.getClerkByIdentityId(identityId);
        if(clerkByIdentityId==null){
            throw new GlobalException(CodeMsg.IDENTITYID_NOT_EXIST);
        }else if(!clerkByIdentityId.getPassword().equals(password)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }else{//密码正确，将员工id存入缓存
            //生成cookie
            log.info("登陆成功");
            String token = UUID.randomUUID().toString().replace("-", "");
            addCookie(response,token,clerkByIdentityId);
            return token;
        }
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        log.info("将token存入缓存");
        //将token存入到redis
        redisService.set(COOKIE_NAME_TOKEN + ":" + token, user,TOKEN_EXPIRE, TimeUnit.SECONDS );
        //将token写入cookie
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
