package com.ems.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.business.model.entity.DeviceRepairRecord;
import com.ems.business.model.response.DeviceRepairListRes;
import java.util.List;

/**
* @author 龙志明
* @description 针对表【DeviceRepairRecord(设备维修记录表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface DeviceRepairRecordService extends IService<DeviceRepairRecord> {
//查看维修记录
    List<DeviceRepairListRes> getRepairlist(int userID);


    List<DeviceRepairListRes> getAllRepairlist();


}
