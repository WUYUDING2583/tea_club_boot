package com.yuyi.tea.config;

import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.common.CommConstants;
import com.yuyi.tea.common.Result;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.exception.UserException;
import org.omg.IOP.Codec;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     */
    @ExceptionHandler
    public Object handleException(HttpServletRequest request, HttpServletResponse response, final Exception e) {
        if (e instanceof GlobalException) {//可以在前端Alert的异常
            return new Result(((GlobalException) e).getCodeMsg().getCode(),((GlobalException) e).getCodeMsg().getError());
        } else {//其它异常
            System.out.println(e.toString());
            return new Result(CodeMsg.SERVER_ERROR.getCode(),CodeMsg.SERVER_ERROR.getError());
        }
    }

}
