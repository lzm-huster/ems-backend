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
 * 借用申请表
 * @TableName BorrowApplySheet
 */
@TableName(value ="BorrowApplySheet")
@Data
public class BorrowApplySheet implements Serializable {
    /**
     * 借用申请编号
     */
    @TableId(value = "BorrowID",type = IdType.AUTO)
    private Integer borrowID;

    /**
     * 借用申请单编号
     */
    @TableField(value = "BorrowApplyID")
    private Integer borrowApplyID;

    /**
     * 设备编号
     */
    @TableField(value = "DeviceID")
    private Integer deviceID;

    /**
     * 设备名称
     */
    @TableField(value = "DeviceName")
    private String deviceName;

    /**
     * 设备类型
     */
    @TableField(value = "DeviceType")
    private String deviceType;

    /**
     * 设备型号
     */
    @TableField(value = "DeviceModel")
    private String deviceModel;

    /**
     * 预计归还时间
     */
    @TableField(value = "ExpectedReturnTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedReturnTime;

    /**
     * 实际归还时间
     */
    @TableField(value = "ActualReturnTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualReturnTime;

    /**
     * 借用计费
     */
    @TableField(value = "BorrowFee")
    private BigDecimal borrowFee;

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