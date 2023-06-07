package com.ems.business.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.AuthCheck;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.DeviceMapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.response.DeviceList;
import com.ems.business.service.impl.DeviceServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.mapper.UserMapper;
import com.ems.usercenter.model.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private UserRedisConstant redisConstant;

    @AuthCheck
    @GetMapping("/getDeviceList")
    //设备信息列表：管理员返回所有设备列表数据，其他用户返回公用设备数据
    public List<DeviceList> getDeviceList(@RequestHeader(value = "token",required = false) String token)
    {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

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

    @AuthCheck
    @GetMapping("/getPersonDeviceList")
    //个人信息列表：返回个人名下设备信息列表
    public List<DeviceList> getPersonDeviceList(@RequestHeader(value = "token",required = false) String token)
    {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

        List<DeviceList> deviceLists=null;
        deviceLists=deviceMapper.getPersonDeviceList(UserID);

        return deviceLists;
    }

    @GetMapping("getDeviceDetail")
    //根据DeviceID查询详细信息
    public Device getDeviceDetail(int DeviceID)
    {
        if (ObjectUtil.isNull(DeviceID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数为空");
        }

        Device device=null;
        device=deviceMapper.getDeviceByDeviceID(DeviceID);

        return device;
    }

    @PostMapping ("insertDevice")
    //插入一条Device数据,返回受影响条数
    public int insertDevice(@NotNull Device device)
    {

        //提取前端传入实体的属性值
        String deviceName = device.getDeviceName();
        String deviceType = device.getDeviceType();
        String deviceModel = device.getDeviceModel();
        String deviceSpecification = device.getDeviceSpecification();
        String deviceImageList = device.getDeviceImageList();
        Double unitPrice = device.getUnitPrice();
        Integer isPublic = device.getIsPublic();
        Integer userID = device.getUserID();
        String CategoryCode = device.getAssetNumber();
        //部分数据系统赋值
        device.setDeviceState("申请中");
        device.setBorrowRate(0.05);
        Date date=new Date();
        device.setPurchaseDate(date);
        //预计五年后报废
        device.setExpectedScrapDate(new Date());
        Date newDate = DateUtil.offset(date, DateField.DAY_OF_YEAR, 5);

        // 判断空
        if (StringUtils.isBlank(deviceName)|| StringUtils.isBlank(deviceType)||
                StringUtils.isBlank(deviceSpecification)||ObjectUtil.isNull(deviceModel)
                ||ObjectUtil.isNull(isPublic)||ObjectUtil.isNull(unitPrice)||
                ObjectUtil.isNull(deviceImageList)||
                ObjectUtil.isNull(userID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        // 判断设备资产编号目录是否传入
        if(StringUtils.isBlank(CategoryCode))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入CategoryCode为空");
        }
        String assetNumber=CategoryCode;
        CategoryCode=CategoryCode+"%"; //模糊查询条件
        int num=deviceMapper.getNumberByCategoryCode(CategoryCode)+1;

        if(num<10){assetNumber= assetNumber+"000"+Integer.toString(num);}
        else  if(num<100){assetNumber= assetNumber+"00"+Integer.toString(num);}
        else  if(num<1000){assetNumber= assetNumber+"0"+Integer.toString(num);}
        else {assetNumber= assetNumber+Integer.toString(num);}
        //设置设备资产编号
        device.setAssetNumber(assetNumber);

        int Number=0;
        Number=deviceMapper.insert(device);
        return Number;
    }

    @PostMapping ("UpdateDevice")
    //更新一条Device数据,返回受影响条数
    public int UpdateDevice(@NotNull Device device)
    {
        //提取前端传入实体的属性值
        Integer deviceID = device.getDeviceID();

        if(ObjectUtil.isNull(deviceID))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键DeviceID为空");
        }

        device.setUpdateTime(new Date());

        int Number=0;
        Number=deviceMapper.updateById(device);

        return Number;
    }

    @AuthCheck
    @GetMapping("getLatestDeviceID")
    //在添加记录时获取刚添加记录的DeviceID
    public int getLatestDeviceID(@RequestHeader(value = "token",required = false) String token)
    {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

        int Number=0;
        Number=deviceMapper.getLatestDeviceID(UserID);

        return Number;
    }


    @PostMapping("deleteDeviceByDeviceID")
    //根据DeviceID删除一条Device数据，成功返回1，失败返回0
    public int deleteDeviceByDeviceID(int DeviceID)
    {
        if(ObjectUtil.isNull(DeviceID))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键DeviceID为空");
        }

        int Number=0;
        Number=deviceMapper.deleteDeviceByDeviceID(DeviceID);

        return Number;
    }

    @GetMapping("getAllDeviceIDAndAssetNumber")
    //返回ID与资产编号键值对
    public  List<Map<Integer,String>> getAllDeviceIDAndAssetNumber()
    {
        List<Map<Integer,String>> mapList=new ArrayList<>();
        mapList=deviceMapper.getAllDeviceIDAndAssetNumber();

        return mapList;
    }


}
