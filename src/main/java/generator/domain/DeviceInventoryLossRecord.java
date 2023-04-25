package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 设备盘亏表
 * @TableName DeviceInventoryLossRecord
 */
@TableName(value ="DeviceInventoryLossRecord")
@Data
public class DeviceInventoryLossRecord implements Serializable {
    /**
     * 盘亏编号
     */
    @TableField(value = "InventoryLossID")
    private Integer inventoryLossID;

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
     * 盘亏时间
     */
    @TableField(value = "InventoryLossTime")
    private Date inventoryLossTime;

    /**
     * 盘亏说明
     */
    @TableField(value = "InventoryLossDescription")
    private String inventoryLossDescription;

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