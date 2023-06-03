package com.ems.business.model.request;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
@Data
public class DeviceCheckListreq {
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

    private Date checkTime;
    /**
     * 设备状态
     */

    private String deviceState;
    /**
     * 核查图片列表
     */

    private String checkImages;
    /**
     * 备注
     */

    private String remark;

}
