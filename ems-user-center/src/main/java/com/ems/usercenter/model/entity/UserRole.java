package com.ems.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户角色表
 * @TableName UserRole
 */
@TableName(value ="UserRole")
@Data
public class UserRole implements Serializable {
    /**
     * 用户编号
     */
    @MppMultiId
    @TableField(value = "UserID")
    private Integer userID;

    /**
     * 角色编号
     */
    @MppMultiId
    @TableField(value = "RoleID")
    private Integer roleID;

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