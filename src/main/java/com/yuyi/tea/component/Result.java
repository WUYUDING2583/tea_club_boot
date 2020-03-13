package com.yuyi.tea.component;

public class Result<T> {
    // 接口调用成功或者失败
    private Integer code = 200;
    // 失败的具体code
    private String error ;
    // 需要传递的信息，例如错误信息
    private String msg;
    // 需要传递的数据
    private T data;

    public Result(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
