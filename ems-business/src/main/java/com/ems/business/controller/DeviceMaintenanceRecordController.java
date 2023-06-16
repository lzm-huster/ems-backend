package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ems.annotation.AuthCheck;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.DeviceMaintenanceRecordMapper;
import com.ems.business.model.entity.DeviceMaintenanceRecord;
import com.ems.business.model.response.DeviceMaintenanceRecordList;
import com.ems.business.model.response.DeviceMaintenanceRecordResponse;
import com.ems.business.service.DeviceMaintenanceRecordService;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@ResponseResult
@RestController
@RequestMapping("/maintenance")
public class DeviceMaintenanceRecordController {
    @Autowired
    private DeviceMaintenanceRecordService deviceMaintenanceRecordService;
    @Autowired
    private DeviceMaintenanceRecordMapper deviceMaintenanceRecordMapper;
    @Autowired
    private UserRedisConstant redisConstant;

    @GetMapping("/deviceMaintenanceDetailQuery")
    //根据设备保养记录编号返回设备保养记录表
    public List<DeviceMaintenanceRecordResponse> getDeviceMaintenanceRecord(Integer maintenanceId) {

        if (ObjectUtil.isNull(maintenanceId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备保养记录Id参数为空");
        }
        List<DeviceMaintenanceRecordResponse> maintenanceRecord = deviceMaintenanceRecordService.getDeviceMaintenanceRecord(maintenanceId);
        return maintenanceRecord;
    }

    //新增一条设备保养记录返回受影响条数，成功返回1，失败返回0
    @PostMapping("/deviceMaintenanceListInsert")
    public boolean insertDeviceMaintenanceRecord(@RequestBody DeviceMaintenanceRecord deviceMaintenanceRecord) {

        Integer deviceId = deviceMaintenanceRecord.getDeviceID();
        Date maintenanceTime = deviceMaintenanceRecord.getMaintenanceTime();
        String maintenanceContent = deviceMaintenanceRecord.getMaintenanceContent();
        if (ObjectUtil.isNull(deviceId) || ObjectUtil.isNull(maintenanceTime) || StringUtils.isBlank(maintenanceContent)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        int number = 0;
        number = deviceMaintenanceRecordMapper.insert(deviceMaintenanceRecord);
        if (ObjectUtil.equal(number, 0)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"保存保养数据失败");
        }
        return true;
    }

    //更新一条设备保养记录返回受影响条数，成功返回1，失败返回0
    @PostMapping("/deviceMaintenanceListUpdate")
    public int updateDeviceMaintenanceRecord(@NotNull DeviceMaintenanceRecord deviceMaintenanceRecord) {

        Integer maintenanceId = deviceMaintenanceRecord.getMaintenanceID();

        if (ObjectUtil.isNull(maintenanceId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备保养记录Id参数为空");
        }

        deviceMaintenanceRecord.setUpdateTime(new Date());
        int number = 0;
        number = deviceMaintenanceRecordMapper.updateById(deviceMaintenanceRecord);
        if (ObjectUtil.equal(number, 0)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return number;
    }

    //根据MaintenanceID删除设备保养记录，成功返回1，失败返回0
    @PostMapping("/deleteDeviceMaintenanceRecordByMaintenanceID")
    public int deleteDeviceMaintenanceRecordByMaintenanceID(int maintenanceID) {
        if (ObjectUtil.isNull(maintenanceID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备保养记录Id参数为空");
        }

        int number = 0;
        number = deviceMaintenanceRecordMapper.deleteDeviceMaintenanceRecordByMaintenanceID(maintenanceID);
        if (ObjectUtil.equal(number, 0)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return number;
    }

    //根据用户编号返回设备保养数据
    @GetMapping("/getDeviceMaintenanceRecordList")
    public List<DeviceMaintenanceRecordList> getDeviceMaintenanceRecordList(@RequestHeader(value = "token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User) userInfo.get(RedisConstant.UserInfo);
        Integer UserID = user.getUserID();

        List<DeviceMaintenanceRecordList> deviceMaintenanceRecordList = deviceMaintenanceRecordService.getDeviceMaintenanceRecordList(UserID);
        return deviceMaintenanceRecordList;
    }

    //对保养设备进行计数
    @AuthCheck
    @GetMapping("/getMaintenanceDeviceNumber")
    public int getMaintenanceDeviceNumber(@RequestHeader(value = "token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User) userInfo.get(RedisConstant.UserInfo);
        Integer UserID = user.getUserID();

        int number = 0;
        number = deviceMaintenanceRecordMapper.getAllMaintenanceDeviceNumber(UserID);
        if (ObjectUtil.equal(number, 0)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return number;
    }

};



