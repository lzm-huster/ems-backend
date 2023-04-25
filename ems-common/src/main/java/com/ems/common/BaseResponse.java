package com.ems.common;

import lombok.Data;

import java.io.Serializable;
/**
 * 通用返回类
 *
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回消息
     */
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}