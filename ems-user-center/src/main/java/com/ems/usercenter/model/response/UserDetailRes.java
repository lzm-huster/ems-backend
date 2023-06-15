package com.ems.usercenter.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.Date;

@Data
public class UserDetailRes {
    private Integer userID;

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

    private Integer roleID;
    /**
     * 角色
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String roleDescription;

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
    /**
     * 创建时间
     */
    private Date createTime;
}
