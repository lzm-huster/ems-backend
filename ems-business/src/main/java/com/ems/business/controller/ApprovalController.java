package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ems.annotation.ResponseResult;
import com.ems.business.model.entity.*;
import com.ems.business.model.response.*;
import com.ems.business.service.PurchaseApplyService;
import com.ems.business.service.impl.*;
//import jdk.nashorn.internal.parser.Token;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.entity.UserRole;
import com.ems.usercenter.service.UserRoleService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangwy
 * 内容包括：采购申请单审批、借用申请单审批、报废申请单审批
 */

@ResponseResult
@RestController
@RequestMapping("/approval")
public class ApprovalController {

    @Autowired
    private DeviceServiceImpl deviceService;
    @Autowired
    private PurchaseApplySheetServiceImpl purchaseApplySheetService;
    @Autowired
    private BorrowApplyRecordServiceImpl borrowApplyRecordService;
    @Autowired
    private DeviceScrapRecordServiceImpl deviceScrapRecordService;
    @Autowired
    private ApprovalRecordServiceImpl approvalRecordService;
    @Autowired
    private PurchaseApplyServiceImpl purchaseApplyService;

    @Autowired
    private UserRedisConstant redisConstant;
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/test")
    public void test() {
        deviceService.test();
    }

    /*
     1、根据返回需要审批的申请单。
                       参数:
                           state 审批状态：“未审批、导师已审批、管理员已审批、申请通过、已入库、驳回”
                           id 审批人id
    */

    @GetMapping("/purchaseApprovalList")
    public List<PurchaseApplySheetList> purchaseApprovalList(String state, @RequestHeader(value = "token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UserID", userId);
        UserRole userRole = userRoleService.getOne(queryWrapper);
        Integer roleId = userRole.getRoleID();

        //要分老师、设备管理员、院领导
        List<PurchaseApplySheetList> List1 = null;
        if(roleId == 1 ){
            List1 = approvalRecordService.purchaseApprovalListTe(userId, state);
        }else{
            List1 = approvalRecordService.purchaseApprovalList(state);
        }
        return List1;
    }

    @GetMapping("/borrowApprovalList")
    public List<BorrowApplyRecordList> borrowApprovalList(String state, @RequestHeader(value = "token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UserID", userId);
        UserRole userRole = userRoleService.getOne(queryWrapper);
        Integer roleId = userRole.getRoleID();

        //要分老师、设备管理员
        List<BorrowApplyRecordList> List1 = null;
        if(roleId == 1 ){
            List1 = approvalRecordService.borrowApprovalListTe(userId, state);
        }else{
            List1 = approvalRecordService.borrowApprovalList(state);
        }
        return List1;


    }

    @GetMapping("/scrapApprovalList")    //报废只能由负责人申请，学生不能申请
    public List<DeviceScrapList> scrapApprovalList(String state, @RequestHeader(value = "token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();

        //只有设备管理员能审批报废
        List<DeviceScrapList> List1 = null;
        List1 = approvalRecordService.scrapApprovalList(state);
        return List1;
    }



    /*
     2、修改审批单的记录，并且修改审批记录。
                       参数：type 申请单类型：purchase、borrow、scrap
                            state 审批状态：“未审批、导师已审批、管理员已审批、申请通过、已入库、驳回”
                            sheetId 申请单id
                            approverId 审批人id
    */

    @PostMapping("/purchaseApprovalRecord")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void purchaseApprovalRecord(String state, PurchaseApplySheet purchaseApplySheet, @RequestHeader(value = "token",required = false) String token) {
        //获取用户信息
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userId = user.getUserID();

        //判断主键是否为空
        if(ObjectUtil.isNull(purchaseApplySheet.getPurchaseApplySheetID()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键PurchaseApplySheetID为空");
        }
        if(ObjectUtil.isNull(state))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数state为空");
        }

        Integer applyId  = purchaseApplySheet.getPurchaseApplySheetID();

        //修改申请单审批状态
        purchaseApplySheet.setPurchaseApplyState(state);
        purchaseApplySheetService.updateById(purchaseApplySheet);

        //修改审批记录（审批记录在申请单提交的时候就自动生成）：填写ApprovalRecord中的ApprovalDate，使其从空值变为非空
        QueryWrapper<ApprovalRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ApplySheetID", applyId).eq("ApproverID", userId);
        ApprovalRecord approvalRecord = approvalRecordService.getOne(queryWrapper);
        if(approvalRecord != null) {
            if (approvalRecord.getApprovalDate() == null) {
                //填入当前时间
                Date date = new Date();
                approvalRecord.setApprovalDate(date);
                approvalRecordService.updateById(approvalRecord);
            } else {
                System.out.println("已有审批记录");
            }
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到对应的审批记录");
        }

    }

    @PostMapping("/borrowApprovalRecord")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void borrowApprovalRecord(String state, BorrowApplyRecord borrowApplyRecord, @RequestHeader(value = "token",required = false) String token) {
        //获取用户信息
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userId = user.getUserID();

        //判断主键是否为空
        if(ObjectUtil.isNull(borrowApplyRecord.getBorrowApplyID()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键borrowApplyRecordID为空");
        }
        if(ObjectUtil.isNull(state))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数state为空");
        }
        Integer applyId  = borrowApplyRecord.getBorrowApplyID();

        //修改申请单审批状态
        borrowApplyRecord.setBorrowApplyState(state);
        borrowApplyRecordService.updateById(borrowApplyRecord);

        //修改审批记录（审批记录在申请单提交的时候就自动生成）：填写ApprovalRecord中的ApprovalDate，使其从空值变为非空
        QueryWrapper<ApprovalRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ApplySheetID", applyId).eq("ApproverID", userId);
        ApprovalRecord approvalRecord = approvalRecordService.getOne(queryWrapper);
        if(approvalRecord != null) {
            if (approvalRecord.getApprovalDate() == null) {
                //填入当前时间
                Date date = new Date();
                approvalRecord.setApprovalDate(date);
                approvalRecordService.updateById(approvalRecord);
            } else {
                System.out.println("已有审批记录");
            }
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到对应的审批记录");
        }
    }


    @PostMapping("/scrapApprovalRecord")   //报废只能由负责人申请，学生不能申请
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void scrapApprovalRecord(String state, DeviceScrapRecord deviceScrapRecord, @RequestHeader(value = "token",required = false) String token) {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer userId = user.getUserID();

        //判断主键是否为空
        if(ObjectUtil.isNull(deviceScrapRecord.getScrapID()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键ScrapID为空");
        }
        if(ObjectUtil.isNull(state))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数state为空");
        }
        Integer applyId  = deviceScrapRecord.getScrapID();

        //修改申请单审批状态
        deviceScrapRecord.setScrapState(state);
        deviceScrapRecordService.updateById(deviceScrapRecord);

        //修改审批记录（审批记录在申请单提交的时候就自动生成）：填写ApprovalRecord中的ApprovalDate，使其从空值变为非空
        QueryWrapper<ApprovalRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ApplySheetID", applyId).eq("ApproverID", userId);
        ApprovalRecord approvalRecord = approvalRecordService.getOne(queryWrapper);
        if(approvalRecord != null) {
            if (approvalRecord.getApprovalDate() == null) {
                //填入当前时间
                Date date = new Date();
                approvalRecord.setApprovalDate(date);
                approvalRecordService.updateById(approvalRecord);
            } else {
                System.out.println("已有审批记录");
            }
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到对应的审批记录");
        }
    }

    /*
     3、根据审批人ID，返回他的审批记录列表（已审批、未审批）。
                       参数：state: 审批状态（y、n）
                            id 审批人id
    */
    //所有：
    @GetMapping("/allApprovalRecord")
    public List<ApprovalRecordResponse> allApprovalRecord(@RequestHeader(value = "token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();

        List<ApprovalRecordResponse> List1 = null;
        List1 = approvalRecordService.allApprovalRecord(userId);
        return List1;


    }

    //查询已审批/未审批的:
    @GetMapping("/someApprovalRecord")
    public List<ApprovalRecordResponse> allApprovalRecord(String state, @RequestHeader(value = "token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();

        List<ApprovalRecordResponse> List1 = null;
        if(state.equals('y')){
            List1 = approvalRecordService.allApprovalRecordNotNull(userId);
        }
        if(state.equals('n')){
            List1 = approvalRecordService.allApprovalRecordNull(userId);
        }else{
            List1 = null;
        }
        return List1;
    }


/*
    4、按照 设备类型/价格区间/用户类型/设备申请时间 进行筛选，返回审批人的审批单。
                    参数：
                         id 审批人id
                         其他因素: 申请时间、价格区间、用户类型
*/
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @GetMapping("/getPSheetByTime")
    public List<PurchaseApplySheetList> getPSheetByTime(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date mindate, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date maxdate, @RequestHeader(value="token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UserID", userId);
        UserRole userRole = userRoleService.getOne(queryWrapper);
        Integer roleId = userRole.getRoleID();

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String mindate = dateFormat.format(mindate);
//        String maxdate = dateFormat.format(maxdate);

        //要分老师、设备管理员、院领导
        List<PurchaseApplySheetList> List1 = null;
        if(roleId == 1 ){
            List1 = approvalRecordService.getPSheetByTimeTe(mindate, maxdate,userId);
        }else{
            List1 = approvalRecordService.getPSheetByTime(mindate, maxdate);
        }
        return List1;
    }


    @GetMapping("/getBSheetByTime")
    public List<BorrowApplyRecordList> getBSheetByTime(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date mindate, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date maxdate, @RequestHeader(value="token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UserID", userId);
        UserRole userRole = userRoleService.getOne(queryWrapper);
        Integer roleId = userRole.getRoleID();

        //要分老师、设备管理员、院领导


        //要分老师、设备管理员、院领导
        List<BorrowApplyRecordList> List1 = null;
        if(roleId == 1 ){
            List1 = approvalRecordService.getBSheetByTimeTe(mindate, maxdate,userId);
        }else{
            List1 = approvalRecordService.getBSheetByTime(mindate, maxdate);
        }
        return List1;
    }

    @GetMapping("/getSSheetByTime")
    public List<DeviceScrapList> getSSheetByTime(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date mindate, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date maxdate, @RequestHeader(value="token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();

        List<DeviceScrapList> List1 = null;
        List1 = approvalRecordService.getSSheetByTime(mindate, maxdate);
        return List1;
    }

    /**
     * 按照价格区间筛选采购申请单
     */
    @GetMapping("/getPSheetByPrize")
    public List<PurchaseApplySheetList> getPSheetByPrize(double minprize, double maxprize, @RequestHeader(value="token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UserID", userId);
        UserRole userRole = userRoleService.getOne(queryWrapper);
        Integer roleId = userRole.getRoleID();

        //要分老师、设备管理员、院领导
        List<PurchaseApplySheetList> List1 = null;
        if(roleId == 1 ){
            List1 = approvalRecordService.getPSheetByPrizeTe(minprize, maxprize,userId);
        }else{
            List1 = approvalRecordService.getPSheetByPrize(minprize, maxprize);
        }
        return List1;
    }


    /**
     * 按照用户类型筛选借用申请单_设备管理员
     */
    @GetMapping("/getAllBSheetByUserType")
    public List<BorrowApplyRecordList> getAllBSheetByUserType(Integer rid, @RequestHeader(value="token", required = false) String token) {

        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);

        Integer userId = user.getUserID();

        List<BorrowApplyRecordList> List1 = null;
        List1 = approvalRecordService.getAllBSheetByUserType(rid);
        return List1;


    }




}
