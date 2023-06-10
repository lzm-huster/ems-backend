package com.ems.business.model.request;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class DeviceScrapListreq {
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
     * 报废设备图片列表
     */

    private MultipartFile scrapImages;
    /**
     * 备注
     */

    private String remark;
}
