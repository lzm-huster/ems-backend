package com.ems.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限表
 * @TableName RolePermission
 */
@TableName(value ="RolePermission")
@Data
public class RolePermission implements Serializable {
    /**
     * 角色编号
     */
    @MppMultiId
    @TableField(value = "RoleID")
    private Integer roleID;

    /**
     * 权限编号
     */
    @MppMultiId
    @TableField(value = "PermissionID")
    private Integer permissionID;

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