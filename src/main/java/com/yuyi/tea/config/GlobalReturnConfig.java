package com.yuyi.tea.config;


import com.google.gson.Gson;
import com.yuyi.tea.common.Result;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 此配置使得返回值统一
 * 以component.Result的格式返回数据
 */
@Configuration
public class GlobalReturnConfig {

    static Gson gson=new Gson();

    @RestControllerAdvice
    static class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class converterType) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class selectedConverterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
            if (body instanceof Result) {
                return body;
            }
            if (body instanceof String) {
                return gson.toJson(new Result(body)).toString();
            }
            return new Result(body);
        }
    }
}
