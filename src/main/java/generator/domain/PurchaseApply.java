package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 采购申请表
 * @TableName PurchaseApply
 */
@TableName(value ="PurchaseApply")
@Data
public class PurchaseApply implements Serializable {
    /**
     * 采购申请编号
     */
    @TableId(value = "PurchaseApplyID", type = IdType.AUTO)
    private Integer purchaseApplyID;

    /**
     * 采购申请单编号
     */
    @TableField(value = "PurchaseApplySheetID")
    private Integer purchaseApplySheetID;

    /**
     * 设备名称
     */
    @TableField(value = "DeviceName")
    private String deviceName;

    /**
     * 设备类型
     */
    @TableField(value = "DeviceType")
    private String deviceType;

    /**
     * 设备型号
     */
    @TableField(value = "DeviceModel")
    private String deviceModel;

    /**
     * 设备数量
     */
    @TableField(value = "DeviceQuantity")
    private Integer deviceQuantity;

    /**
     * 采购预算
     */
    @TableField(value = "PurchaseBudget")
    private BigDecimal purchaseBudget;

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
     * 修改时间
     */
    @TableField(value = "UpdateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}