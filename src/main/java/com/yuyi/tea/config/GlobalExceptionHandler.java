package com.yuyi.tea.config;

import com.yuyi.tea.common.CommConstants;
import com.yuyi.tea.component.Result;
import com.yuyi.tea.exception.UserException;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     */
    @ExceptionHandler
    public Object handleException(HttpServletRequest request, HttpServletResponse response, final Exception e) {
        if (e instanceof UserException) {//可以在前端Alert的异常
            return new Result(CommConstants.UserException.USER_EXCEPTION_CODE,((UserException) e).getError(),((UserException) e).getMsg());
        } else {//其它异常
            System.out.println(e.toString());
            return new Result(500,  "internal error","服务器内部错误");
        }
    }

}
