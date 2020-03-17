package com.yuyi.tea.exception;

public class UserException extends RuntimeException {

    private String error;
    private String msg;

    public UserException(String error, String msg) {
        super(msg);
        this.error = error;
        this.msg = msg;
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
}
