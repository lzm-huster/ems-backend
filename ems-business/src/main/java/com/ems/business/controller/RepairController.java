package com.ems.business.controller;
import cn.hutool.core.util.ObjectUtil;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ems.annotation.ResponseResult;
import com.ems.business.model.entity.Device;
import com.ems.business.model.entity.DeviceCheckRecord;
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
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.entity.UserRole;
import com.ems.usercenter.service.RoleService;
import com.ems.usercenter.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
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
    private UserRedisConstant redisConstant;
    @Autowired
    private UserRoleService userRoleService;
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

    @GetMapping("/getRepairList")
    //查询当前设备维修记录
    public List<DeviceRepairListRes> getRepairlist(@RequestHeader(value = "token",required = false) String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        int userID =user.getUserID();

        List<DeviceRepairListRes> DeviceRepairList;

        if(!ObjectUtil.isEmpty(userID))
        {
            if(getRoleName(userID).equals("deviceAdmin"))
                DeviceRepairList=deviceRepairRecordService.getAllRepairlist();
            else DeviceRepairList=deviceRepairRecordService.getRepairlist(userID);
            return DeviceRepairList;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @GetMapping("/getNumCurrentRepair")
    //获取“当前用户”维修记录的数量
    public int num_current_repair(@RequestHeader(value = "token",required = false) String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        int userID =user.getUserID();

        List<DeviceRepairListRes> DeviceRepairList;

        if(!ObjectUtil.isEmpty(userID))
        {
            if(getRoleName(userID).equals("deviceAdmin"))
                DeviceRepairList=deviceRepairRecordService.getAllRepairlist();
            else DeviceRepairList=deviceRepairRecordService.getRepairlist(userID);
            return DeviceRepairList.size();
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @GetMapping("/getNumRepair")
    //获取“所有”正在维修的设备的数量
    public int num_repair(@RequestHeader(value="token" , required = false) String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        int userID =user.getUserID();

        int num_repair;
        //获取正在维修的设备的列表
        if(!ObjectUtil.isEmpty(userID)) {
            if(getRoleName(userID).equals("deviceAdmin"))
            {
                QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("DeviceState", "维修中").eq("UserID", userID);
                List<Device> deviceRepairingList = deviceService.list(queryWrapper);
                //管理员：查询列表长度获取所有正在维修的设备的数量
                num_repair = deviceRepairingList.size();
            }else
            {
                QueryWrapper<Device> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("DeviceState", "维修中");
                List<Device> deviceRepairingList = deviceService.list(queryWrapper1);
                //普通用户：查询列表长度获取当前用户正在维修的设备的数量
                num_repair = deviceRepairingList.size();
            }
            return num_repair;
        } else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @GetMapping("/getRepairDetill")
    //查询当前设备维修具体信息
    public String getRepairDetills(int repairID){
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
    public int insertRepairRecord(@NotNull DeviceRepairListreq deviceRepairListreq){
        //将request的数据转换为数据表中的格式
        DeviceRepairRecord deviceRepairRecord=new DeviceRepairRecord();
        BeanUtils.copyProperties(deviceRepairListreq,deviceRepairRecord);

        if(ObjectUtil.isEmpty(deviceRepairRecord.getRepairID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据插入表中
            boolean state=deviceRepairRecordService.save(deviceRepairRecord);
            if(state) {
                /*Device device = new Device();
                device.setDeviceID(deviceRepairListreq.getDeviceID());
                device.setDeviceState("维修中");
                deviceService.updateById(device);*/
                return 1;
            }
            else
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"插入数据失败");
        }
    }

    @PostMapping("/updateDeviceRepairRecord")
    //更新维修记录
    public int updateRepairRecord(@NotNull DeviceRepairListreq deviceRepairListreq){
        //将request的数据转换为entity中的格式
        DeviceRepairRecord deviceRepairRecord=new DeviceRepairRecord();
        BeanUtils.copyProperties(deviceRepairListreq,deviceRepairRecord);

        if(ObjectUtil.isEmpty(deviceRepairRecord.getRepairID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据更新进表中

            UpdateWrapper<DeviceRepairRecord> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("RepairID", deviceRepairRecord.getRepairID());

            boolean state = deviceRepairRecordService.update(deviceRepairRecord, userUpdateWrapper);

            if (state)
            {
                /*Device device = new Device();
                device.setDeviceID(deviceRepairListreq.getDeviceID());
                device.setDeviceState("维修中");
                deviceService.updateById(device);*/
                return 1;
            }
            else
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新操作失败");
        }
    }

/*    @Test
    public void test()
    {
       Date date=new Date();
        //SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd");
        date.setTime(System.currentTimeMillis());
        System.out.println(date);
        Integer i = 5;
        int j = i;
        System.out.println(j);
    }*/
}
