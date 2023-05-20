package com.ems.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.business.mapper.DeviceMapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.response.DeviceList;
import com.ems.business.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【Device(设备列表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Transactional
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
    implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    //获取个人设备列表
    public List<DeviceList> getPersonDeviceList(int UserID)
    {
        List<DeviceList> deviceList=null;
        deviceList=deviceMapper.getPersonDeviceList(UserID);

        return deviceList;
    }
    //获取所有设备列表
    public List<DeviceList> getAllDeviceList()
    {
        List<DeviceList> deviceList=null;
        deviceList=deviceMapper.getAllDeviceList();

        return deviceList;
    }
    //获取公用设备列表
    public List<DeviceList> getPublicDeviceList()
    {
        List<DeviceList> deviceList=null;
        deviceList=deviceMapper.getPublicDevice();

        return deviceList;
    }
}




