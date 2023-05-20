package com.ems.business.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.DeviceMapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.response.DeviceList;
import com.ems.business.service.impl.DeviceServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.mapper.UserMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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


    @GetMapping("/getPersonDeviceList")
    //个人信息列表：返回个人名下设备信息列表
    public List<DeviceList> getPersonDeviceList(int UserID)
    {
        List<DeviceList> deviceLists=null;
        deviceLists=deviceServiceImpl.getPersonDeviceList(UserID);

        return deviceLists;
    }

    @GetMapping("getDeviceDetail")
    //根据DeviceID查询详细信息
    public Device getDeviceDetail(int DeviceID)
    {
        Device device=null;
        device=deviceMapper.getDeviceByDeviceID(DeviceID);

        return device;
    }

    @PutMapping ("insertDevice")
    //插入一条Device数据,返回受影响条数
    public int insertDevice(@NotNull Device device, int UserID)
    {

        //提取前端传入实体的属性值
        String deviceName = device.getDeviceName();
        String deviceType = device.getDeviceType();
        String deviceModel = device.getDeviceModel();
        String deviceSpecification = device.getDeviceSpecification();
        String deviceImageList = device.getDeviceImageList();
        Double unitPrice = device.getUnitPrice();
        Integer isPublic = device.getIsPublic();
        //部分数据系统赋值
        device.setUserID(UserID);
        device.setDeviceState("申请中");
        device.setBorrowRate(0.05);
        Date date=new Date();
        Date newDate = DateUtil.offset(date, DateField.DAY_OF_YEAR, 5);
        device.setPurchaseDate(date);
        //预计五年后报废
        device.setExpectedScrapDate(new Date());


        //判断是否存在空值数据
//        StringUtils.isBlank(deviceType);
//        ObjectUtil.isNull();

        // 判断空
        if (StringUtils.isBlank(deviceName)|| StringUtils.isBlank(deviceType)||
                StringUtils.isBlank(deviceSpecification)||ObjectUtil.isNull(deviceModel)
                ||ObjectUtil.isNull(isPublic)||ObjectUtil.isNull(unitPrice)||
                ObjectUtil.isNull(deviceImageList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        int Number=0;
        Number=deviceMapper.insert(device);
        return Number;
    }

    @PutMapping ("UpdateDevice")
    //更新一条Device数据,返回受影响条数
    public int UpdateDevice(@NotNull Device device)
    {
        //提取前端传入实体的属性值
        Integer deviceID = device.getDeviceID();
        String deviceName = device.getDeviceName();
        String deviceType = device.getDeviceType();
        String deviceModel = device.getDeviceModel();
        String deviceSpecification = device.getDeviceSpecification();
        String deviceImageList = device.getDeviceImageList();
        Double unitPrice = device.getUnitPrice();
        Integer isPublic = device.getIsPublic();

        if(ObjectUtil.isNull(deviceID))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键DeviceID为空");
        }else
        // 判断空
        if (StringUtils.isBlank(deviceName)|| StringUtils.isBlank(deviceType)||
                StringUtils.isBlank(deviceSpecification)||ObjectUtil.isNull(deviceModel)
                ||ObjectUtil.isNull(isPublic)||ObjectUtil.isNull(unitPrice)||
                ObjectUtil.isNull(deviceImageList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        int Number=0;
        Number=deviceMapper.updateById(device);

        return Number;
    }


    @GetMapping("/test")
    public List<DeviceList> test(int ID)
    {
        List<DeviceList> device = deviceMapper.getPersonDeviceList(ID);
        return device;
    }


}
