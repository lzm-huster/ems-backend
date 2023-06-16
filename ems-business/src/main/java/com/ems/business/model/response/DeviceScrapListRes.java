package com.ems.business.model.response;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class DeviceScrapListRes {
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
     * 设备责任人
     */
    private String scrapPerson;

    /**
     * 资产编号
     */
    private String assetNumber;

}
