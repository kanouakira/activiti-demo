package com.cqkj.activiti6demo.config;

import com.cqkj.activiti6demo.common.HttpResult;
import com.cqkj.activiti6demo.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 异常捕获
     * @param e 捕获的异常
     * @return 封装的返回响应对象
     */
    @ExceptionHandler(Exception.class)
    public HttpResult handlerException(Exception e){
        ResultCodeEnum resultCodeEnum;

        resultCodeEnum = ResultCodeEnum.SERVER_ERROR;
        resultCodeEnum.setMessage(e.getMessage());
        log.error("common exception:{}", e.getMessage());

        return HttpResult.failure(resultCodeEnum);
    }

}
