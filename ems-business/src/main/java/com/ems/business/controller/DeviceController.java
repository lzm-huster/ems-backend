package com.ems.business.controller;

import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.DeviceMapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.response.DeviceList;
import com.ems.business.service.impl.DeviceServiceImpl;
import com.ems.usercenter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ResponseResult
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DeviceServiceImpl deviceServiceImpl;

    @GetMapping("/getDeviceList")
    //设备信息列表：管理员返回所有设备列表数据，其他用户返回公用设备数据
    public List<DeviceList> getDeviceList(int UserID)
    {

        String RoleName=null;
        RoleName=userMapper.getRoleNameByUserID(UserID);

        List<DeviceList> deviceLists=null;
        if(RoleName.contains("deviceAdmin"))
        {
            deviceLists=deviceServiceImpl.getAllDeviceList();

        } else {
            deviceLists=deviceServiceImpl.getPublicDeviceList();
        }
        return deviceLists;
    }



    //个人信息列表：返回个人名下设备信息列表
    public List<DeviceList> getPersonDeviceList(int UserID)
    {
        List<DeviceList> deviceLists=null;
        deviceLists=deviceServiceImpl.getPersonDeviceList(UserID);

        return deviceLists;
    }

    //根据DeviceID查询详细信息
    public Device getDeviceDetail(int DeviceID)
    {
        Device device=null;
        device=deviceMapper.selectById(DeviceID);

        return device;
    }



}
