package com.ems.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备列表
 * @TableName Device
 */
@TableName(value ="Device")
@Data
public class Device implements Serializable {
    /**
     * 设备编号
     */
    @TableId(value = "DeviceID", type = IdType.AUTO)
    private Integer deviceID;

    /**
     * 设备名称
     */
    @TableField(value = "DeviceName")
    private String deviceName;

    /**
     * 设备型号
     */
    @TableField(value = "DeviceModel")
    private String deviceModel;

    /**
     * 设备类型
     */
    @TableField(value = "DeviceType")
    private String deviceType;

    /**
     * 设备参数
     */
    @TableField(value = "DeviceSpecification")
    private String deviceSpecification;

    /**
     * 设备图片列表
     */
    @TableField(value = "DeviceImageList")
    private String deviceImageList;

    /**
     * 设备状态
     */
    @TableField(value = "DeviceState")
    private String deviceState;

    /**
     * 设备负责人
     */
    @TableField(value = "UserID")
    private Integer userID;

    /**
     * 库存数量
     */
    @TableField(value = "StockQuantity")
    private Integer stockQuantity;

    /**
     * 单价
     */
    @TableField(value = "UnitPrice")
    private BigDecimal unitPrice;

    /**
     * 借用费率
     */
    @TableField(value = "BorrowRate")
    private BigDecimal borrowRate;

    /**
     * 购买日期
     */
    @TableField(value = "PurchaseDate")
    private Date purchaseDate;

    /**
     * 资产编号
     */
    @TableField(value = "AssetNumber")
    private String assetNumber;

    /**
     * 预计报废时间
     */
    @TableField(value = "ExpectedScrapDate")
    private Date expectedScrapDate;

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