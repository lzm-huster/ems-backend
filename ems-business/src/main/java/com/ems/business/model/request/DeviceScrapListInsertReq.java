package com.ems.business.model.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DeviceScrapListInsertReq {

    /**
     * 设备编号
     */

    private Integer deviceID;
    /**
     * 设备责任人
     */

    private String scrapPerson;

    /**
     * 报废时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scrapTime;

    /**
     * 报废原因
     */
    private String scrapReason;
    /**
     * 设备名称
     */

    private String deviceName;


    /**
     * 备注
     */

    private String remark;
}
