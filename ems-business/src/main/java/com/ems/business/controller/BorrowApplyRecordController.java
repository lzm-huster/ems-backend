package com.ems.business.controller;


import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.BorrowApplyRecordMapper;
import com.ems.business.model.response.BorrowApplyRecordList;
import com.ems.business.service.impl.BorrowApplyRecordServiceImpl;
import com.ems.usercenter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}