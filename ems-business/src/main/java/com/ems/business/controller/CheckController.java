package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ems.annotation.ResponseResult;
import com.ems.business.model.entity.Device;
import com.ems.business.model.entity.DeviceCheckRecord;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.request.DeviceCheckListreq;
import com.ems.business.model.request.DeviceScrapListreq;
import com.ems.business.model.response.DeviceCheckListRes;
import com.ems.business.service.DeviceCheckRecordService;
import com.ems.business.service.DeviceService;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.entity.UserRole;
import com.ems.usercenter.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ResponseResult
@RestController
@RequestMapping("/check")
public class CheckController {
    @Autowired
    DeviceCheckRecordService deviceCheckRecordService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    private List<DeviceCheckListRes> DeviceCheckList;
    private int usertype;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserRedisConstant redisConstant;
    private int num_checking;
    private int num_checked;

    @GetMapping("/get_check_list/")
    public List<DeviceCheckListRes> get_check_list(@RequestHeader("token") String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userID =user.getUserID();

        if(!ObjectUtil.isEmpty(userID)){
            usertype = 0 ;  //默认普通用户账号
            QueryWrapper<UserRole> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("UserID",userID);
            List<UserRole> userRoleslst =userRoleService.list(queryWrapper);
            for(UserRole s: userRoleslst){
                if(s.getRoleID()==1||s.getRoleID()==0) {usertype = 1; break;} //管理员账号
            }
            if(usertype == 0) DeviceCheckList=deviceCheckRecordService.getCheckList(userID);
            else DeviceCheckList=deviceCheckRecordService.getCheckListAll();

        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");

        return DeviceCheckList;
    }

    @GetMapping("/num_checking")
    //获取“当前用户”按计划，待核查设备的数量
    public int num_checking(){
        num_checking = 0;
        for(DeviceCheckListRes s:DeviceCheckList)
            if(s.getDeviceState().equals("待核查")) num_checking += 1;
        return num_checking;
    }

    @GetMapping("/num_checked")
    //获取“当前用户”按计划，待核查设备的数量
    public int num_checked(){
        num_checked = DeviceCheckList.size()-num_checking;
        return num_checked;
    }

    @GetMapping("/checkDetill")
    public Map<String,String> checkDetill(@RequestHeader("token") String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer checkID =user.getUserID();

        if(!ObjectUtil.isEmpty(checkID)){
            QueryWrapper<DeviceCheckRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("CheckID", checkID);
            DeviceCheckRecord deviceCheckRecord = deviceCheckRecordService.getOne(queryWrapper);
            Map<String,String> map=new HashMap<>();
            map.put("ScrapImages",deviceCheckRecord.getCheckImages());
            map.put("Remark",deviceCheckRecord.getRemark());
            return map;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @PostMapping("/insertDeviceCheckRecord")
    //插入报废记录
    public int insertDeviceCheckRecord(DeviceCheckListreq deviceCheckListreq){
        DeviceCheckRecord deviceCheckRecord=new DeviceCheckRecord();

        deviceCheckRecord.setDeviceID(deviceCheckListreq.getDeviceID());
        deviceCheckRecord.setCheckID(deviceCheckListreq.getCheckID());
        deviceCheckRecord.setChecker(deviceCheckListreq.getChecker());
        deviceCheckRecord.setCheckTime(deviceCheckListreq.getCheckTime());
        deviceCheckRecord.setDeviceState(deviceCheckListreq.getDeviceState());
        deviceCheckRecord.setCheckImages(deviceCheckListreq.getCheckImages());
        deviceCheckRecord.setRemark(deviceCheckListreq.getRemark());

        if(ObjectUtil.isEmpty(deviceCheckListreq.getCheckID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据插入表中
            boolean t=deviceCheckRecordService.save(deviceCheckRecord);
            if(t)
                return 1;
            else
                return 0;
        }
    }

    @PostMapping("/updateDeviceCheckRecord")
    //更新报废记录
    public int updateCheckRecord(DeviceCheckListreq deviceCheckListreq){
        DeviceCheckRecord deviceCheckRecord=new DeviceCheckRecord();

        deviceCheckRecord.setDeviceID(deviceCheckListreq.getDeviceID());
        deviceCheckRecord.setCheckID(deviceCheckListreq.getCheckID());
        deviceCheckRecord.setChecker(deviceCheckListreq.getChecker());
        deviceCheckRecord.setCheckTime(deviceCheckListreq.getCheckTime());
        deviceCheckRecord.setDeviceState(deviceCheckListreq.getDeviceState());
        deviceCheckRecord.setCheckImages(deviceCheckListreq.getCheckImages());
        deviceCheckRecord.setRemark(deviceCheckListreq.getRemark());

        if(ObjectUtil.isEmpty(deviceCheckListreq.getCheckID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据更新进表中
            boolean t = deviceCheckRecordService.updateById(deviceCheckRecord);
            if (t)
                return 1;

            else
                return 0;
        }
    }




}
