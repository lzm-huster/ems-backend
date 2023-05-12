package com.ems.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限表
 * @TableName Permission
 */
@TableName(value ="Permission")
@Data
public class Permission implements Serializable {
    /**
     * 权限编号
     */
    @TableId(value = "PermissionID", type = IdType.AUTO)
    private Integer permissionID;

    /**
     * 权限名称
     */
    @TableField(value = "PermissionName")
    private String permissionName;

    /**
     * 权限描述
     */
    @TableField(value = "PermissionDescription")
    private String permissionDescription;

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