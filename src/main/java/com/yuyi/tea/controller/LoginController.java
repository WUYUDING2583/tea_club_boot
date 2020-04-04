package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.User;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.ClerkService;
import com.yuyi.tea.service.LoginService;
import com.yuyi.tea.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ClerkService clerkService;

    /**
     * 职员通过身份证密码登陆
     * @param response
     * @param loginClerk
     * @return
     */
    @PostMapping("/admin/idLogin")
    public User clerkIdLogin(HttpServletResponse response,@RequestBody Clerk loginClerk){
        log.info("职员身份证密码登陆id:"+loginClerk.getIdentityId()+" psw:"+loginClerk.getPassword());
        User clerk= loginService.loginByIdPsw(response, loginClerk.getIdentityId(), loginClerk.getPassword());
        return clerk;
    }

    /**
     * 发送短信验证码
     * @param contact
     * @return
     */
    @PostMapping("/clerkSms")
    public String clerkSms(@RequestParam("contact") String contact){
        loginService.clerkSms(contact);
        return "success";
    }

    /**
     * 短信验证码登陆
     * @param response
     * @param contact
     * @param otp
     * @return
     */
    @PostMapping("/admin/otpLogin")
    public User clerkOtpLogin(HttpServletResponse response,@RequestParam("contact") String contact,
                              @RequestParam("otp") String otp){
        log.info("职员短信验证码登陆phone:"+contact+" otp:"+otp);
        User clerk= loginService.otpLogin(response,contact,otp);
        return clerk;
    }

    /**
     * 用户刷新页面后验证是否登陆
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/verifyLogin")
    public User verifyToken(HttpServletResponse response, HttpServletRequest request){
        User user = loginService.verifyToken(response, request);
        return user;
    }
}
