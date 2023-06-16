package com.ems.business.model.request;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class DeviceCheckUpdateListReq {

    /**
     * 核查编号
     */

    private Integer checkID;
    /**
     * 设备编号
     */

    private Integer deviceID;

    /**
     * 设备责任人
     */

    private String checker;

    /**
     * 核查时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    /**
     * 设备状态
     */

    private String deviceState;

    /**
     * 备注
     */

    private String remark;
}
