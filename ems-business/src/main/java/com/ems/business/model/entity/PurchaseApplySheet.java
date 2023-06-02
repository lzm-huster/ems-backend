package com.ems.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 采购申请单表
 * @TableName PurchaseApplySheet
 */
@TableName(value ="PurchaseApplySheet")
@Data
public class PurchaseApplySheet implements Serializable {
    /**
     * 采购申请单编号
     */
    @TableId(value = "PurchaseApplySheetID", type = IdType.AUTO)
    private Integer purchaseApplySheetID;

    /**
     * 采购申请人
     */
    @TableField(value = "PurchaseApplicantID")
    private Integer purchaseApplicantID;


    /**
     * 采购申请时间
     */
    @TableField(value = "PurchaseApplyDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseApplyDate;

    /**
     * 采购申请说明
     */
    @TableField(value = "PurchaseApplyDescription")
    private String purchaseApplyDescription;

    /**
     * 审批导师编号
     */
    @TableField(value = "ApproveTutorID")
    private Integer approveTutorID;

    /**
     * 采购申请状态
     */
    @TableField(value = "PurchaseApplyState")
    private String purchaseApplyState;

    /**
     * 是否删除
     */
    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CreateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "UpdateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public Integer getPurchaseApplicantID() {
        return purchaseApplicantID;
    }

    public Integer getApproveTutorID() {
        return approveTutorID;
    }

}

