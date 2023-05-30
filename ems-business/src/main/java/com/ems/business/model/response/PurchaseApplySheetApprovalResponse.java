package com.ems.business.model.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangxq
 */
@Data
public class PurchaseApplySheetApprovalResponse {
    private Object originalObject;
    private String Applicant;
    private String Tutor;

    public PurchaseApplySheetApprovalResponse(Object originalObject, String applicant, String tutor) {
        this.originalObject = originalObject;
        this.Applicant = applicant;
        this.Tutor = tutor;

    }

//    public void setNewList(List<Object> ANameList) {
//        this.ANameList = ANameList;
//        this.ANameList = ANameList;
//    }

    // getter and setter methods for originalObject and newList
}




//
//public class PurchaseApplySheetApprovalResponse {
//    /**
//     * 采购申请单编号
//     */
//    private Integer purchaseApplySheetID;
//
//    /**
//     * 采购申请人ID
//     */
//    private Integer purchaseApplicantID;
//
//    /**
//     * 新增：采购申请人名字
//     */
//    private Integer purchaseApplicant;
//
//    /**
//     * 采购申请时间
//     */
//    private Date purchaseApplyDate;
//
//    /**
//     * 采购申请说明
//     */
//    private String purchaseApplyDescription;
//
//    /**
//     * 审批导师ID
//     */
//    private Integer approveTutorID;
//
//    /**
//     * 新增：审批导师名字
//     */
//    private Integer approveTutor;
//
//    /**
//     * 采购申请状态
//     */
//    private String purchaseApplyState;
//
//    /**
//     * 是否删除
//     */
//    private Integer isDeleted;
//
//}