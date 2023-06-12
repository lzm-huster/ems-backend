package com.ems.business.model.response;

import com.baomidou.mybatisplus.annotation.TableField;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


@Data

public class DeviceRepairDetail {
    /**
     * 维修编号
     */
    @TableField(value = "RepairID")
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
     * 设备名称
     */
    @TableField(value = "DeviceName")
    private String deviceName;

    /**
     * 资产编号
     */
    @TableField(value = "AssetNumber")
    private String assetNumber;

    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;

}
