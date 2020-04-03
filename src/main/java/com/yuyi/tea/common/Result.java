package com.yuyi.tea.common;

public class Result<T> {
    // 接口调用成功或者失败
    private Integer code = 200;
    // 需要传递的信息，例如错误信息
    private String msg;
    // 需要传递的数据
    private T data;

    public Result() {
    }



    public Result(Integer code,  String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
