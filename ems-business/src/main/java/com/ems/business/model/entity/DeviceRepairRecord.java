package com.ems.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备维修记录表
 * @TableName DeviceRepairRecord
 */
@TableName(value ="DeviceRepairRecord")
@Data
public class DeviceRepairRecord implements Serializable {
    /**
     * 维修编号
     */
    @TableId(value = "RepairID",type = IdType.AUTO)
    private Integer repairID;

    /**
     * 设备编号
     */
    @TableField(value = "DeviceID")
    private Integer deviceID;

    /**
     * 维修时间
     */
    @TableField(value = "RepairTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date repairTime;

    /**
     * 维修内容
     */
    @TableField(value = "RepairContent")
    private String repairContent;

    /**
     * 维修费用
     */
    @TableField(value = "RepairFee")
    private BigDecimal repairFee;

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