package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.entity.DeviceRepairRecord;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.request.DeviceRepairListreq;
import com.ems.business.model.request.DeviceScrapListreq;
import com.ems.business.service.DeviceService;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.entity.UserRole;
import com.ems.annotation.ResponseResult;
import com.ems.business.model.response.DeviceScrapListRes;
import com.ems.business.service.DeviceScrapRecordService;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.service.UserRoleService;
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
    private List<DeviceScrapListRes> DeviceScrapList;
    @Autowired
    private UserRedisConstant redisConstant;
    private int usertype;


    @GetMapping("/getscraplist")
    //查询当前报废记录
    public List<DeviceScrapListRes> getScraplist(@RequestHeader("token") String token){
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
            if(usertype == 0) DeviceScrapList=deviceScrapRecordService.getScrapList(userID);
            else DeviceScrapList=deviceScrapRecordService.getScrapListAll();

        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");

        return DeviceScrapList;
    }
    @GetMapping("/num_current_scarp")
    //获取“当前用户”报废设备记录的数量
    public int num_current_scarp(){
        return DeviceScrapList.size();
    }

    @GetMapping("/num_expectedly_scrap")
    //待报废设备计数
    public int num_expectedly_scrap(@RequestHeader("token") String token) {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userID =user.getUserID();

        int num;
        if (usertype == 0) {
            int currentUserID = userID;
            num = 0;
            QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("UserID", currentUserID);
            List<Device> deviceList = deviceService.list(queryWrapper);
            Date date = new Date();
            //SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd");
            date.setTime(System.currentTimeMillis());
            for (Device s : deviceList) {
                if (date.compareTo(s.getExpectedScrapDate()) >= 0) num += 1;
            }
            return num;
        } else if (usertype == 1) {
            num = 0;
            QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
            List<Device> deviceList = deviceService.list(queryWrapper);
            Date date = new Date();
            date.setTime(System.currentTimeMillis());
            for (Device s : deviceList) {
                if (date.compareTo(s.getExpectedScrapDate()) >= 0) num += 1;
            }
            return num;
        }
        return 0;
    }

    @GetMapping("/scrapDetill")
    public Map<String,String> scrapDetill(@RequestHeader("token") String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer scrapID =user.getUserID();

        if(!ObjectUtil.isEmpty(scrapID)){
            QueryWrapper<DeviceScrapRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ScrapID", scrapID);
            DeviceScrapRecord deviceScrapRecord = deviceScrapRecordService.getOne(queryWrapper);
            Map<String,String> map=new HashMap<>();
            map.put("ScrapImages",deviceScrapRecord.getScrapImages());
            map.put("Remark",deviceScrapRecord.getRemark());
            return map;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @PostMapping("/insertDeviceScarpRecord")
    //插入报废记录
    public int insertDeviceScarpRecord(DeviceScrapListreq deviceScrapListreq){
        DeviceScrapRecord deviceScrapRecord=new DeviceScrapRecord();

        deviceScrapRecord.setDeviceID(deviceScrapListreq.getDeviceID());
        deviceScrapRecord.setScrapID(deviceScrapListreq.getScrapID());
        deviceScrapRecord.setScrapImages(deviceScrapListreq.getScrapImages());
        deviceScrapRecord.setScrapTime(deviceScrapListreq.getScrapTime());
        deviceScrapRecord.setScrapReason(deviceScrapListreq.getScrapReason());
        deviceScrapRecord.setScrapPerson(deviceScrapListreq.getScrapPerson());
        deviceScrapRecord.setRemark(deviceScrapListreq.getRemark());

        if(ObjectUtil.isEmpty(deviceScrapListreq.getScrapID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据插入表中
            boolean t=deviceScrapRecordService.save(deviceScrapRecord);

            if(t)
            {
                Device device=new Device();
                device.setDeviceID(deviceScrapListreq.getDeviceID());
                device.setDeviceState("已报废");
                deviceService.updateById(device);

                DeviceScrapRecord deviceScrapRecord1=new DeviceScrapRecord();
                deviceScrapRecord1.setDeviceID(deviceScrapListreq.getDeviceID());
                deviceScrapRecord1.setDeviceState("已报废");
                deviceScrapRecordService.updateById(deviceScrapRecord1);
                return 1;
            }
            else
                return 0;
        }
    }

    @PostMapping("/updateDeviceScarpRecord")
    //更新报废记录
    public int updateScarpRecord(DeviceScrapListreq deviceScrapListreq){
        DeviceScrapRecord deviceScrapRecord=new DeviceScrapRecord();

        deviceScrapRecord.setDeviceID(deviceScrapListreq.getDeviceID());
        deviceScrapRecord.setScrapID(deviceScrapListreq.getScrapID());
        deviceScrapRecord.setScrapImages(deviceScrapListreq.getScrapImages());
        deviceScrapRecord.setScrapTime(deviceScrapListreq.getScrapTime());
        deviceScrapRecord.setScrapReason(deviceScrapListreq.getScrapReason());
        deviceScrapRecord.setScrapPerson(deviceScrapListreq.getScrapPerson());
        deviceScrapRecord.setRemark(deviceScrapListreq.getRemark());

        if(ObjectUtil.isEmpty(deviceScrapRecord.getScrapID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据更新进表中
            boolean t = deviceScrapRecordService.updateById(deviceScrapRecord);
            if (t)
            {
                Device device=new Device();
                device.setDeviceID(deviceScrapListreq.getDeviceID());
                device.setDeviceState("已报废");
                deviceService.updateById(device);

                DeviceScrapRecord deviceScrapRecord1=new DeviceScrapRecord();
                deviceScrapRecord1.setDeviceID(deviceScrapListreq.getDeviceID());
                deviceScrapRecord1.setDeviceState("已报废");
                deviceScrapRecordService.updateById(deviceScrapRecord1);

                return 1;
            }
            else
                return 0;
        }
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
}*/
