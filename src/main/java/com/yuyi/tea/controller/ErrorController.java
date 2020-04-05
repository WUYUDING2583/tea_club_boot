package com.yuyi.tea.controller;

import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.filter.ExceptionFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 统一处理filter异常
 */
@Slf4j
@RestController
public class ErrorController {

    @Resource
    private HttpServletRequest request;

    /**
     * 重新抛出异常
     */
    @RequestMapping("/error/rethrow")
    public void rethrow() {
        log.info("重新抛出异常");
        throw ((GlobalException) request.getAttribute(ExceptionFilter.EXCEPTION));
    }
}

