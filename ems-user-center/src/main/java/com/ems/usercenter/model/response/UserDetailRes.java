package com.ems.usercenter.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class UserDetailRes {

    /**
     * 用户名
     */
    private String userName;

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
