package com.ems.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.business.mapper.DeviceMaintenanceRecordMapper;
import com.ems.business.model.entity.DeviceMaintenanceRecord;
import com.ems.business.model.request.DeviceMaintenanceRecordRequest;
import com.ems.business.model.response.DeviceMaintenanceRecordList;
import com.ems.business.service.DeviceMaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【DeviceMaintenanceRecord(设备保养表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class DeviceMaintenanceRecordServiceImpl extends ServiceImpl<DeviceMaintenanceRecordMapper, DeviceMaintenanceRecord>
    implements DeviceMaintenanceRecordService {
    @Autowired
    private DeviceMaintenanceRecordMapper deviceMaintenanceRecordMapper;
    //根据保养编号返回设备保养数据
    @Override
    public DeviceMaintenanceRecord getDeviceMaintenanceRecord(Integer maintenanceid) {
        DeviceMaintenanceRecord deviceMaintenanceRecordService =deviceMaintenanceRecordMapper.selectById(maintenanceid);
        return deviceMaintenanceRecordService;
    }
    //根据UserID返回设备保养数据
    @Override
    public List<DeviceMaintenanceRecordList> getDeviceMaintenanceRecordList(Integer UserID){
        List<DeviceMaintenanceRecordList> deviceMaintenanceRecordList=deviceMaintenanceRecordMapper.getDeviceMaintenanceRecordList(UserID);
        return deviceMaintenanceRecordList;
    }
}




