package com.ems.business.model.response;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备借用申请单列表 Response   来源于PuchaseApplySheet表和User表
 *申请单编号、设备名称（多设备组合成一条字符串）、申请人、申请时间、申请状态、责任导师（学生)
 */
@Data
public class BorrowApplySheetList {
    /**
     * 借用申请编号
     */
    private Integer borrowID;

    /**
     * 设备编号
     */
    private Integer deviceID;

    /**
     * 资产编号
     */
    private String assetNumber;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 单价
     */
    private Double unitPrice;

    /**
     * 归还时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    /**
     * 借用计费
     */
    private BigDecimal borrowFee;



}
