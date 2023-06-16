package com.ems.business.model.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import lombok.Data;
@Data
public class DeviceScrapListUpdateReq {

    /**
     * 报废编号
     */

    private Integer scrapID;

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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
