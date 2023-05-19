package com.ems.business.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备报废表
 * @TableName DeviceScrapRecord
 */
@TableName(value ="DeviceScrapRecord")
@Data
public class DeviceScrapRecord implements Serializable {
    /**
     * 报废编号
     */
    @TableField(value = "ScrapID")
    private Integer scrapID;

    /**
     * 设备编号
     */
    @TableField(value = "DeviceID")
    private Integer deviceID;

    /**
     * 设备责任人
     */
    @TableField(value = "ScrapPerson")
    private String scrapPerson;

    /**
     * 报废设备图片列表
     */
    @TableField(value = "ScrapImages")
    private String scrapImages;

    /**
     * 报废时间
     */
    @TableField(value = "ScrapTime")
    private Date scrapTime;

    /**
     * 报废原因
     */
    @TableField(value = "ScrapReason")
    private String scrapReason;

    /**
     * 设备状态
     */
    @TableField(value = "DeviceState")
    private String deviceState;

    @TableField(value = "ScrapState")
    private String scrapState;

    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;

    /**
     * 是否删除
     */
    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UpdateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}