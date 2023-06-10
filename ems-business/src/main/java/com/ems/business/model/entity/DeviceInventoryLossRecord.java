package com.ems.business.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备盘亏表
 * @TableName DeviceInventoryLossRecord
 */
@TableName(value ="DeviceInventoryLossRecord")
@Data
public class DeviceInventoryLossRecord implements Serializable {
    /**
     * 盘亏编号
     */
    @TableField(value = "InventoryLossID")
    private Integer inventoryLossID;

    /**
     * 设备编号
     */
    @TableField(value = "DeviceID")
    private Integer deviceID;

    /**
     * 设备责任人
     */
    @TableField(value = "Checker")
    private String checker;

    /**
     * 盘亏时间
     */
    @TableField(value = "InventoryLossTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inventoryLossTime;

    /**
     * 盘亏说明
     */
    @TableField(value = "InventoryLossDescription")
    private String inventoryLossDescription;

    /**
     * 是否删除
     */
    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CreateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UpdateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}