package com.ems.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.business.model.entity.DeviceCheckRecord;
import com.ems.business.model.response.DeviceCheckListRes;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【DeviceCheckRecord(设备核查表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface DeviceCheckRecordService extends IService<DeviceCheckRecord> {

    List<DeviceCheckListRes> getCheckList(int userID);

    List<DeviceCheckListRes> getCheckListAll();

    int getCheckList_CheckingNum_All();

    int getCheckList_CheckingNum(int userID);
}
