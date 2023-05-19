package com.ems.exception.handler;


import com.ems.common.ErrorCode;
import com.ems.common.ErrorResult;
import com.ems.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ErrorResult businessExceptionHandler(BusinessException e) {
        log.error("exception.BusinessException", e.getMessage());
        return ErrorResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ErrorResult runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ErrorResult.fail(ErrorCode.SYSTEM_ERROR, e);
    }
}
