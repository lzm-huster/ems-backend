package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.business.model.entity.Device;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.request.DeviceScrapListInsertReq;
import com.ems.business.model.request.DeviceScrapListUpdateReq;
import com.ems.business.model.response.DeviceScrapDetail;
import com.ems.business.model.response.DeviceScrapListRes;
import com.ems.business.service.ApprovalRecordService;
import com.ems.business.service.DeviceScrapRecordService;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
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
    @Autowired
    private CosService cosService;
    @Autowired
    private ApprovalRecordService approvalRecordService;
    private static final String scrapPrefix = "Scrap";



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


    @GetMapping("/getNumCurrentScrap")
    //获取“当前用户”报废设备记录的数量
    public int num_current_scrap(@RequestHeader(value = "token",required = false) String token){
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
    public DeviceScrapDetail scrapDetail(int scrapID){

        if(!ObjectUtil.isEmpty(scrapID)){
            QueryWrapper<DeviceScrapRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ScrapID", scrapID);
            DeviceScrapRecord deviceScrapRecord = deviceScrapRecordService.getOne(queryWrapper);

            DeviceScrapDetail deviceScrapDetail = new DeviceScrapDetail();
            BeanUtils.copyProperties(deviceScrapRecord,deviceScrapDetail);

            QueryWrapper<Device> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("DeviceID",deviceScrapRecord.getDeviceID());
            Device device =deviceService.getOne(queryWrapper1);
            deviceScrapDetail.setDeviceName(device.getDeviceName());
            deviceScrapDetail.setAssetNumber(device.getAssetNumber());

            Gson gson = new Gson();
            List<String> scrapImages =gson.fromJson(deviceScrapRecord.getScrapImages(),new TypeToken<List<String>>(){}.getType());
            deviceScrapDetail.setScrapImages(scrapImages);

            return deviceScrapDetail;
        }
        else throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
    }

    @Transactional
    @ApiOperation(value = "插入设备信息",notes = "插入",consumes = "multipart/form-data",response = Object.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", paramType="form", value = "文件", dataType="file", collectionFormat="array"),
    })
    @PostMapping("/insertDeviceScrapRecord")
    //插入报废记录
    public boolean insertDeviceScrapRecord( DeviceScrapListInsertReq deviceScrapListreq, @RequestPart(value = "files",required = false) MultipartFile[] files, @RequestHeader(value = "token",required = false) String token){
        Integer deviceID = deviceScrapListreq.getDeviceID();
        String deviceName = deviceScrapListreq.getDeviceName();
        String scrapPerson = deviceScrapListreq.getScrapPerson();
        Date scrapTime = deviceScrapListreq.getScrapTime();
        if (ObjectUtil.isNull(deviceID)|| StringUtils.isBlank(deviceName)||StringUtils.isBlank(scrapPerson)||ObjectUtil.isNull(scrapTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"部分参数为空");
        }
        String pathStr = null;
        if (ObjectUtil.isNotNull(files) && files.length > 0){
            List<String> pathList = cosService.batchUpload(files, scrapPrefix);
            if (ObjectUtil.isNull(pathList)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
            }
            pathStr = JSONUtil.toJsonStr(pathList);
        }


        Device deviceServiceById = deviceService.getById(deviceID);
        if (ObjectUtil.isNull(deviceServiceById)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"查找设备失败");
        }
        Map<Object, Object> redisMapFromToken = redisConstant.getRedisMapFromToken(token);
        List<String> roles = (List<String>) redisMapFromToken.get(RedisConstant.UserRole);
        User user = (User) redisMapFromToken.get(RedisConstant.UserInfo);

        if (roles.get(0).contains("deviceAdmin")){
            deviceServiceById.setDeviceState("报废");
            boolean update = deviceService.updateById(deviceServiceById);
            if (!update){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新设备状态失败");
            }
            DeviceScrapRecord deviceScrapRecord=new DeviceScrapRecord();
            BeanUtils.copyProperties(deviceScrapListreq,deviceScrapRecord);
            deviceScrapRecord.setDeviceState("报废");
            deviceScrapRecord.setScrapImages(pathStr);
            boolean save = deviceScrapRecordService.save(deviceScrapRecord);
            if (!save){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"保存报废信息失败");
            }
        }else {
            DeviceScrapRecord deviceScrapRecord=new DeviceScrapRecord();
            BeanUtils.copyProperties(deviceScrapListreq,deviceScrapRecord);
            deviceScrapRecord.setDeviceState("已报废");
            deviceScrapRecord.setScrapImages(pathStr);
            boolean save = deviceScrapRecordService.save(deviceScrapRecord);
            if (!save){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"保存报废信息失败");
            }
            int num = approvalRecordService.genApprovalRecord(user.getUserID(),deviceScrapRecord.getScrapID(), "Scrap", null);
            if (ObjectUtil.equal(num,0)){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"新建审批信息出错");
            }
        }
        return true;
    }

    @Transactional
    @ApiOperation(value = "更新设备信息",notes = "更新",consumes = "multipart/form-data",response = Object.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", paramType="form", value = "文件", dataType="file", collectionFormat="array"),
    })
    @PostMapping("/updateDeviceScrapRecord")
    //更新报废记录
    public int updateScrapRecord(@NotNull DeviceScrapListUpdateReq deviceScrapListupdatereq,@RequestPart("files") MultipartFile[] files){
          //将request的数据转换为数据表中的格式
//        String path = cosService.uploadFile(deviceScrapListreq.getScrapImages(),"Scarp");
//       deviceScrapRecord.setScrapImages(path);
        DeviceScrapRecord deviceScrapRecord=new DeviceScrapRecord();
        BeanUtils.copyProperties(deviceScrapListupdatereq,deviceScrapRecord);
        List<String> pathList = cosService.batchUpload(files, scrapPrefix);
        if (ObjectUtil.isNull(pathList)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }
        String pathStr = JSONUtil.toJsonStr(pathList);
        deviceScrapRecord.setScrapImages(pathStr);

        if(ObjectUtil.isEmpty(deviceScrapRecord.getScrapID())) throw new BusinessException(ErrorCode.PARAMS_ERROR,"重要数据缺失");
        else {
            //将数据更新进表中

            UpdateWrapper<DeviceScrapRecord> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("ScrapID", deviceScrapRecord.getScrapID());
            boolean state = deviceScrapRecordService.update(deviceScrapRecord, userUpdateWrapper);

            if (state)
            {
               /*     Device device = new Device();
                    device.setDeviceID(deviceScrapListupdatereq.getDeviceID());
                    device.setDeviceState("已报废");
                    deviceService.updateById(device);*/

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
            boolean state = deviceScrapRecordService.removeById(scrapID);
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

