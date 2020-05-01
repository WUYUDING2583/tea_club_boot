package com.yuyi.tea.service;

import com.auth0.jwt.interfaces.Claim;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.utils.JwtUtil;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.AuthorityMapper;
import com.yuyi.tea.mapper.ClerkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LoginService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ClerkMapper clerkMapper;

    @Autowired
    private SMSService smsService;

    @Autowired
    private ClerkService clerkService;

    @Autowired
    private AuthorityMapper authorityMapper;

    /**
     * 存储在cookie中的key
     */
    public static final String COOKIE_NAME_TOKEN = "token";
    /**
     * 用户权限存储在redis中的key
     */
    private static final String REDIS_AUTHORITY_NAME="authorities";

    /**
     * token过期时间，20 分钟
     */
    public static final int TOKEN_EXPIRE = 60*20;


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
            clerkService.clearClerk((Clerk) clerk);
            Position position = ((Clerk) clerk).getPosition();
            List<PositionAutorityFrontDetail> authorities = getPositionAutorityFrontDetails(position);
            ((Clerk) clerk).setPositionAutorityFrontDetails(authorities);
            addCookie(response,token);
            return clerk;
        }
    }


    /**
     * 将token存入cookie
     * @param response
     * @param token
     */
    private void addCookie(HttpServletResponse response, String token) {
        log.info("将token存入cookie");
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge((int)JwtUtil.EXPIRATION);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获取职员前端路由权限
     * @return
     */
    private List<PositionAutorityFrontDetail> getPositionAutorityFrontDetails(Position position){
        boolean hasKey=redisService.exists(REDIS_AUTHORITY_NAME+":"+position.getUid());
        List<PositionAutorityFrontDetail> positionAutorityFrontDetails=null;
        if(hasKey){
            positionAutorityFrontDetails= (List<PositionAutorityFrontDetail>) redisService.get(REDIS_AUTHORITY_NAME+":"+position.getUid());
            log.info("从缓存中获取职员前端路由权限"+positionAutorityFrontDetails);
        }else{
            log.info("从数据库中获取职员前端路由权限");
            positionAutorityFrontDetails=authorityMapper.getPositionAutorityFrontDetails(position.getUid());
            log.info("将职员前端路由权限存入缓存"+positionAutorityFrontDetails);
            redisService.set(REDIS_AUTHORITY_NAME+":"+position.getUid(),positionAutorityFrontDetails);
        }
        return positionAutorityFrontDetails;
    }

    /**
     * 短信验证码登陆
     * @param response
     * @param contact
     * @param otp
     * @return
     */
    public User otpLogin(HttpServletResponse response,String contact, String otp) {
        //判断验证码是否失效
        boolean hasKey = redisService.exists(SMSService.OTP_TOKEN_NAME + ":" + contact);
        if(hasKey){//此号码在5分钟内发送过验证码
            String redisOtp= (String) redisService.get(SMSService.OTP_TOKEN_NAME + ":" + contact);
            if(otp.equals(redisOtp)){
                log.info("登陆成功，清除redis验证码缓存");
                redisService.remove(SMSService.OTP_TOKEN_NAME + ":" + contact);
                User clerk = clerkMapper.getClerkByContact(contact);
                String token = JwtUtil.createToken(clerk);
                ClerkService.clearClerk((Clerk) clerk);
                Position position = ((Clerk) clerk).getPosition();
                List<PositionAutorityFrontDetail> positionAutorityFrontDetails = getPositionAutorityFrontDetails(position);
                ((Clerk) clerk).setPositionAutorityFrontDetails(positionAutorityFrontDetails);
                addCookie(response,token);
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

    /**
     * 发送短信验证码给职员
     * @param contact
     */
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
     * 用户刷新页面后验证是否登陆
     * @param response
     * @param request
     * @return
     */
    public User verifyToken(HttpServletResponse response, HttpServletRequest request) {
        User user = null;
        try {
            int uid = (int) request.getAttribute("uid");
            String type = (String) request.getAttribute("type");
            log.info("验证token信息，uid：" + uid + "type：" + type);
            if (type.equals("customer")) {

            } else {
                user = clerkService.getClerk(uid);
                Position position = ((Clerk) user).getPosition();
                List<PositionAutorityFrontDetail> positionAutorityFrontDetails = getPositionAutorityFrontDetails(position);
                ((Clerk) user).setPositionAutorityFrontDetails(positionAutorityFrontDetails);
            }
            log.info("获取token用户信息" + user);
        }catch (NullPointerException e){
            throw new GlobalException(CodeMsg.TOKEN_NOT_EXIST);
        }
        return user;
    }

    /**
     * 校验移动端职员token
     * @param token
     * @return
     */
    public User verifyToken(String token) {
        User user = null;
        Map<String, Claim> userData = JwtUtil.verifyToken(token);
        if (userData == null) {
            log.info("token无效");
            throw new GlobalException(CodeMsg.TOKEN_INVALID);
        }else {
            Integer uid = userData.get("uid").asInt();
            user = clerkMapper.getClerk(uid);
        }
        return user;
    }

    /**
     * 移动端职员登陆
     * @param clerk
     * @return
     */
    public User moblieLogin(Clerk clerk) {
        User realClerk = clerkMapper.getClerkByIdentityId(clerk.getIdentityId());
        if(realClerk==null){
            throw new GlobalException(CodeMsg.IDENTITYID_NOT_EXIST);
        }else if(!realClerk.getPassword().equals(clerk.getPassword())){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }else{//密码正确，生成token
            //生成cookie
            log.info("登陆成功");
            String token = JwtUtil.createToken(realClerk);
            clerkService.clearClerk((Clerk) realClerk);
            realClerk.setToken(token);
            realClerk.setPassword(null);
            return realClerk;
        }
    }
}
