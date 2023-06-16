package com.ems.business.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class DeviceScrapDetail {
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
     * 设备责任人
     */
    @TableField(value = "ScrapPerson")
    private String scrapPerson;
    /**
     * 报废时间
     */
    @TableField(value = "ScrapTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 资产编号
     */
    @TableField(value = "AssetNumber")
    private String assetNumber;

    /**
     * 报废设备图片列表
     */
    @TableField(value = "ScrapImages")
    private List<String> scrapImages;

    /**
     * 设备报废状态
     */
    @TableField(value = "ScrapState")
    private String scrapState;
    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;
}
