package com.yuyi.tea.common;

import com.yuyi.tea.enums.ErrorCodeEnum;

/**
 * 状态码，错误码
 */
public class CodeMsg {

    private int code;
    private String error;
    //通用的错误码
    public static final CodeMsg SUCCESS = new CodeMsg(200, "成功");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常，无法获取数据");
    public static final CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static final CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static final CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500103, "访问太频繁！");
    //登录模块 5002XX
    public static final CodeMsg USER_NOT_LOGIN = new CodeMsg(500200, "用户未登录");
    public static final CodeMsg TOKEN_INVALID = new CodeMsg(500201, "无效token，请重新登陆");
    public static final CodeMsg USERNAME_NOT_EXIST = new CodeMsg(500202, "用户名不存在");
    public static final CodeMsg PASSWORD_ERROR = new CodeMsg(500203, "密码错误");
    public static final CodeMsg IDENTITYID_NOT_EXIST=new CodeMsg(500204,"该身份信息不存在");
    public static final CodeMsg CONTACT_NOT_EXIST=new CodeMsg(500204,"该手机信息不存在");
    public static final CodeMsg TOKEN_NOT_EXIST = new CodeMsg(500205,"token不存在，请重新登陆");
    //短信验证码模块 5003xx
    public static final CodeMsg SMS_SEND_ERROR=new CodeMsg(500300,"验证码发送失败");
    public static final CodeMsg OTP_INVALID = new CodeMsg(500301,"验证码失效请重新获取");
    public static final CodeMsg OTP_ERROR = new CodeMsg(500302,"验证码错误");
    //权限验证模块 5004xx
    public static final CodeMsg NO_AUTHORITY = new CodeMsg(500400,"您没有权限");
    //人脸识别模块 5005xx
    public static final CodeMsg FILE_IS_NULL=new CodeMsg(500500,"文件为空");
    public static final CodeMsg GROUP_ID_IS_NULL=new CodeMsg(500501,"groupId为空");
    public static final CodeMsg NAME_IS_NULL=new CodeMsg(500502,"名字为空");
    public static final CodeMsg NON_REGISTER_CUSTOMER=new CodeMsg(500503,"非注册客户");
    //包厢预约模块 5006xx
    public static CodeMsg RESERVATION_DUPLICATE(String msg){
        return new CodeMsg(500600,msg);
    }
    //付费模块 5007xx
    public static CodeMsg INSUFFICIENT_BALANCE(String msg){
        return new CodeMsg(500700,"余额不足\n"+msg);
    }
    public static final CodeMsg FAIL_IN_PAYMENT=new CodeMsg(500701,"扣费失败，请重试");
    public static final CodeMsg ADD_CREDIT_FAIL=new CodeMsg(500702,"积分赠送失败");
    //订单模块 5008xx
    public static final CodeMsg PLACE_ORDER_FAIL=new CodeMsg(500800,"下单失败");
    public static final CodeMsg UPDATE_ORDER_STATUS_FAIL=new CodeMsg(500801,"修改订单状态失败");
    //注册模块 5009xx
    public static final CodeMsg REGISTER_FAIL=new CodeMsg(500900,"注册失败，请重试");
    public static final CodeMsg DUPLICATE_REGISTER=new CodeMsg(500901,"重复注册");
    //购物车模块 5010xx
    public static final CodeMsg ADD_TO_CART_FAIL=new CodeMsg(501000,"加入购物车失败");

    private CodeMsg() {
    }
    private CodeMsg(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public CodeMsg(ErrorCodeEnum errorCodeEnum){
        this.code=errorCodeEnum.getCode();
        this.error=errorCodeEnum.getDescription();
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.error, args);
        return new CodeMsg(code, message);
    }
    @Override
    public String toString() {
        return "CodeMsg ";
    }
}
