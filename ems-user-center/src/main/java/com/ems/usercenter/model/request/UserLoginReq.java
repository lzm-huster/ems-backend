package com.ems.usercenter.model.request;

import lombok.Data;

@Data
public class UserLoginReq {
    /**
     * 用户学号
     */
    private String userNumber;

    /**
     * 用户密码
     */
    private String userPassword;
}
