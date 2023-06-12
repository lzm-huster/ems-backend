package com.ems.business.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
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
     * 保养时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date maintenanceTime;

    /**
     * 保养内容
     */
    private String maintenanceContent;
}
