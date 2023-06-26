package com.ems.business.controller;


import cn.hutool.core.util.ObjectUtil;
import com.ems.annotation.AuthCheck;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.BorrowApplyRecordMapper;
import com.ems.business.mapper.BorrowApplySheetMapper;
import com.ems.business.model.entity.BorrowApplyRecord;
import com.ems.business.model.response.BorrowApplyRecordList;
import com.ems.business.service.BorrowApplyRecordService;
import com.ems.business.service.impl.ApprovalRecordServiceImpl;
import com.ems.business.service.impl.BorrowApplyRecordServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.mapper.UserMapper;
import com.ems.usercenter.model.entity.User;
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
    private BorrowApplyRecordService borrowApplyRecordService;
    @Autowired
    private UserRedisConstant redisConstant;

    @Autowired
    private ApprovalRecordServiceImpl approvalRecordServiceImpl;

    @AuthCheck(mustAuth = {"borrow:list"})
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

    @AuthCheck(mustAuth = {"borrow:query"})
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

    @AuthCheck(mustAuth = {"borrow:query"})
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

    @AuthCheck(mustAuth = {"borrow:query"})
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

    @Transactional
    @AuthCheck(mustAuth = {"borrow:add"})
    @PostMapping("/insertBorrowApplyRecord")
    //插入一条设备借用申请单数据，成功返回1，失败返回0
    public int insertBorrowApplyRecord(BorrowApplyRecord borrowApplyRecord)
    {


        //提取传入实体数据
        Integer approveTutorID = borrowApplyRecord.getApproveTutorID();
        String applyDescription = borrowApplyRecord.getApplyDescription();
        Integer borrowerID = borrowApplyRecord.getBorrowerID();


        //部分数据系统赋值
        borrowApplyRecord.setBorrowApplyDate(new Date());
        borrowApplyRecord.setBorrowApplyState("未审批");

        if ( ObjectUtil.isNull(borrowerID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }

        
        //学生需要导师，教职工不需要
        String RoleName=userMapper.getRoleNameByUserID(borrowerID);
        if(ObjectUtil.isNull(approveTutorID)&& RoleName.contains("Student"))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学生身份导师数据不能为空");
        }else if(ObjectUtil.isNull(approveTutorID)||approveTutorID==0)  {
            borrowApplyRecord.setApproveTutorID(borrowerID);
        }

        //学生申请提交后状态为”待导师审批“，教职工为”待管理员审批“
        if(RoleName.contains("Student"))
        {
            borrowApplyRecord.setBorrowApplyState("待导师审批");
        }else{
            borrowApplyRecord.setBorrowApplyState("待管理员审批");
        }

        int Number=0;
        Number= borrowApplyRecordMapper.insert(borrowApplyRecord);

        if(Number!=0)
        {
            //获取添加的借用申请单ID，添加待审批记录
            int ID=borrowApplyRecordMapper.getLatestBorrowApplyID(borrowerID);
            approvalRecordServiceImpl.genApprovalRecord(borrowerID,ID,"Borrow", approveTutorID);
        }else {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"添加信息失败");
        }

        return Number;

    }

    @AuthCheck(mustAuth = {"borrow:update"})
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

    @AuthCheck(mustAuth = {"borrow:add"})
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
    @AuthCheck(mustAuth = {"borrow:delete"})
    @PostMapping("deleteBorrowApplyRecordByBorrowApplyID")
    //根据BorrowApplyRecordID删除借用申请单表数据，并删除关联的借用申请表数据，成功返回1，失败返回0
    public int deleteBorrowApplyRecordByBorrowApplyRecordID(int BorrowApplyID)
    {
        if (ObjectUtil.isNull(BorrowApplyID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数为空");
        }

        BorrowApplyRecord borrowApplyRecord=null;
        borrowApplyRecord=borrowApplyRecordMapper.getBorrowApplyRecordByBorrowApplyID(BorrowApplyID);
        String borrowApplyState = borrowApplyRecord.getBorrowApplyState();

        if(borrowApplyState=="借用中"){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "请先归还设备再删除借用记录");
        }

        //删除借用申请表数据
        borrowApplySheetMapper.deleteBorrowApplySheetByBorrowApplyID(BorrowApplyID);

        //删除借用申请单表数据
        int Number=0;
        Number=borrowApplyRecordMapper.deleteBorrowApplyRecordByBorrowApplyID(BorrowApplyID);

        return Number;


    }

    @Transactional
    @AuthCheck(mustAuth = {"borrow:update"})
    @PostMapping("updateBorrowRecordAndDeviceState")
    //借用设备：根据传入的BorrowApplyID修改设备的状态,操作成功返回1，失败返回0
    public int updateBorrowRecordAndDeviceState(int BorrowApplyID)
    {
        if (ObjectUtil.isNull(BorrowApplyID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入BorrowApplyID为空");
        }

        //将目标借用申请单状态更改为已完成
        int action1 =0;
        action1=borrowApplyRecordMapper.updateBorrowApplyStateByBorrowApplyID("借用中",BorrowApplyID);

        //将目标借用申请单涉及设备的状态更改为外借
        int action2=0;
        action2=borrowApplySheetMapper.updateDeviceStateByBorrowApplyID("外借",BorrowApplyID);

        //统计借用设备的费用
        int action3=0;

        if(action1>0 && action2>=0)
        {
            return 1;
        }else {
            return 0;
        }
    }


    @Transactional
    @AuthCheck(mustAuth = {"borrow:update"})
    @PostMapping("updateReturnRecordAndDeviceState")
    //归还设备：根据传入的BorrowApplyID修改设备的状态,操作成功返回1，失败返回0
    public int updateReturnRecordAndDeviceState(int BorrowApplyID)
    {
        if (ObjectUtil.isNull(BorrowApplyID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入BorrowApplyID为空");
        }

        //将目标借用申请单状态更改为已完成
        int action1 =0;
        action1=borrowApplyRecordMapper.updateBorrowApplyStateByBorrowApplyID("已归还",BorrowApplyID);
        //将目标借用申请单涉及设备的状态更改为正常
        int action2=0;
        action2=borrowApplySheetMapper.updateDeviceStateByBorrowApplyID("正常",BorrowApplyID);
        //写入目标借用申请单涉及设备的归还时间
        int action3=0;
        action3=borrowApplySheetMapper.updateActualReturnTimeByBorrowApplyID(BorrowApplyID);
        //写入目标借用申请单涉及设备的借用费用
        int action4=0;
        action4=borrowApplyRecordMapper.updateBorrowFeeByBorrowApplyID(BorrowApplyID);

        if(action1>0 && action2>=0&&action3>=0&&action4>=0)
        {
            return 1;
        }else {
            return 0;
        }
    }


}