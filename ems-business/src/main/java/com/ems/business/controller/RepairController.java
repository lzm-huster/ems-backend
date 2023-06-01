package com.ems.business.controller;
import cn.hutool.core.util.ObjectUtil;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ems.annotation.ResponseResult;
import com.ems.business.model.entity.Device;
import com.ems.business.model.entity.DeviceRepairRecord;
import com.ems.business.model.request.DeviceRepairListreq;
import com.ems.business.model.response.DeviceRepairListRes;
import com.ems.business.service.DeviceRepairRecordService;
import com.ems.business.service.DeviceService;
import com.ems.business.service.impl.DeviceRepairRecordServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@ResponseResult
@RestController
@RequestMapping("/repair")
public class RepairController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepairRecordService deviceRepairRecordService;
    @Autowired
    private List<DeviceRepairListRes> DeviceRepairList;
    @Autowired
    private UserRedisConstant redisConstant;
    static Integer num_repair;

    @GetMapping("/num_repair")
    //获取“所有”正在维修的设备的数量
    public int num_repair(){
        //获取正在维修的设备的列表
        QueryWrapper<Device> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("DeviceState","维修");
        List<Device> deviceList =deviceService.list(queryWrapper);
        //查询列表长度获取正在维修的设备的数量
        num_repair=deviceList.size();
        return num_repair;
    }
    @GetMapping("/getRepairlist")
    //查询当前设备维修记录
    public List<DeviceRepairListRes> getRepairlist(@RequestHeader("token") String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userID =user.getUserID();

        if(!ObjectUtil.isEmpty(userID))
            DeviceRepairList=deviceRepairRecordService.getRepairlist(userID);
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");

        return DeviceRepairList;
    }

    @GetMapping("/num_current_repair")
    //获取“当前用户”维修记录的数量
    public int num_current_repair(){
        return DeviceRepairList.size();
    }

    @GetMapping("/getRepairDetill")
    //查询当前设备维修具体信息
    public String getRepairDetill(@RequestHeader("token") String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer repairID =user.getUserID();

        if(!ObjectUtil.isEmpty(repairID)) {
            //获取当前设备记录
            QueryWrapper<DeviceRepairRecord> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("RepairID",repairID);
            DeviceRepairRecord selectOne = deviceRepairRecordService.getOne(queryWrapper);
            //取得备注（Remark）信息
            String S=selectOne.getRemark();
            if(StringUtils.isEmpty(S)) throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "请求数据不存在");
            else return S;
            }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");

    }
    @PostMapping("/insertDeviceRepairRecord")
    //插入维修记录
    public int insertRepairRecord(DeviceRepairListreq deviceRepairListreq){
        //将request的数据转换为entity中的格式
        DeviceRepairRecord deviceRepairRecord=new DeviceRepairRecord();

        deviceRepairRecord.setRepairID(deviceRepairListreq.getRepairID());
        deviceRepairRecord.setRepairContent((deviceRepairListreq.getRepairContent()));
        deviceRepairRecord.setRepairFee(deviceRepairListreq.getRepairFee());
        deviceRepairRecord.setRepairTime((deviceRepairListreq.getRepairTime()));
        deviceRepairRecord.setDeviceID(deviceRepairListreq.getDeviceID());
        deviceRepairRecord.setRemark(deviceRepairListreq.getRemark());

        if(ObjectUtil.isEmpty(deviceRepairRecord.getRepairID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据插入表中
            boolean t=deviceRepairRecordService.save(deviceRepairRecord);
            if(t) {
                Device device = new Device();
                device.setDeviceID(deviceRepairListreq.getDeviceID());
                device.setDeviceState("维修中");
                deviceService.updateById(device);

                return 1;
            }
            else
                return 0;
        }
    }

    @PostMapping("/updateDeviceRepairRecord")
    //更新维修记录
    public int updateRepairRecord(DeviceRepairListreq deviceRepairListreq){
        //将request的数据转换为entity中的格式
        DeviceRepairRecord deviceRepairRecord=new DeviceRepairRecord();

        deviceRepairRecord.setRepairID(deviceRepairListreq.getRepairID());
        deviceRepairRecord.setRepairContent((deviceRepairListreq.getRepairContent()));
        deviceRepairRecord.setRepairFee(deviceRepairListreq.getRepairFee());
        deviceRepairRecord.setRepairTime((deviceRepairListreq.getRepairTime()));
        deviceRepairRecord.setDeviceID(deviceRepairListreq.getDeviceID());
        deviceRepairRecord.setRemark(deviceRepairListreq.getRemark());

        if(ObjectUtil.isEmpty(deviceRepairRecord.getRepairID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据更新进表中
            boolean t = deviceRepairRecordService.updateById(deviceRepairRecord);
            if (t)
            {
                Device device = new Device();
                device.setDeviceID(deviceRepairListreq.getDeviceID());
                device.setDeviceState("维修中");
                deviceService.updateById(device);

                return 1;
            }
            else
                return 0;
        }
    }
}
