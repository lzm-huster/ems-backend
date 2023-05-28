package com.ems.business.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.BorrowApplyRecordMapper;
import com.ems.business.model.entity.BorrowApplyRecord;
import com.ems.business.model.response.BorrowApplyRecordList;
import com.ems.business.service.impl.BorrowApplyRecordServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.mapper.UserMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

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
    private UserRedisConstant redisConstant;

    @GetMapping("/getBorrowApplyRecordList")
    //返回设备借用申请单列表数据
    public List<BorrowApplyRecordList> getBorrowApplyRecordList(int UserID)
    {

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

    @GetMapping("/getBorrowDeviceNumber")
    //返回设备借用列表借用中的设备数量
    public int getBorrowDeviceNumber(int UserID)
    {

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

    @GetMapping("/getReturnDeviceNumber")
    //返回设备借用列表已归还设备数量
    public int getReturnDeviceNumber(int UserID)
    {

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
        BorrowApplyRecord borrowApplyRecord=null;
        borrowApplyRecord=borrowApplyRecordMapper.getBorrowApplyRecordByBorrowApplyID(BorrowApplyID);

        return borrowApplyRecord;
    }

    @PutMapping("/insertBorrowApplyRecord")
    //插入一条设备借用申请单数据，成功返回1，失败返回0
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


    @PutMapping("/updateBorrowApplyRecord")
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

    @GetMapping("getLatestBorrowApplyRecordID")
    //在添加记录时获取刚添加记录的DeviceID
    public int getLatestBorrowApplyRecordID(int UserID)
    {
        int Number=0;
        Number=borrowApplyRecordMapper.getLatestBorrowApplyID(UserID);
        return Number;
    }

}