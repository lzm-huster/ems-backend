package com.ems.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.business.model.entity.DeviceMaintenanceRecord;
import com.ems.business.model.response.DeviceMaintenanceRecordList;
import com.ems.business.model.response.DeviceMaintenanceRecordResponse;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【DeviceMaintenanceRecord(设备保养表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface DeviceMaintenanceRecordService extends IService<DeviceMaintenanceRecord> {
    List<DeviceMaintenanceRecordResponse> getDeviceMaintenanceRecord(Integer maintenanceid);
    List<DeviceMaintenanceRecordList> getDeviceMaintenanceRecordList(Integer UserID);

}

