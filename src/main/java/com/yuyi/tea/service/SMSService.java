package com.yuyi.tea.service;

import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.utils.NumberUtil;
import com.yuyi.tea.exception.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 发送短信相关业务
 */
@Service
public class SMSService {

    private final Logger log = LoggerFactory.getLogger(SMSService.class);

    @Autowired
    private RedisService redisService;

    // 短信应用 SDK AppID
    private final int APP_ID = 1400244217; // SDK AppID 以1400开头
    // 短信应用 SDK AppKey
    private final String APP_KEY = "ebbe021156ca27d220e3cef80879ee1f";
    // 短信模板 ID，需要在短信应用中申请
    private final int TEMPLATE_ID = 397475; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请
    // 签名
    private final String SMS_SIGN = "活趣公众号"; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是示例，真实的签名需要在短信控制台申请
    //验证码过期时间5分钟
    private final int SMS_EXPIRE=5;
    //缓存名称
    public static final String OTP_TOKEN_NAME="otp";

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    public void sendOTP(String phone){
//        try {
            String otp=null;
            //查看五分钟内是否已经发送过验证码
            boolean hasKey=redisService.exists(OTP_TOKEN_NAME+":"+phone);
            if(hasKey){
                otp= (String) redisService.get(OTP_TOKEN_NAME+":"+phone);
                log.info("五分钟内发送过验证码，从缓存中获取："+otp);
            }else{
                otp= NumberUtil.RandomNumber(6);
                log.info("未发送验证码，创建验证码:"+otp);
                //生成token并存入缓存
                log.info("短信验证码存入缓存");
                redisService.set(OTP_TOKEN_NAME+":"+phone,otp,SMS_EXPIRE, TimeUnit.MINUTES);
            }
//            String[] params = {otp,String.valueOf(SMS_EXPIRE)};
//            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
//            SmsSingleSenderResult result = ssender.sendWithParam("86", contact,
//                    TEMPLATE_ID, params, SMS_SIGN, "", "");
//        } catch (HTTPException e) {
//            // HTTP 响应码错误
//            e.printStackTrace();
//            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
//        } catch (JSONException e) {
//            // JSON 解析错误
//            e.printStackTrace();
//            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
//        } catch (IOException e) {
//            // 网络 IO 错误
//            e.printStackTrace();
//            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
//        }
    }
}
