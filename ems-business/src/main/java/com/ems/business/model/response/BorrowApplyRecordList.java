package com.ems.business.model.response;

import lombok.Data;

import java.util.Date;

/**
 * 设备借用申请单列表 Response   来源于PuchaseApplySheet表和User表
 *申请单编号、设备名称（多设备组合成一条字符串）、申请人、申请时间、申请状态、责任导师（学生)
 */
@Data
public class BorrowApplyRecordList {
    /**
     * 借用申请单编号
     */
    private Integer borrowApplyID;

    /**
     * 设备名称
     */
    private String deviceList;

    /**
     * 申请人名称
     */
    private String userName;

    /**
     * 采购申请时间
     */
    private Date borrowApplyDate;

    /**
     * 采购申请状态
     */
    private String borrowApplyState;

    /**
     * 责任导师名称
     */
    private String approveTutorName;


}