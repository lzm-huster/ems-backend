package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 设备核查表
 * @TableName DeviceCheckRecord
 */
@TableName(value ="DeviceCheckRecord")
@Data
public class DeviceCheckRecord implements Serializable {
    /**
     * 核查编号
     */
    @TableField(value = "CheckID")
    private Integer checkID;

    /**
     * 设备编号
     */
    @TableField(value = "DeviceID")
    private Integer deviceID;

    /**
     * 设备责任人
     */
    @TableField(value = "Checker")
    private String checker;

    /**
     * 核查时间
     */
    @TableField(value = "CheckTime")
    private Date checkTime;

    /**
     * 核查图片列表
     */
    @TableField(value = "CheckImages")
    private String checkImages;

    /**
     * 设备状态
     */
    @TableField(value = "DeviceState")
    private String deviceState;

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