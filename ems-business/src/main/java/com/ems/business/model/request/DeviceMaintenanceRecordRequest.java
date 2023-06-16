package com.ems.business.model.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class DeviceMaintenanceRecordRequest {
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date maintenanceTime;

    /**
     * 保养内容
     */
    private String maintenanceContent;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

