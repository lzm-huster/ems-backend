package com.ems.business.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
     * 资产编号
     */
    private String assetNumber;

    /**
     * 设备名称
     */
    @TableField(value = "DeviceName")
    private String deviceList;

    /**
     * 申请人名称
     */
    @TableField(value = "UserName")
    private String userName;

    /**
     * 借用申请时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date borrowApplyDate;

    /**
     * 借用申请状态
     */
    private String borrowApplyState;

    /**
     * 责任导师名称
     */
    @TableField(value = "UserName")
    private String approveTutorName;


}
