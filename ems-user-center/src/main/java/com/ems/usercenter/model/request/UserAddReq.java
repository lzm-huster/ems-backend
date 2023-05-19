package com.ems.usercenter.model.request;

import lombok.Data;

@Data
public class UserAddReq {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别
     */

    private String gender;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 学号/工号
     */

    private String IDNumber;

    /**
     * 所属部门
     */

    private String department;

    /**
     * 电子邮件
     */

    private String email;

    /**
     * 联系电话
     */

    private String phoneNumber;
}
