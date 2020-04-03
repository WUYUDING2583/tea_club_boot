package com.yuyi.tea.common;

public class Result<T> {
    // 接口调用成功或者失败
    private Integer code = 200;
    // 需要传递的信息，例如错误信息
    private String error;
    // 需要传递的数据
    private T data;

    public Result() {
    }



    public Result(Integer code,  String error) {
        this.code = code;
        this.error = error;
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


    public String getError() {
        return error;
    }

    public void setError(String msg) {
        this.error = msg;
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
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}
