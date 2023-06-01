package com.ems.business.model.request;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceScrapListreq {
    /**
     * 报废编号
     */
    @TableField(value = "ScrapID")
    private Integer scrapID;

    /**
     * 设备编号
     */
    @TableField(value = "DeviceID")
    private Integer deviceID;
    /**
     * 报废时间
     */
    @TableField(value = "ScrapTime")
    private Date scrapTime;

    /**
     * 报废原因
     */
    @TableField(value = "ScrapReason")
    private String scrapReason;
    /**
     * 设备名称
     */
    @TableField(value = "DeviceName")
    private String deviceName;
    /**
     * 设备责任人
     */
    @TableField(value = "ScrapPerson")
    private String scrapPerson;

    /**
     * 报废设备图片列表
     */
    @TableField(value = "ScrapImages")
    private String scrapImages;
    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;
}
