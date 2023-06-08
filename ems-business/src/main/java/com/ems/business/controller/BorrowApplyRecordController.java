package com.ems.business.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.AuthCheck;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.BorrowApplyRecordMapper;
import com.ems.business.mapper.BorrowApplySheetMapper;
import com.ems.business.model.entity.BorrowApplyRecord;
import com.ems.business.model.response.BorrowApplyRecordList;
import com.ems.business.service.impl.BorrowApplyRecordServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.mapper.UserMapper;
import com.ems.usercenter.model.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@ResponseResult
@RestController
@RequestMapping("/BorrowApplyRecord")
public class BorrowApplyRecordController {

    @Autowired
    private BorrowApplyRecordServiceImpl borrowApplyRecordServiceImpl;
    @Autowired
    private BorrowApplyRecordMapper borrowApplyRecordMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BorrowApplySheetMapper borrowApplySheetMapper;

    @Autowired
    private UserRedisConstant redisConstant;

    @AuthCheck
    @GetMapping("/getBorrowApplyRecordList")
    //返回设备借用申请单列表数据
    public List<BorrowApplyRecordList> getBorrowApplyRecordList(@RequestHeader(value = "token",required = false) String token)
    {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();


        String RoleName=null;
        RoleName=userMapper.getRoleNameByUserID(UserID);

        List<BorrowApplyRecordList> borrowApplyRecordLists=null;
        if(RoleName.contains("deviceAdmin"))
        {
            borrowApplyRecordLists=borrowApplyRecordServiceImpl.getAllBorrowApplyRecordList();

        } else {
            borrowApplyRecordLists=borrowApplyRecordServiceImpl.getPersonBorrowApplyRecordList(UserID);
        }
        return borrowApplyRecordLists;
    }

    @AuthCheck
    @GetMapping("/getBorrowDeviceNumber")
    //返回设备借用列表借用中的设备数量
    public int getBorrowDeviceNumber(@RequestHeader(value = "token",required = false) String token)
    {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

        String RoleName=null;
        RoleName=userMapper.getRoleNameByUserID(UserID);

        int borrowNumber=0;
        if(RoleName.contains("deviceAdmin"))
        {
            borrowNumber=borrowApplyRecordMapper.getAllBorrowDeviceNumber();

        } else {
            borrowNumber=borrowApplyRecordMapper.getPersonBorrowDeviceNumber(UserID);
        }
        return borrowNumber;
    }

    @AuthCheck
    @GetMapping("/getReturnDeviceNumber")
    //返回设备借用列表已归还设备数量
    public int getReturnDeviceNumber(@RequestHeader(value = "token",required = false) String token)
    {


        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();


        String RoleName=null;
        RoleName=userMapper.getRoleNameByUserID(UserID);

        int returnNumber=0;
        if(RoleName.contains("deviceAdmin"))
        {
            returnNumber=borrowApplyRecordMapper.getAllReturnDeviceNumber();

        } else {
            returnNumber=borrowApplyRecordMapper.getPersonReturnDeviceNumber(UserID);
        }
        return returnNumber;
    }


    @GetMapping("/getBorrowApplyRecord")
    //返回查看设备借用申请单对应设备详情数据
    public BorrowApplyRecord getBorrowApplyRecord(int BorrowApplyID)
    {
        if (ObjectUtil.isNull(BorrowApplyID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数为空");
        }

        BorrowApplyRecord borrowApplyRecord=null;
        borrowApplyRecord=borrowApplyRecordMapper.getBorrowApplyRecordByBorrowApplyID(BorrowApplyID);

        return borrowApplyRecord;
    }

    @PostMapping("/insertBorrowApplyRecord")
    //插入一条设备借用申请单数据，成功返回1，失败返回0UpdateTime
    public int insertBorrowApplyRecord(@NotNull BorrowApplyRecord borrowApplyRecord)
    {

        //提取传入实体数据
        Integer approveTutorID = borrowApplyRecord.getApproveTutorID();
        String applyDescription = borrowApplyRecord.getApplyDescription();
        Integer borrowerID = borrowApplyRecord.getBorrowerID();
        //部分数据系统赋值
        borrowApplyRecord.setBorrowApplyDate(new Date());
        borrowApplyRecord.setBorrowApplyState("申请中");

        if (ObjectUtil.isNull(approveTutorID)|| ObjectUtil.isNull(borrowerID)||StringUtils.isBlank(applyDescription)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        int Number=0;
        Number= borrowApplyRecordMapper.insert(borrowApplyRecord);
        return Number;

    }


    @PostMapping("/updateBorrowApplyRecord")
    //更新一条设备借用申请单数据，成功返回1，失败返回0
    public int updateBorrowApplyRecord(BorrowApplyRecord borrowApplyRecord)
    {
        //判断主键是否为空
        Integer borrowApplyID = borrowApplyRecord.getBorrowApplyID();

        if(ObjectUtil.isNull(borrowApplyID))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键BorrowApplyID为空");
        }

        borrowApplyRecord.setUpdateTime(new Date());

        int Number=0;
        Number=borrowApplyRecordMapper.updateById(borrowApplyRecord);

        return Number;
    }

    @AuthCheck
    @GetMapping("getLatestBorrowApplyRecordID")
    //在添加记录时获取刚添加记录的DeviceID
    public int getLatestBorrowApplyRecordID(@RequestHeader(value = "token",required = false) String token)
    {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();


        int Number=0;
        Number=borrowApplyRecordMapper.getLatestBorrowApplyID(UserID);
        return Number;
    }

    @Transactional
    @PostMapping("deleteBorrowApplyRecordByBorrowApplyID")
    //根据BorrowApplyRecordID删除借用申请单表数据，并删除关联的借用申请表数据，成功返回1，失败返回0
    public int deleteBorrowApplyRecordByBorrowApplyRecordID(int BorrowApplyID)
    {
        if (ObjectUtil.isNull(BorrowApplyID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数为空");
        }

        //删除借用申请表数据
        borrowApplySheetMapper.deleteBorrowApplySheetByBorrowApplyID(BorrowApplyID);

        //删除借用申请单表数据
        int Number=0;
        Number=borrowApplyRecordMapper.deleteBorrowApplyRecordByBorrowApplyID(BorrowApplyID);

        return Number;


    }

}