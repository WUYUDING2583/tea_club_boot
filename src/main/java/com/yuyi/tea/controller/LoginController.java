package com.yuyi.tea.controller;

import com.yuyi.tea.bean.AuthorityDetail;
import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.service.LoginService;
import com.yuyi.tea.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private SMSService smsService;

    //职员通过身份证密码登陆
    @PostMapping("/clerk/idLogin")
    public String clerkIdLogin(HttpServletResponse response,
                                              @RequestParam("identityId") String identityId,
                                              @RequestParam("password") String password){
        log.info("职员身份证密码登陆id:"+identityId+" psw:"+password);
        String token = loginService.loginByIdPsw(response, identityId, password);
        return token;
    }

    //发送短信验证码
    @PostMapping("/sms")
    public String sms(@RequestParam("contact") String contact,@RequestParam("token") String token){
        return smsService.sendOTP(contact,token);
    }
}
