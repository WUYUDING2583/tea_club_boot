package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.User;
import com.yuyi.tea.service.LoginService;
import com.yuyi.tea.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;


    //职员通过身份证密码登陆
    @PostMapping("/clerk/idLogin")
    public User clerkIdLogin(HttpServletResponse response,@RequestBody Clerk loginClerk){
        log.info("职员身份证密码登陆id:"+loginClerk.getIdentityId()+" psw:"+loginClerk.getPassword());
        User clerk= loginService.loginByIdPsw(response, loginClerk.getIdentityId(), loginClerk.getPassword());
        return clerk;
    }

    //发送短信验证码
    @PostMapping("/clerk/sms")
    public String clerkSms(@RequestParam("contact") String contact){
        loginService.clerkSms(contact);
        return "success";
    }

    //短信验证码登陆
    @PostMapping("/clerk/otpLogin")
    public User clerkOtpLogin(HttpServletResponse response,@RequestParam("contact") String contact,
                              @RequestParam("otp") String otp){
        log.info("职员短信验证码登陆phone:"+contact+" otp:"+otp);
        User clerk= loginService.otpLogin(response,contact,otp);
        return clerk;
    }
}
