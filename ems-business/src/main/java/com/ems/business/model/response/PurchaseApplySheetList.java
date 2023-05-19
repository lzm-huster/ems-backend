package com.ems.business.model.response;

import java.util.Date;

/**
 * 设备采购申请单列表 Response
 *申请单编号、设备名称（多设备组合成一条字符串）、申请人、申请时间、采购申请状态、责任导师（学生)
 */

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
    private Date purchaseApplyDate;

    /**
     * 采购申请状态
     */
    private String purchaseApplyState;

    /**
     * 责任导师名称
     */
    private String approveTutorName;

    public void SetPurchaseApplySheetID(int PurchaseApplySheetID)
    {
        purchaseApplySheetID= PurchaseApplySheetID;
    }

    public Integer getPurchaseApplySheetID()
    {
        return purchaseApplySheetID;
    }

    public void SetDeviceList(String DeviceList)
    {
        deviceList= DeviceList;
    }

    public String getDeviceList()
    {
        return deviceList;
    }
    public void SetUserName(String UserName)
    {
        userName= UserName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void SetPurchaseApplyDate(Date PurchaseApplyDate)
    {
        purchaseApplyDate= PurchaseApplyDate;
    }

    public Date  getPurchaseApplyDate()
    {
        return purchaseApplyDate;
    }
    public void SetPurchaseApplyState(String PurchaseApplyState)
    {
        purchaseApplyState= PurchaseApplyState;
    }

    public String getPurchaseApplyState()
    {
        return purchaseApplyState;
    }
    public void SetApproveTutorName(String ApproveTutorName)
    {
        approveTutorName= ApproveTutorName;
    }

    public String getApproveTutorName()
    {
        return approveTutorName;
    }

}
