package com.ems.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 借用申请单表
 * @TableName BorrowApplyRecord
 */
@TableName(value ="BorrowApplyRecord")
@Data
public class BorrowApplyRecord implements Serializable {
    /**
     * 借用申请单编号
     */
    @TableId(value = "BorrowApplyID",type = IdType.AUTO)
    private Integer borrowApplyID;

    /**
     * 借用申请人
     */
    @TableField(value = "BorrowerID")
    private Integer borrowerID;

    /**
     * 借用申请时间
     */
    @TableField(value = "BorrowApplyDate")
    private Date borrowApplyDate;

    /**
     * 借用申请说明
     */
    @TableField(value = "ApplyDescription")
    private String applyDescription;

    /**
     * 审批导师编号
     */
    @TableField(value = "ApproveTutorID")
    private Integer approveTutorID;

    /**
     * 借用申请状态
     */
    @TableField(value = "BorrowApplyState")
    private String borrowApplyState;

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