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
 * 申请审批记录表
 * @TableName ApprovalRecord
 */
@TableName(value ="ApprovalRecord")
@Data
public class ApprovalRecord implements Serializable {
    /**
     * 审批记录编号
     */
    @TableId(value = "ApprovalID",type = IdType.AUTO)
    private Integer approvalID;

    /**
     * 申请单编号
     */
    @TableField(value = "ApplySheetID")
    private Integer applySheetID;

    /**
     * 申请类型
     */
    @TableField(value = "ApplyType")
    private String applyType;

    /**
     * 审批人编号
     */
    @TableField(value = "ApproverID")
    private Integer approverID;

    /**
     * 审批时间
     */
    @TableField(value = "ApprovalDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalDate;

    /**
     * 审批说明
     */
    @TableField(value = "ApprovalDescription")
    private String approvalDescription;

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
     * 更新时间
     */
    @TableField(value = "UpdateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}