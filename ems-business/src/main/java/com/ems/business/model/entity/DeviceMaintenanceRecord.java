package com.ems.business.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备保养表
 * @TableName DeviceMaintenanceRecord
 */
@TableName(value ="DeviceMaintenanceRecord")
@Data
public class DeviceMaintenanceRecord implements Serializable {
    /**
     * 保养编号
     */
    @TableId(value = "MaintenanceID",type = IdType.AUTO)
    private Integer maintenanceID;

    /**
     * 设备编号
     */
    @TableField(value = "DeviceID")
    private Integer deviceID;

    /**
     * 保养时间
     */
    @TableField(value = "MaintenanceTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date maintenanceTime;

    /**
     * 保养内容
     */
    @TableField(value = "MaintenanceContent")
    private String maintenanceContent;

    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;

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