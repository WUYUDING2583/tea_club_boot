package com.yuyi.tea.exception;

public class PhotoTooBigException extends UserException {
    public PhotoTooBigException() {
        super("photo too big", "上传的照片过大，照片最大不能超过1M");
    }
}
