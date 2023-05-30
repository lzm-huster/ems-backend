package com.ems.usercenter.model.request;

import lombok.Data;

@Data
public class UserRegisterReq {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 学号/工号
     */
    private String IDNumber;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 确认密码
     */
    private String confirm;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户手机号
     */
    private String phoneNumber;
    /**
     * 验证码
     */
    private String captcha;
    /**
     * 注册类型 1-邮箱注册 2-手机号注册
     */
    private Integer registerType;

}
