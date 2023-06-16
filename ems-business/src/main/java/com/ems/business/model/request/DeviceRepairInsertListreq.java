package com.ems.business.model.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class DeviceRepairInsertListreq {


    /**
     * 设备编号
     */

    private Integer deviceID;

    /**
     * 维修时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date repairTime;

    /**
     * 维修内容
     */

    private String repairContent;

    /**
     * 维修费用
     */

    private BigDecimal repairFee;

    /**
     * 设备名称
     */

    private String deviceName;
    /**
     * 备注
    **/
    private String remark;


}