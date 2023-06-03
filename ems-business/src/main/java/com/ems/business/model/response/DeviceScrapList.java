package com.ems.business.model.response;

import com.baomidou.mybatisplus.annotation.TableField;

import java.math.BigDecimal;
import java.util.Date;

public class DeviceScrapList {
    /**
     * 报废编号
     */
    private Integer scrapID;

    /**
     * 设备编号
     */
    private Integer deviceID;
    /**
     * 报废时间
     */
    private Date scrapTime;

    /**
     * 报废原因
     */
    private String scrapReason;
    /**
     * 报废申请单状态
     */
    private String scrapState;

    /**
     * 设备名称
     */
    private String deviceName;


    /**
     * 设备责任人
     */
    private String scrapPerson;
}