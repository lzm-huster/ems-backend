package com.ems.business.model.response;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DeviceRepairListRes {
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


}
