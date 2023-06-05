package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.entity.DeviceCheckRecord;
import com.ems.business.model.entity.DeviceRepairRecord;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.request.DeviceRepairListreq;
import com.ems.business.model.request.DeviceScrapListreq;
import com.ems.business.service.DeviceService;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.entity.UserRole;
import com.ems.annotation.ResponseResult;
import com.ems.business.model.response.DeviceScrapListRes;
import com.ems.business.service.DeviceScrapRecordService;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.service.RoleService;
import com.ems.usercenter.service.UserRoleService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ResponseResult
@RestController
@RequestMapping("/scrap")
public class ScrapController {

    @Autowired
    private DeviceScrapRecordService deviceScrapRecordService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserRedisConstant redisConstant;
    @Autowired
    private RoleService roleService;


    //获取当前用户的角色名称以判断查询范围
    public String getRoleName(int userID){
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UserID", userID);
        int roleID = userRoleService.getOne(queryWrapper).getRoleID();

        QueryWrapper<Role> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("RoleID", roleID);
        String rolename = roleService.getOne(queryWrapper1).getRoleName();
        return rolename;
    }


    @GetMapping("/getScrapList")
    //查询当前报废记录
    public List<DeviceScrapListRes> getScraplist(@RequestHeader(value = "token",required = false) String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        int userID =user.getUserID();

        List<DeviceScrapListRes> DeviceScrapList;

        if(!ObjectUtil.isEmpty(userID)){
            if(getRoleName(userID).equals("deviceAdmin"))
                DeviceScrapList = deviceScrapRecordService.getScrapListAll();
            else DeviceScrapList = deviceScrapRecordService.getScrapList(userID);
            return DeviceScrapList;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");

    }


    @GetMapping("/getNumCurrentScarp")
    //获取“当前用户”报废设备记录的数量
    public int num_current_scarp(@RequestHeader(value = "token",required = false) String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        int userID =user.getUserID();

        List<DeviceScrapListRes> DeviceScrapList;

        if(!ObjectUtil.isEmpty(userID)){
            if(getRoleName(userID).equals("deviceAdmin"))
                DeviceScrapList = deviceScrapRecordService.getScrapListAll();
            else DeviceScrapList = deviceScrapRecordService.getScrapList(userID);
            return DeviceScrapList.size();
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");

    }

    @GetMapping("/getNumExpectedlyScrap")
    //待报废设备计数
    public int num_expectedly_scrap(@RequestHeader(value = "token",required = false) String token) {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userID =user.getUserID();
        if(!ObjectUtil.isEmpty(userID)) {
            if (!getRoleName(userID).equals("deviceAdmin")) {
                //获取当前时间
                Date date = new Date();
                //SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd");
                date.setTime(System.currentTimeMillis());

                //当前时间 > 记录中ExpectScraptime（预计报废时间）时，认为此设备为待报废设备
                return deviceService.getNumScarpingAll(date);
            } else {
                Date date = new Date();
                date.setTime(System.currentTimeMillis());
                return deviceService.getNumScarping(date, userID);

            }
        } else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");

    }

    @GetMapping("/getScrapDetail")
    public DeviceScrapRecord scrapDetail(int scrapID){

        if(!ObjectUtil.isEmpty(scrapID)){
            QueryWrapper<DeviceScrapRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ScrapID", scrapID);
            DeviceScrapRecord deviceScrapRecord = deviceScrapRecordService.getOne(queryWrapper);

            return deviceScrapRecord;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @PostMapping("/insertDeviceScarpRecord")
    //插入报废记录
    public int insertDeviceScarpRecord(@NotNull DeviceScrapListreq deviceScrapListreq){
        //将request的数据转换为数据表中的格式
        DeviceScrapRecord deviceScrapRecord=new DeviceScrapRecord();
        BeanUtils.copyProperties(deviceScrapListreq,deviceScrapRecord);
        deviceScrapRecord.setDeviceState("已报废");

        if(ObjectUtil.isEmpty(deviceScrapListreq.getScrapID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据插入表中
            boolean state=deviceScrapRecordService.save(deviceScrapRecord);
            if(state)
            {
                Device device=new Device();
                device.setDeviceID(deviceScrapListreq.getDeviceID());
                device.setDeviceState("已报废");
                deviceService.updateById(device);

                return 1;
            }
            else
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"插入数据失败");
        }
    }

    @PostMapping("/updateDeviceScarpRecord")
    //更新报废记录
    public int updateScarpRecord(@NotNull DeviceScrapListreq deviceScrapListreq){
        //将request的数据转换为数据表中的格式
        DeviceScrapRecord deviceScrapRecord=new DeviceScrapRecord();
        BeanUtils.copyProperties(deviceScrapListreq,deviceScrapRecord);

        if(ObjectUtil.isEmpty(deviceScrapRecord.getScrapID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据更新进表中

            UpdateWrapper<DeviceScrapRecord> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("ScrapID", deviceScrapRecord.getScrapID());
            boolean state = deviceScrapRecordService.update(deviceScrapRecord, userUpdateWrapper);

            if (state)
            {
                Device device=new Device();
                device.setDeviceID(deviceScrapListreq.getDeviceID());
                device.setDeviceState("已报废");
                deviceService.updateById(device);

                return 1;
            }
            else
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新操作失败");
        }
    }

    @PostMapping("/deleteDeviceScrapRecord")
    //删除维修记录
    public int deleteScrapRecord(int scrapID){
        if(!ObjectUtil.isEmpty(scrapID)) {
            DeviceScrapRecord deviceScrapRecord=new DeviceScrapRecord();
            deviceScrapRecord.setScrapID(scrapID);
            deviceScrapRecord.setIsDeleted(1);
            //更改IsDelete属性，删除记录
            UpdateWrapper<DeviceScrapRecord> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("ScrapID", scrapID);
            boolean state = deviceScrapRecordService.update(deviceScrapRecord, userUpdateWrapper);
            if (state)
            {
                return 1;
            }
            else
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"删除操作失败");

        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
    }


}

/* @Test
    public void test()
    {
        Date date=new Date();
        //SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd");
        date.setTime(System.currentTimeMillis());
        System.out.println(date);
    }
*/

