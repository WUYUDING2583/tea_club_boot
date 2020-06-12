package com.yuyi.tea.service;

import com.yuyi.tea.bean.Order;
import com.yuyi.tea.bean.Reservation;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.CommConstants;
import com.yuyi.tea.common.utils.NumberUtil;
import com.yuyi.tea.common.utils.TimeUtil;
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
    private final int OTP_ID = 397475; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请
    private final int RESERVATION_CLOSE_ID=628630;
    private final int REFUND_SUCCESS_ID=628632;
    private final int ORDER_SHIPPED_ID=632632;
    private final int ORDER_PREPARED_ID=632633;
    private final int CHARGE_SUCCESS_ID=632596;
    private static final int REFUND_REJECT_ID = 632955;
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
        if(CommConstants.CLOSE_SMS){
            return;
        }
        try {
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
            String[] params = {otp,String.valueOf(SMS_EXPIRE)};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    OTP_ID, params, SMS_SIGN, "", "");
            log.info("发送短信结果："+result.toString());
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        }
    }

    /**
     * 预约临近短信通知
     * @param order
     */
    public void sendReservationClose(Order order){
        if(CommConstants.CLOSE_SMS){
            return;
        }
        try {
            String content="";
            for(Reservation reservation:order.getReservations()){
                content+= TimeUtil.convertTimestampToyyyMMdd(reservation.getReservationTime())+" "+TimeUtil.convertTimestampToHHmm(reservation.getReservationTime())+"~"+TimeUtil.convertTimestampToHHmm(reservation.getReservationTime()+reservation.getBox().getDuration()*1000*60)+"\n";
            }
            String[] params = {order.getReservations().get(0).getBox().getName(),content};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", order.getCustomer().getContact(),
                    RESERVATION_CLOSE_ID, params, SMS_SIGN, "", "");
            log.info("发送短信结果："+result.toString());
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        }

    }


    /**
     * 退款成功短信通知
     * @param order
     */
    public void sendRefundSuccess(Order order){
        if(CommConstants.CLOSE_SMS){
            return;
        }
        try {
            String content="";
            for(Reservation reservation:order.getReservations()){
                content+= TimeUtil.convertTimestampToyyyMMdd(reservation.getReservationTime())+" "+TimeUtil.convertTimestampToHHmm(reservation.getReservationTime())+"~"+TimeUtil.convertTimestampToHHmm(reservation.getReservationTime()+reservation.getBox().getDuration()*1000*60)+"\n";
            }
            String[] params = {order.getUid()+"",order.getIngot()+"",order.getCredit()+""};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", order.getCustomer().getContact(),
                    REFUND_SUCCESS_ID, params, SMS_SIGN, "", "");
            log.info("发送短信结果："+result.toString());
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        }

    }


    /**
     * 订单发货通知
     * @param order
     */
    public void sendOrderShipped(Order order){
        if(CommConstants.CLOSE_SMS){
            return;
        }
        try {
            String[] params = {TimeUtil.convertTimestampToyyyMMdd(order.getOrderTime()),TimeUtil.convertTimestampToHHmm(order.getOrderTime()),order.getUid()+""};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", order.getCustomer().getContact(),
                    ORDER_SHIPPED_ID, params, SMS_SIGN, "", "");
            log.info("发送短信结果："+result.toString());
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        }

    }

    /**
     * 商品备好通知
     * @param order
     */
    public void sendOrderPrepared(Order order){
        if(CommConstants.CLOSE_SMS){
            return;
        }
        try {
            String[] params = {TimeUtil.convertTimestampToyyyMMdd(order.getOrderTime()),TimeUtil.convertTimestampToHHmm(order.getOrderTime()),order.getUid()+""};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", order.getCustomer().getContact(),
                    ORDER_PREPARED_ID, params, SMS_SIGN, "", "");
            log.info("发送短信结果："+result.toString());
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        }

    }

    /**
     * 充值成功通知
     * @param timestamp
     * @param value
     * @param contact
     */
    public void sendChargeSuccess(long timestamp,float value,String contact){
        if(CommConstants.CLOSE_SMS){
            return;
        }
        try {
            String[] params = {TimeUtil.convertTimestampToyyyMMdd(timestamp),TimeUtil.convertTimestampToHHmm(timestamp),value+""};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", contact,
                    CHARGE_SUCCESS_ID, params, SMS_SIGN, "", "");
            log.info("发送短信结果："+result.toString());
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        }

    }

    /**
     * 商家拒绝退款通知
     * @param order
     */
    public void sendRefundReject(Order order) {
        if(CommConstants.CLOSE_SMS){
            return;
        }
        try {
            String[] params = {order.getUid()+""};
            SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", order.getCustomer().getContact(),
                    REFUND_REJECT_ID, params, SMS_SIGN, "", "");
            log.info("发送短信结果："+result.toString());
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
            throw new GlobalException(CodeMsg.SMS_SEND_ERROR);
        }
    }
}
