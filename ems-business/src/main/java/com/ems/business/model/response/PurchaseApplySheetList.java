package com.ems.business.model.response;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备采购申请单列表 Response  来源于PurchaseApplySheet表和User表
 *申请单编号、设备名称（多设备组合成一条字符串）、申请人、申请时间、采购申请状态、责任导师（学生)
 */
@Data
public class PurchaseApplySheetList {
    /**
     * 采购申请单编号
     */
    private Integer purchaseApplySheetID;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseApplyDate;

    /**
     * 采购申请状态
     */
    private String purchaseApplyState;

    /**
     * 责任导师名称
     */
    private String approveTutorName;

}
