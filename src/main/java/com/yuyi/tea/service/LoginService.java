package com.yuyi.tea.service;

import com.yuyi.tea.bean.AuthorityDetail;
import com.yuyi.tea.bean.Clerk;
import com.yuyi.tea.bean.User;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.utils.JwtUtil;
import com.yuyi.tea.common.utils.TokenUtil;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.ClerkMapper;
import com.yuyi.tea.mapper.LoginMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    public static final String COOKIE_NAME_TOKEN = "token";
    private static final String REDIS_AUTHORITY_NAME="authorities";

    /**
     * token过期时间，20 分钟
     */
    public static final int TOKEN_EXPIRE = 60*20;

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ClerkMapper clerkMapper;

    @Autowired
    private SMSService smsService;

    @Autowired
    private ClerkService clerkService;


    /**
     * 判断员工身份证密码登陆是否正确
     * @param response
     * @param identityId
     * @param password
     * @return
     */
    public User loginByIdPsw(HttpServletResponse response, String identityId, String password) {
        User clerk = clerkMapper.getClerkByIdentityId(identityId);
        if(clerk==null){
            throw new GlobalException(CodeMsg.IDENTITYID_NOT_EXIST);
        }else if(!clerk.getPassword().equals(password)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }else{//密码正确，生成token
            //生成cookie
            log.info("登陆成功");
            String token = JwtUtil.createToken(clerk);
            addCookie(response,token);
            clearClerk((Clerk) clerk);
            List<AuthorityDetail> authorities = getAuthorities();
            ((Clerk) clerk).setAuthorities(authorities);
            return clerk;
        }
    }


    //将token存入cookie
    private void addCookie(HttpServletResponse response, String token) {
        log.info("将token存入cookie");
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge((int)JwtUtil.EXPIRATION);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //获取职员权限
    private List<AuthorityDetail> getAuthorities(){
        boolean hasKey=redisService.exists(REDIS_AUTHORITY_NAME);
        List<AuthorityDetail> authorities=null;
        if(hasKey){
            authorities= (List<AuthorityDetail>) redisService.get(REDIS_AUTHORITY_NAME);
            log.info("从缓存中获取职员权限"+authorities);
        }else{
            log.info("从数据库中获取职员权限");
            authorities=loginMapper.getAuthorities();
            log.info("将职员权限存入缓存"+authorities);
            redisService.set(REDIS_AUTHORITY_NAME,authorities);
        }
        return authorities;
    }

    //短信验证码登陆
    public User otpLogin(HttpServletResponse response,String contact, String otp) {
        //判断验证码是否失效
        boolean hasKey = redisService.exists(SMSService.OTP_TOKEN_NAME + ":" + contact);
        if(hasKey){//验证码有效
            String redisOtp= (String) redisService.get(SMSService.OTP_TOKEN_NAME + ":" + contact);
            if(otp.equals(redisOtp)){
                log.info("登陆成功");
                User clerk = clerkMapper.getClerkByContact(contact);
                String token = TokenUtil.getToken();
//                clerk.setAvatar(null);
                clerk.setPassword(null);
                addCookie(response,token);
                List<AuthorityDetail> authorities = getAuthorities();
                ((Clerk) clerk).setAuthorities(authorities);
                return clerk;
            }else{
                log.info("验证码错误");
                throw new GlobalException(CodeMsg.OTP_ERROR);
            }
        }else{
            log.info("验证码失效");
            throw new GlobalException(CodeMsg.OTP_INVALID);
        }
    }

    //发送短信验证码给职员
    public void clerkSms(String contact) {
        //首先判断该职员是否存在
        Clerk clerk = clerkMapper.getClerkByContact(contact);
        if(clerk==null){
            log.info("职员信息不存在");
            throw new GlobalException(CodeMsg.CONTACT_NOT_EXIST);
        }
        //发送短信验证码
        smsService.sendOTP(contact);
    }

    /**
     * 将登陆返回的职员信息内不需要的去除
     * @param clerk
     */
    private void clearClerk(Clerk clerk){
        clerk.setPassword(null);
        clerk.getShop().setPhotos(null);
        clerk.getShop().setShopBoxes(null);
        clerk.getShop().setClerks(null);
        clerk.getShop().setOpenHours(null);
    }

    /**
     * 用户刷新页面后验证是否登陆
     * @param response
     * @param request
     * @return
     */
    public User verifyLogin(HttpServletResponse response, HttpServletRequest request) {
        int uid = (int) request.getAttribute("uid");
        String type=(String) request.getAttribute("type");
        log.info("验证token信息，uid："+uid+"type："+type);
        if(type==null){
            log.info("token无效");
            throw new GlobalException(CodeMsg.TOKEN_INVALID);
        }
        User user=null;
        if(type.equals("customer")){

        }else{
            user=clerkService.getClerk(uid);
            clearClerk((Clerk)user);
            List<AuthorityDetail> authorities = getAuthorities();
            ((Clerk) user).setAuthorities(authorities);
        }
        log.info("获取token用户信息"+user);
        return user;
    }
}
