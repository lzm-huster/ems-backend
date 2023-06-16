package com.ems.business.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceScrapListReq {

    /**
     * 设备编号
     */

    private Integer deviceID;
    /**
     * 报废时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
     * 设备责任人
     */

    private String scrapPerson;

    /**
     * 备注
     */

    private String remark;
}
