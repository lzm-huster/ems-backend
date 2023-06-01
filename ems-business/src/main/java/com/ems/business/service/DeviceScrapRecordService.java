package com.ems.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.response.DeviceScrapListRes;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【DeviceScrapRecord(设备报废表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface DeviceScrapRecordService extends IService<DeviceScrapRecord> {

    List<DeviceScrapListRes> getScrapList(int userID);

    List<DeviceScrapListRes> getScrapListAll();
}
