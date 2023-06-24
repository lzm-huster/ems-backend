package com.ems.business.controller;


import cn.hutool.core.util.ObjectUtil;
import com.ems.annotation.AuthCheck;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.PurchaseApplyMapper;
import com.ems.business.mapper.PurchaseApplySheetMapper;
import com.ems.business.model.entity.PurchaseApplySheet;
import com.ems.business.model.response.PurchaseApplySheetList;
import com.ems.business.service.impl.ApprovalRecordServiceImpl;
import com.ems.business.service.impl.PurchaseApplySheetServiceImpl;
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
@RequestMapping("/PurchaseApplySheet")
public class PurchaseApplySheetController {

    @Autowired
    private PurchaseApplySheetServiceImpl purchaseApplySheetService;
    @Autowired
    private PurchaseApplySheetMapper purchaseApplySheetMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PurchaseApplyMapper purchaseApplyMapper;
    @Autowired
    private UserRedisConstant redisConstant;

    @Autowired
    private ApprovalRecordServiceImpl approvalRecordServiceImpl;

    @AuthCheck(mustAuth = {"purchase:list"})
    @GetMapping("/getPurchaseApplySheetList")
    //返回设备采购申请单列表数据
    public List<PurchaseApplySheetList> getPurchaseApplySheetList(@RequestHeader(value = "token",required = false) String token)
    {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

        String RoleName=null;
        RoleName=userMapper.getRoleNameByUserID(UserID);

        List<PurchaseApplySheetList> purchaseApplySheetLists=null;
        if(RoleName.contains("deviceAdmin"))
        {
            purchaseApplySheetLists=purchaseApplySheetService.getAllPurchaseApplySheetList();

        } else {
            purchaseApplySheetLists=purchaseApplySheetService.getPersonPurchaseApplySheetList(UserID);
        }
        return purchaseApplySheetLists;
    }

    @AuthCheck(mustAuth = {"purchase:query"})
    @GetMapping("/getPurchaseApplySheetByID")
    //  根据采购申请单查询申请单详情
    public PurchaseApplySheet getPurchaseApplySheetByID(int PurchaseApplySheetID)
    {
        if (ObjectUtil.isNull(PurchaseApplySheetID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数为空");
        }

        PurchaseApplySheet purchaseApplySheet=null;
        purchaseApplySheet=purchaseApplySheetMapper.getPurchaseApplySheetByID(PurchaseApplySheetID);

        return purchaseApplySheet;
    }

    @AuthCheck(mustAuth = {"purchase:add"})
    @PostMapping("/insertPurchaseApplySheet")
    //插入一条采购申请单数据,返回受影响行数，0表示不成功，1表示成功
    public int insertPurchaseApplySheet(@RequestBody PurchaseApplySheet purchaseApplySheet)
    {

        //提取传入实体部分数据
        Integer approveTutorID = purchaseApplySheet.getApproveTutorID();
        String purchaseApplyDescription = purchaseApplySheet.getPurchaseApplyDescription();
        Integer purchaseApplicantID = purchaseApplySheet.getPurchaseApplicantID();
        //部分数据系统赋值
        purchaseApplySheet.setPurchaseApplyDate(new Date());

        if (ObjectUtil.isNull(purchaseApplicantID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }


        //学生需要导师，教职工不需要
        String RoleName=userMapper.getRoleNameByUserID(purchaseApplicantID);
        if(ObjectUtil.isNull(approveTutorID)&& RoleName.contains("Student"))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学生身份导师数据不能为空");
        }else if(ObjectUtil.isNull(approveTutorID)||approveTutorID==0) {
            purchaseApplySheet.setApproveTutorID(purchaseApplicantID);
        }

        //学生申请提交后状态为”待导师审批“，教职工为”待管理员审批“
        if(RoleName.contains("Student"))
        {
            purchaseApplySheet.setPurchaseApplyState("待导师审批");
        }else{
            purchaseApplySheet.setPurchaseApplyState("待管理员审批");
        }


        int Number=0;
        Number= purchaseApplySheetMapper.insert(purchaseApplySheet);

        if(Number!=0)
        {
            //获取添加的采购申请单ID，添加待审批记录
            int ID=purchaseApplySheetMapper.getLatestPurchaseApplySheetID(purchaseApplicantID);
            approvalRecordServiceImpl.genApprovalRecord(purchaseApplicantID,ID,"Purchase", approveTutorID);
        }


        return Number;

    }

    @AuthCheck(mustAuth = {"purchase:update"})
    @PostMapping("/updatePurchaseApplySheet")
    //更新一条采购申请单数据,返回受影响行数，0表示不成功，1表示成功
    public int updatePurchaseApplySheet(@RequestBody PurchaseApplySheet purchaseApplySheet)
    {
        //判断主键是否为空
        Integer purchaseApplySheetID = purchaseApplySheet.getPurchaseApplySheetID();

        if(ObjectUtil.isNull(purchaseApplySheetID))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键PurchaseApplySheetID为空");
        }

        purchaseApplySheet.setUpdateTime(new Date());

        int Number=0;
        Number=purchaseApplySheetMapper.updateById(purchaseApplySheet);

        return Number;
    }

    @AuthCheck(mustAuth = {"purchase:add"})
    @GetMapping("getLatestPurchaseApplySheetID")
    //在添加记录时获取刚添加记录的DeviceID
    public int getLatestPurchaseApplySheetID(@RequestHeader(value = "token",required = false) String token)
    {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

        int Number=0;
        Number=purchaseApplySheetMapper.getLatestPurchaseApplySheetID(UserID);

        return Number;
    }

    @Transactional
    @AuthCheck(mustAuth = {"purchase:delete"})
    @PostMapping("deletePurchaseApplySheetByPurchaseApplySheetID")
    //根据PurchaseApplyRecordID删除借用申请单表数据，并删除关联的借用申请表数据，成功返回1，失败返回0
    public int deletePurchaseApplySheetByPurchaseApplySheetID(@RequestBody int PurchaseApplySheetID)
    {
        if (ObjectUtil.isNull(PurchaseApplySheetID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数为空");
        }

        //删除借用申请表数据
        purchaseApplyMapper.deletePurchaseApplyByPurchaseApplyID(PurchaseApplySheetID);

        //删除借用申请单表数据
        int Number=0;
        Number= purchaseApplySheetMapper.deletePurchaseApplySheetByPurchaseApplySheetID(PurchaseApplySheetID);
        return Number;

    }

    @AuthCheck(mustAuth = {"purchase:update"})
    @PostMapping("updateStateByApplySheetID")
    //根据PurchaseApplySheetID进行采购申请单设备入库操作，成功返回1，失败返回0
    public int updateStateByApplySheetID(int PurchaseApplySheetID)
    {
        if (ObjectUtil.isNull(PurchaseApplySheetID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数为空");
        }

        int Number=0;
        Number= purchaseApplySheetMapper.updateStateByApplySheetID("已入库",PurchaseApplySheetID);
        return Number;

    }



}