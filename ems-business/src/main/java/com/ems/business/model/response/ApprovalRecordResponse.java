package com.ems.business.model.response;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class ApprovalRecordResponse {
//    private Object originalObject;
//    private String Approver;
//
//    public ApprovalRecordResponse(Object originalObject, String approver) {
//        this.originalObject = originalObject;
//        this.Approver = approver;
//
//
//    }

    /**
     * 审批记录编号
     */
    private Integer approvalID;

    /**
     * 申请单编号
     */
    private Integer applySheetID;

    /**
     * 申请类型
     */
    private String applyType;

    /**
     * 审批人名字
     */
    private Integer approver;

    /**
     * 审批时间
     */
    private Date approvalDate;

    /**
     * 审批说明
     */
    private String approvalDescription;


}
