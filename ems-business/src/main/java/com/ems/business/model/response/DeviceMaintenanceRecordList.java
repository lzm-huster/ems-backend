package com.ems.business.model.response;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DeviceMaintenanceRecordList {
    /**
     * 保养编号
     */
    private Integer maintenanceID;

    /**
     * 设备编号
     */
    private Integer deviceID;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 资产编号
     */
    private String assetNumber;

    /**
     * 保养时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date maintenanceTime;

    /**
     * 保养内容
     */
    private String maintenanceContent;
}
