package com.ems.common;

import lombok.Data;

/**
 * 异常结果包装类
 *
 */
@Data
public class ErrorResult {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 错误详情
     */
    private String exception;

    /**
     * 错误封装
     * @param errorCode 错误码
     * @param e         报错实例
     * @param message   报错信息
     * @return
     */
    public static ErrorResult fail(ErrorCode errorCode, Throwable e, String message) {
        ErrorResult errorResult = ErrorResult.fail(errorCode, e);
        errorResult.setMessage(message);
        return errorResult;
    }

    /**
     * 错误封装
     * @param errorCode 错误码
     * @param e         报错实例
     * @return
     */
    public static ErrorResult fail(ErrorCode errorCode, Throwable e) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setCode(errorCode.getCode());
        errorResult.setMessage(errorCode.getMessage());
        errorResult.setException(e.getClass().getName());
        return errorResult;
    }

    /**
     * 错误封装
     * @param code      自定义码
     * @param message   报错信息
     * @return
     */
    public static ErrorResult fail(Integer code, String message) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setCode(code);
        errorResult.setMessage(message);
        return errorResult;
    }
}