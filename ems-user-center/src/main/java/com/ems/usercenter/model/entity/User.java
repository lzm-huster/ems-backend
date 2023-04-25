package com.ems.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName User
 */
@TableName(value ="User")
@Data
public class User implements Serializable {
    /**
     * 用户编号
     */
    @TableId(value = "UserID", type = IdType.AUTO)
    private Integer userID;

    /**
     * 用户名
     */
    @TableField(value = "UserName")
    private String userName;

    /**
     * 密码
     */
    @TableField(value = "Password")
    private String password;

    /**
     * 性别
     */
    @TableField(value = "Gender")
    private String gender;

    /**
     * 头像
     */
    @TableField(value = "Avatar")
    private String avatar;

    /**
     * 学号/工号
     */
    @TableField(value = "IDNumber")
    private String IDNumber;

    /**
     * 所属部门
     */
    @TableField(value = "Department")
    private String department;

    /**
     * 电子邮件
     */
    @TableField(value = "Email")
    private String email;

    /**
     * 联系电话
     */
    @TableField(value = "PhoneNumber")
    private String phoneNumber;

    /**
     * 是否删除
     */
    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CreateTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "UpdateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}