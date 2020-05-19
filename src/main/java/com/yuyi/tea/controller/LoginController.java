package com.yuyi.tea.controller;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.utils.JwtUtil;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ClerkService clerkService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SMSService smsService;

    /**
     * 职员通过身份证密码登陆
     * @param response
     * @param loginClerk
     * @return
     */
    @PostMapping("/admin/idLogin")
    public User clerkIdLogin(HttpServletResponse response,@RequestBody Clerk loginClerk){
        User clerk= loginService.loginByIdPsw(response, loginClerk.getIdentityId(), loginClerk.getPassword());
        return clerk;
    }

    /**
     * 管理端发送短信验证码
     * @param contact
     * @return
     */
    @PostMapping("/clerkSms")
    public String clerkSms(@RequestParam("contact") String contact){
        loginService.clerkSms(contact);
        return "success";
    }

    /**
     * 小程序发送短信验证码
     * @param contact
     * @return
     */
    @GetMapping("/mp/sms/{contact}")
    public String mpSms(@PathVariable String contact){
        smsService.sendOTP(contact);
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
        loginService.otpLogin(contact,otp);
        User clerk = clerkService.getClerkByContact(contact);
        String token = JwtUtil.createToken(clerk);
        ClerkService.clearClerk((Clerk) clerk);
        Position position = ((Clerk) clerk).getPosition();
        List<PositionAutorityFrontDetail> positionAutorityFrontDetails = loginService.getPositionAutorityFrontDetails(position);
        ((Clerk) clerk).setPositionAutorityFrontDetails(positionAutorityFrontDetails);
        loginService.addCookie(response,token);
        return clerk;
    }

    /**
     * 短信验证码登陆
     * @param contact
     * @param otp
     * @return
     */
    @PostMapping("/mp/login")
    public User mpOtpLogin(@RequestParam("contact") String contact,
                              @RequestParam("otp") String otp){
        //校验验证码
        loginService.otpLogin(contact,otp);
        //根据手机号获取客户信息
        User user=customerService.getCustomer(contact);
        if(user==null){
            throw new GlobalException(CodeMsg.CONTACT_NOT_EXIST);
        }
        return user;
    }


    /**
     * 用户刷新页面后验证是否登陆
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/verifyToken")
    public User verifyToken(HttpServletResponse response, HttpServletRequest request){
        User user = loginService.verifyToken(response, request);
        return user;
    }

    /**
     * 移动端登陆验证
     * @param
     * @return
     */
    @PostMapping("/mobile/login")
    public User mobileLogin(@RequestBody Clerk clerk){
        User user = loginService.moblieLogin(clerk);
        return user;
    }

    @GetMapping("/mobile/verifyToken/{token}")
    public User moblieVerifyToken(@PathVariable String token){
        User user = loginService.verifyToken(token);
        return user;
    }

    /**
     * 小程序客户注册
     * @param contact
     * @param otp
     * @return
     */
    @PostMapping("/mp/register")
    public Customer register(@RequestParam("contact") String contact,
                           @RequestParam("otp") String otp,@RequestBody Customer customer){
        //校验验证码
        loginService.otpLogin(contact,otp);
        //检查是否已经注册
        Customer hasCustomer = customerService.getCustomer(customer.getContact());
        if(hasCustomer==null) {
            //根据手机号获取客户信息
            customerService.register(customer);
            return customer;
        }else{
            return hasCustomer;
        }
    }

    @GetMapping("/mp/customer/{contact}")
    public Customer getCustomer(@PathVariable String contact){
        Customer customer = customerService.getCustomer(contact);
        return customer;
    }
}