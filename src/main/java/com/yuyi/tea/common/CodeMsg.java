package com.yuyi.tea.common;

/**
 * 状态码，错误码
 */
public class CodeMsg {

    private int code;
    private String error;
    //通用的错误码
    public static final CodeMsg SUCCESS = new CodeMsg(200, "成功");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
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

    private CodeMsg() {
    }
    private CodeMsg(int code, String error) {
        this.code = code;
        this.error = error;
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
