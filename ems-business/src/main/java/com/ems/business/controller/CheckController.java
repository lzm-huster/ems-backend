package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.DeviceCheckRecordMapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.entity.DeviceCheckRecord;
import com.ems.business.model.entity.DeviceRepairRecord;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.request.DeviceCheckListreq;
import com.ems.business.model.request.DeviceScrapListreq;
import com.ems.business.model.response.DeviceCheckDetail;
import com.ems.business.model.response.DeviceCheckListRes;
import com.ems.business.service.DeviceCheckRecordService;
import com.ems.business.service.DeviceService;
import com.ems.common.ErrorCode;
import com.ems.cos.service.CosService;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.entity.UserRole;
import com.ems.usercenter.service.RoleService;
import com.ems.usercenter.service.UserRoleService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
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
    private DeviceCheckRecordService deviceCheckRecordService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserRedisConstant redisConstant;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeviceCheckRecordMapper deviceCheckRecordMapper;
    @Autowired
    private CosService cosService;



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

    @GetMapping("/getCheckList")
    //查询获得设备核查的记录列表
    public List<DeviceCheckListRes> get_check_list(@RequestHeader(value = "token",required = false) String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userID =user.getUserID();
        List<DeviceCheckListRes> DeviceCheckList;
        if(!ObjectUtil.isEmpty(userID)){
            if(getRoleName(userID).equals("deviceAdmin"))
                DeviceCheckList=deviceCheckRecordService.getCheckListAll();
            else DeviceCheckList=deviceCheckRecordService.getCheckList(userID);
            return DeviceCheckList;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @GetMapping("/getNumChecking")
    //获取“当前用户”按计划，待核查设备的数量
    public int num_checking(@RequestHeader(value = "token",required = false) String token){

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userID =user.getUserID();

        int num_checking ;

        if(!ObjectUtil.isEmpty(userID)){

            if(getRoleName(userID).equals("deviceAdmin"))
                num_checking = deviceCheckRecordService.getCheckList_CheckingNum_All();  //管理员查询
            else num_checking = deviceCheckRecordService.getCheckList_CheckingNum(userID); //普通用户查询
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");

        return num_checking;
    }

    @GetMapping("/getNumChecked")
    //获取“当前用户”按计划，已核查设备的数量
    public int num_checked(@RequestHeader(value = "token",required = false) String token){
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userID =user.getUserID();

        int num_checking ;
        int num_checked ;

        List<DeviceCheckListRes> DeviceCheckList;

        if(!ObjectUtil.isEmpty(userID)){

            if(getRoleName(userID).equals("deviceAdmin"))
                num_checking = deviceCheckRecordService.getCheckList_CheckingNum_All(); //管理员查询
            else num_checking = deviceCheckRecordService.getCheckList_CheckingNum(userID); //普通用户查询

            if(getRoleName(userID).equals("deviceAdmin"))
                DeviceCheckList=deviceCheckRecordService.getCheckListAll();  //管理员查询
            else DeviceCheckList=deviceCheckRecordService.getCheckList(userID); //普通用户查询
            num_checked = DeviceCheckList.size()-num_checking;
            return num_checked;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @GetMapping("/getCheckDetail")
    public DeviceCheckDetail checkDetail(int checkID){
        if(!ObjectUtil.isEmpty(checkID)){
            QueryWrapper<DeviceCheckRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("CheckID", checkID);
            DeviceCheckRecord deviceCheckRecord = deviceCheckRecordService.getOne(queryWrapper);

            QueryWrapper<Device> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("DeviceID",deviceCheckRecord.getDeviceID());

            DeviceCheckDetail deviceCheckDetail=new DeviceCheckDetail();
            BeanUtils.copyProperties(deviceCheckRecord,deviceCheckDetail);

            Device device=deviceService.getOne(queryWrapper1);
            deviceCheckDetail.setDeviceName(device.getDeviceName());
            deviceCheckDetail.setAssetNumber(device.getAssetNumber());

            return deviceCheckDetail;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @PostMapping("/insertDeviceCheckRecord")
    //插入报废记录
    public int insertDeviceCheckRecord(@NotNull DeviceCheckListreq deviceCheckListreq){
        //将request的数据转换为数据表中的格式
        String path = cosService.uploadFile(deviceCheckListreq.getCheckImages(),"Check");
        DeviceCheckRecord deviceCheckRecord=new DeviceCheckRecord();
        BeanUtils.copyProperties(deviceCheckListreq,deviceCheckRecord);
        deviceCheckRecord.setCheckImages(path);

        if(ObjectUtil.isEmpty(deviceCheckListreq.getCheckID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据插入表中
            boolean state=deviceCheckRecordService.save(deviceCheckRecord);
            if(state)
                return 1;
            else
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"插入数据失败");
        }
    }

    @PostMapping("/updateDeviceCheckRecord")
    //更新报废记录
    public int updateCheckRecord(@NotNull DeviceCheckListreq deviceCheckListreq){
        //将request的数据转换为数据表中的格式
        String path = cosService.uploadFile(deviceCheckListreq.getCheckImages(),"Check");
        DeviceCheckRecord deviceCheckRecord=new DeviceCheckRecord();
        BeanUtils.copyProperties(deviceCheckListreq,deviceCheckRecord);
        deviceCheckRecord.setCheckImages(path);

        if(ObjectUtil.isEmpty(deviceCheckListreq.getCheckID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据更新进表中
            UpdateWrapper<DeviceCheckRecord> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("CheckID", deviceCheckRecord.getCheckID());
            int state = deviceCheckRecordMapper.update(deviceCheckRecord, userUpdateWrapper);

            if (state == 1 )
                return 1;
            else
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新操作失败");
        }
    }



    @PostMapping("/deleteDeviceCheckRecord")
    //删除维修记录
    public int deleteCheckRecord(int checkID){
        if(!ObjectUtil.isEmpty(checkID)) {
            DeviceCheckRecord deviceCheckRecord=new DeviceCheckRecord();
            deviceCheckRecord.setCheckID(checkID);
            deviceCheckRecord.setIsDeleted(1);
            //更改IsDelete属性，删除记录
            UpdateWrapper<DeviceCheckRecord> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("CheckID", checkID);
            boolean state = deviceCheckRecordService.update(deviceCheckRecord, userUpdateWrapper);
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

