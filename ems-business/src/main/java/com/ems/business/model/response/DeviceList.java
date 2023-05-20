package com.ems.business.model.response;

import lombok.Data;

import java.util.Date;

/**
 * 设备列表返回信息  来源于Device表和User表
 * 设备编号、设备名称、设备类型、设备型号、设备状态、设备负责人、购买时间
 */
@Data
public class DeviceList {
    /**
     * 设备编号
     */
    private Integer deviceID;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型
     */
    private String deviceType;


    /**
     * 设备型号
     */
    private String deviceModel;


    /**
     * 设备状态
     */
    private String deviceState;

    /**
     * 申请人名称
     */
    private String userName;

    /**
     * 购买日期
     */
    private Date purchaseDate;

    public void SetDeviceID(int DeviceID)
    {
        deviceID=DeviceID;
    }
    public Integer GetDeviceID()
    {
        return deviceID;
    }
    public void SetDeviceName(String DeviceName)
    {
        deviceName=DeviceName;
    }
    public String GetDeviceName()
    {
        return deviceName;
    }
    public void SetDeviceType(String DeviceType)
    {
        deviceType=DeviceType;
    }
    public String GetDeviceType()
    {
        return deviceType;
    }
    public void SetDeviceModel(String DeviceModel)
    {
        deviceModel=DeviceModel;
    }
    public String GetDeviceModel()
    {
        return deviceModel;
    }
    public void SetDeviceState(String DeviceState)
    {
        deviceState=DeviceState;
    }
    public String GetDeviceState()
    {
        return deviceState;
    }
    public void SetUserName(String UserName)
    {
        userName=UserName;
    }
    public String GetUserName()
    {
        return userName;
    }
    public void SetPurchaseDate(Date PurchaseDate)
    {
        purchaseDate=PurchaseDate;
    }
    public Date GetPurchaseDate()
    {
        return purchaseDate;
    }
}