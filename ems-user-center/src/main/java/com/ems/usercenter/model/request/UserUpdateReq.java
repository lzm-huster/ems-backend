package com.ems.usercenter.model.request;

import lombok.Data;

@Data
public class UserUpdateReq {
    /**
     * 用户角色Id
     */
    private Integer roleId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 性别
     */

    private String gender;

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
