package com.ems.usercenter.model.request;

import lombok.Data;

@Data
public class UserLoginReq {

    /**
     * 邮箱or手机号
     */
    private String userNumber;

    /***
     * 密码
     */
    private String password;
    /**
     * 类型
     */
    private Integer loginType;
}
