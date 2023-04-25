package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 设备保养表
 * @TableName DeviceMaintenanceRecord
 */
@TableName(value ="DeviceMaintenanceRecord")
@Data
public class DeviceMaintenanceRecord implements Serializable {
    /**
     * 保养编号
     */
    @TableField(value = "MaintenanceID")
    private Integer maintenanceID;

    /**
     * 设备编号
     */
    @TableField(value = "DeviceID")
    private Integer deviceID;

    /**
     * 保养时间
     */
    @TableField(value = "MaintenanceTime")
    private Date maintenanceTime;

    /**
     * 保养内容
     */
    @TableField(value = "MaintenanceContent")
    private String maintenanceContent;

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