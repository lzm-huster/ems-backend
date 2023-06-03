package com.ems.business.model.request;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class DeviceRepairListreq {
    /**
     * 维修编号
     */

    private Integer repairID;

    /**
     * 设备编号
     */

    private Integer deviceID;

    /**
     * 维修时间
     */

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
