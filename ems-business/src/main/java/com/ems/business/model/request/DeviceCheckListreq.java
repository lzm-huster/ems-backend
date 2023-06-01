package com.ems.business.model.request;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
@Data
public class DeviceCheckListreq {
    /**
     * 核查编号
     */
    @TableField(value = "CheckID")
    private Integer checkID;

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
     * 核查时间
     */
    @TableField(value = "CheckTime")
    private Date checkTime;
    /**
     * 设备状态
     */
    @TableField(value = "DeviceState")
    private String deviceState;
    /**
     * 核查图片列表
     */
    @TableField(value = "CheckImages")
    private String checkImages;
    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;

}
