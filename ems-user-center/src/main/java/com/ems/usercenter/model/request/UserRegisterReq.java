package com.ems.usercenter.model.request;

import lombok.Data;

@Data
public class UserRegisterReq {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户学号
     */
    private String userNumber;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户QQ号
     */
    private String QQNumber;

    /**
     * 班级
     */
    private Integer userClassId;
}
