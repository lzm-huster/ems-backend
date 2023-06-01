package com.ems.business.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.DeviceMaintenanceRecordMapper;
import com.ems.business.model.entity.DeviceMaintenanceRecord;
import com.ems.business.model.request.DeviceMaintenanceRecordRequest;
import com.ems.business.model.response.DeviceMaintenanceRecordList;
import com.ems.business.service.DeviceMaintenanceRecordService;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@ResponseResult
@RestController
@RequestMapping("/maintenanceID")

public class DeviceMaintenanceRecordController {
    private DeviceMaintenanceRecordService deviceMaintenanceRecordService;
    private DeviceMaintenanceRecordMapper deviceMaintenanceRecordMapper;
    private UserRedisConstant redisConstant;
    @GetMapping("/devicemaintenancelistquery")
    //根据设备保养记录编号返回设备保养记录表
    public DeviceMaintenanceRecord getDeviceMaintenanceRecord (Integer maintenanceid){
        if (ObjectUtil.isNull(maintenanceid)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"设备保养记录Id参数为空");
        }
        DeviceMaintenanceRecord maintenanceRecord = deviceMaintenanceRecordService.getDeviceMaintenanceRecord(maintenanceid);
        return maintenanceRecord;}

    //新增一条设备保养记录返回受影响条数，成功返回1，失败返回0
    @PostMapping("/devicemaintenancelistinsert")
    public int insertDeviceMaintenanceRecord(@NotNull DeviceMaintenanceRecord deviceMaintenanceRecord) {

        Integer deviceiD = deviceMaintenanceRecord.getDeviceID();
        Integer maintenanceid = deviceMaintenanceRecord.getMaintenanceID();
        Date maintenancetime = deviceMaintenanceRecord.getMaintenanceTime();
        String maintenancecontent = deviceMaintenanceRecord.getMaintenanceContent();
        if (ObjectUtil.isNull(deviceiD )|| ObjectUtil.isNull(maintenanceid)|| ObjectUtil.isNull(maintenancetime) ||ObjectUtil.isNull(maintenancecontent)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        int number = 0;
        number = deviceMaintenanceRecordMapper.insert(deviceMaintenanceRecord);
        return number;
    }
    //更新一条设备保养记录返回受影响条数，成功返回1，失败返回0
    @PostMapping("/devicemaintenancelistupdate")
    public int updateDeviceMaintenanceRecord(@NotNull DeviceMaintenanceRecord deviceMaintenanceRecord) {

        Integer maintenanceid = deviceMaintenanceRecord.getMaintenanceID();

        if(ObjectUtil.isNull(maintenanceid))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备保养记录Id参数为空");
        }

        deviceMaintenanceRecord.setUpdateTime(new Date());
        int number = 0;
        number = deviceMaintenanceRecordMapper.updateById(deviceMaintenanceRecord);
        return number;
    }

    //根据用户编号返回设备保养数据
    @GetMapping("/getDeviceMaintenanceRecordList")
    public List<DeviceMaintenanceRecordList> getDeviceMaintenanceRecordList(@RequestHeader("token") String token){

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

        List<DeviceMaintenanceRecordList> deviceMaintenanceRecordList= deviceMaintenanceRecordService.getDeviceMaintenanceRecordList(UserID);
        return deviceMaintenanceRecordList;
    }
    //对保养中设备进行计数
    @GetMapping("/getMaintenanceDeviceNumber")
    public int getMaintenanceDeviceNumber(@RequestHeader("token") String token){

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

        int number=0;
        number = deviceMaintenanceRecordMapper.getAllMaintenanceDeviceNumber(UserID);
        return number;
    }
};



