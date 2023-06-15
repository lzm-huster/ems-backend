package com.ems.business.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
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
     * 资产编号
     */
    private String assetNumber;

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
     * 报废申请单状态
     */
    private String scrapState;

    /**
     * 设备名称
     */
    @TableField(value = "DeviceName")
    private String deviceName;


    /**
     * 设备责任人
     */
    private String scrapPerson;

    /**
     * 用户类型
     */
    @TableField(value = "RoleName")
    private String roleName;
}
