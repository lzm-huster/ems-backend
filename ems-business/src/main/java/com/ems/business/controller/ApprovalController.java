package com.ems.business.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ems.annotation.ResponseResult;
import com.ems.business.model.entity.*;
import com.ems.business.model.response.ApprovalRecordResponse;
import com.ems.business.model.response.BorrowApplyRecordResponse;
import com.ems.business.model.response.DeviceScrapRecordResponse;
import com.ems.business.model.response.PurchaseApplySheetApprovalResponse;
import com.ems.business.service.PurchaseApplyService;
import com.ems.business.service.impl.*;
//import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    public List<PurchaseApplySheetApprovalResponse> purchaseApprovalList(String state, @RequestHeader(value = "token",required = false) String token) {

        int userId = JWT.decode(token).getClaim("UserId").asInt();

        //List<PurchaseApplySheet> selectList = purchaseApplySheetService.getPASByState(state);
        QueryWrapper<PurchaseApplySheet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PurchaseApplyState", state);
        List<PurchaseApplySheet> selectList = purchaseApplySheetService.list(queryWrapper);

        //利用selectList中的PurchaseApplySheetID和ApproveTutorID，返回学生和导师的名字
        //QueryWrapper<PurchaseApplySheet> queryWrapper2 = new QueryWrapper<>();
        List<Integer> ApplicantIdList = null;
        List<Integer> TutorIdList = null;
        List<String> ApplicantNameList = null;
        List<String> TutorNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApplicantNameList.set(i, String.valueOf(purchaseApplySheetService.getById(ApplicantIdList.get(i))));
            TutorNameList.set(i, String.valueOf(purchaseApplySheetService.getById(TutorIdList.get(i))));
        }
        //返回一个对象，其中包含PurchaseApplySheet信息和两个NameList信息
        List<PurchaseApplySheetApprovalResponse> pasarList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            PurchaseApplySheetApprovalResponse pasar = new PurchaseApplySheetApprovalResponse(selectList.get(i), ApplicantNameList.get(i), TutorNameList.get(i));
            pasarList.add(pasar);
        }
        return pasarList;
    }

    @GetMapping("/borrowApprovalList")
    public List<BorrowApplyRecordResponse> borrowApprovalList(String state, @RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        QueryWrapper<BorrowApplyRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BorrowApplyState", state);
        List<BorrowApplyRecord> selectList = borrowApplyRecordService.list(queryWrapper);

        List<Integer> ApplicantIdList = null;
        List<Integer> TutorIdList = null;
        List<String> ApplicantNameList = null;
        List<String> TutorNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApplicantNameList.set(i, String.valueOf(borrowApplyRecordService.getById(ApplicantIdList.get(i))));
            TutorNameList.set(i, String.valueOf(borrowApplyRecordService.getById(TutorIdList.get(i))));
        }
        //返回一个对象，其中包含BorrowApplyRecord信息和两个NameList信息
        List<BorrowApplyRecordResponse> barrList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            BorrowApplyRecordResponse barr = new BorrowApplyRecordResponse(selectList.get(i), ApplicantNameList.get(i), TutorNameList.get(i));
            barrList.add(barr);
        }
        return barrList;
    }

    @GetMapping("/scrapApprovalList")    //报废只能由负责人申请，学生不能申请
    public List<DeviceScrapRecordResponse> scrapApprovalList(String state, @RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        QueryWrapper<DeviceScrapRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ScrapState", state);
        List<DeviceScrapRecord> selectList = deviceScrapRecordService.list(queryWrapper);

        List<Integer> ApplicantIdList = null;
        List<String> ApplicantNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApplicantNameList.set(i, String.valueOf(deviceScrapRecordService.getById(ApplicantIdList.get(i))));
        }
        //返回一个对象，其中包含DeviceScrapRecord信息和一个NameList信息
        List<DeviceScrapRecordResponse> dsrrList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            DeviceScrapRecordResponse dsrr = new DeviceScrapRecordResponse(selectList.get(i), ApplicantNameList.get(i));
            dsrrList.add(dsrr);
        }
        return dsrrList;
    }



    /*
     2、修改审批单的记录，并且修改审批记录。
                       参数：type 申请单类型：purchase、borrow、scrap
                            state 审批状态：“未审批、导师已审批、管理员已审批、申请通过、已入库、驳回”
                            sheetId 申请单id
                            approverId 审批人id
    */

    @PostMapping("/purchaseApprovalRecord")
    public void purchaseApprovalRecord(String state, Integer sheetId, Integer approverId) {
        //修改申请单审批状态
        QueryWrapper<PurchaseApplySheet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PurchaseApplyState", state);
        /* boolean updateOne = purchaseApplySheetService.update(queryWrapper); */
        purchaseApplySheetService.update(queryWrapper);

        //修改审批记录（审批记录在申请单提交的时候就自动生成）：填写ApprovalRecord中的ApprovalDate，使其从空值变为非空
        QueryWrapper<ApprovalRecord> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("ApplySheetID", sheetId).eq("ApproverID", approverId);
        ApprovalRecord approvalRecord = approvalRecordService.getOne(queryWrapper2);
        if (approvalRecord.getApprovalDate() == null) {
            //填入当前时间
            Date date = new Date();
            approvalRecord.setApprovalDate(date);
            approvalRecordService.update(queryWrapper2);
        } else {
            System.out.println("已有审批记录");
        }
    }

    @PostMapping("/borrowApprovalRecord")
    public void borrowApprovalRecord(String state, Integer sheetId, Integer approverId) {
        //修改申请单审批状态
        QueryWrapper<BorrowApplyRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BorrowApplyState", state);
        /* boolean updateOne = purchaseApplySheetService.update(queryWrapper); */
        borrowApplyRecordService.update(queryWrapper);

        //修改审批记录（审批记录在申请单提交的时候就自动生成）：填写ApprovalRecord中的ApprovalDate，使其从空值变为非空
        QueryWrapper<ApprovalRecord> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("ApplySheetID", sheetId).eq("ApproverID", approverId);
        ApprovalRecord approvalRecord = approvalRecordService.getOne(queryWrapper2);
        if (approvalRecord.getApprovalDate() == null) {
            //填入当前时间
            Date date = new Date();
            approvalRecord.setApprovalDate(date);
            approvalRecordService.update(queryWrapper2);
        } else {
            System.out.println("已有审批记录");
        }
    }


    @PostMapping("/scrapApprovalRecord")   //报废只能由负责人申请，学生不能申请
    public void scrapApprovalRecord(String state, Integer sheetId, Integer approverId) {
        //修改申请单审批状态
        QueryWrapper<DeviceScrapRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ScrapState", state);
        /* boolean updateOne = purchaseApplySheetService.update(queryWrapper); */
        deviceScrapRecordService.update(queryWrapper);

        //修改审批记录（审批记录在申请单提交的时候就自动生成）：填写ApprovalRecord中的ApprovalDate，使其从空值变为非空
        QueryWrapper<ApprovalRecord> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("ApplySheetID", sheetId).eq("ApproverID", approverId);
        ApprovalRecord approvalRecord = approvalRecordService.getOne(queryWrapper2);
        if (approvalRecord.getApprovalDate() == null) {
            //填入当前时间
            Date date = new Date();
            approvalRecord.setApprovalDate(date);
            approvalRecordService.update(queryWrapper2);
        } else {
            System.out.println("已有审批记录");
        }

    }

    /*
     3、根据审批人ID，返回他的审批记录列表（已审批、未审批）。
                       参数：state: 审批状态（y、n）
                            id 审批人id
    */
    //所有：
    @GetMapping("/allApprovalRecord")
    public List<ApprovalRecordResponse> allApprovalRecord(@RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        QueryWrapper<ApprovalRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ApproverID", id);
        List<ApprovalRecord> selectList = approvalRecordService.list(queryWrapper);

        List<Integer> ApproverIdList = null;
        List<String> ApproverNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApproverNameList.set(i, String.valueOf(approvalRecordService.getById(ApproverIdList.get(i))));
        }
        //返回一个对象，其中包含DeviceScrapRecord信息和一个NameList信息
        List<ApprovalRecordResponse> dsrrList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            ApprovalRecordResponse arr = new ApprovalRecordResponse(selectList.get(i), ApproverNameList.get(i));
            dsrrList.add(arr);
        }
        return dsrrList;

    }

    //查询已审批/未审批的:
    @GetMapping("/someApprovalRecord")
    public List<ApprovalRecordResponse> allApprovalRecord(String state, @RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        QueryWrapper<ApprovalRecord> queryWrapper = new QueryWrapper<>();
        List<ApprovalRecord> selectList;
        if(state.equals('y')){
            queryWrapper.isNotNull("ApprovalDate").eq("ApproverID", id);
            selectList = approvalRecordService.list(queryWrapper);
        }
        if(state.equals('n')){
            queryWrapper.isNull("ApprovalDate").eq("ApproverID", id);
            selectList = approvalRecordService.list(queryWrapper);
        }else{
            selectList = null;
        }

        List<Integer> ApproverIdList = null;
        List<String> ApproverNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApproverNameList.set(i, String.valueOf(approvalRecordService.getById(ApproverIdList.get(i))));
        }
        //返回一个对象，其中包含DeviceScrapRecord信息和一个NameList信息
        List<ApprovalRecordResponse> dsrrList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            ApprovalRecordResponse arr = new ApprovalRecordResponse(selectList.get(i), ApproverNameList.get(i));
            dsrrList.add(arr);
        }
        return dsrrList;
    }


/*
    4、按照 设备类型/价格区间/用户类型/设备申请时间 进行筛选，返回审批人的审批单。
                    参数：
                         id 审批人id
                         其他因素: 申请时间、价格区间、用户类型
*/

    @GetMapping("/getPSheetByTime")
    public List<PurchaseApplySheetApprovalResponse> getPSheetByTime(Date mindate, Date maxdate, @RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        QueryWrapper<PurchaseApplySheet> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("PurchaseApplyDate", mindate).le("PurchaseApplyDate", mindate);
        List<PurchaseApplySheet> selectList = purchaseApplySheetService.list(queryWrapper);

        List<Integer> ApplicantIdList = null;
        List<Integer> TutorIdList = null;
        List<String> ApplicantNameList = null;
        List<String> TutorNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApplicantNameList.set(i, String.valueOf(purchaseApplySheetService.getById(ApplicantIdList.get(i))));
            TutorNameList.set(i, String.valueOf(purchaseApplySheetService.getById(TutorIdList.get(i))));
        }
        //返回一个对象，其中包含PurchaseApplySheet信息和两个NameList信息
        List<PurchaseApplySheetApprovalResponse> pasarList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            PurchaseApplySheetApprovalResponse pasar = new PurchaseApplySheetApprovalResponse(selectList.get(i), ApplicantNameList.get(i), TutorNameList.get(i));
            pasarList.add(pasar);
        }
        return pasarList;
    }

    @GetMapping("/getBSheetByTime")
    public List<BorrowApplyRecordResponse> getBSheetByTime(Date mindate, Date maxdate, @RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        QueryWrapper<BorrowApplyRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("BorrowApplyDate",mindate).le("BorrowApplyDate", mindate);
        List<BorrowApplyRecord> selectList = borrowApplyRecordService.list(queryWrapper);

        List<Integer> ApplicantIdList = null;
        List<Integer> TutorIdList = null;
        List<String> ApplicantNameList = null;
        List<String> TutorNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApplicantNameList.set(i, String.valueOf(borrowApplyRecordService.getById(ApplicantIdList.get(i))));
            TutorNameList.set(i, String.valueOf(borrowApplyRecordService.getById(TutorIdList.get(i))));
        }
        //返回一个对象，其中包含BorrowApplyRecord信息和两个NameList信息
        List<BorrowApplyRecordResponse> barrList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            BorrowApplyRecordResponse barr = new BorrowApplyRecordResponse(selectList.get(i), ApplicantNameList.get(i), TutorNameList.get(i));
            barrList.add(barr);
        }
        return barrList;
    }

    @GetMapping("/getSSheetByTime")
    public List<DeviceScrapRecordResponse> getSSheetByTime(Date mindate, Date maxdate, @RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        QueryWrapper<DeviceScrapRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("ScrapTime",mindate).le("ScrapTime", mindate);
        List<DeviceScrapRecord> selectList = deviceScrapRecordService.list(queryWrapper);

        List<Integer> ApplicantIdList = null;
        List<String> ApplicantNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApplicantNameList.set(i, String.valueOf(deviceScrapRecordService.getById(ApplicantIdList.get(i))));
        }
        //返回一个对象，其中包含DeviceScrapRecord信息和一个NameList信息
        List<DeviceScrapRecordResponse> dsrrList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            DeviceScrapRecordResponse dsrr = new DeviceScrapRecordResponse(selectList.get(i), ApplicantNameList.get(i));
            dsrrList.add(dsrr);
        }
        return dsrrList;
    }

    /**
     * 按照价格区间筛选采购申请单
     */
    @GetMapping("/getPSheetByPrize")
    public List<PurchaseApply> getPSheetByPrize(double minprize, double maxprize, @RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        QueryWrapper<PurchaseApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("PurchaseBudget", minprize).lt("PurchaseBudget", maxprize);
        /* boolean updateOne = purchaseApplyService.update(queryWrapper); */
        List<PurchaseApply> selectList = purchaseApplyService.list(queryWrapper);
        return selectList;
    }

    /**
     * 按照用户类型筛选借用申请单_导师
     */
    @GetMapping("/getTeBSheetByUserType")
    public List<BorrowApplyRecordResponse> getTeBSheetByUserType(Integer tid, Integer rid){

        List<BorrowApplyRecord> selectList = borrowApplyRecordService.getTeBARByUType(tid,rid);

        List<Integer> ApplicantIdList = null;
        List<Integer> TutorIdList = null;
        List<String> ApplicantNameList = null;
        List<String> TutorNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApplicantNameList.set(i, String.valueOf(borrowApplyRecordService.getById(ApplicantIdList.get(i))));
            TutorNameList.set(i, String.valueOf(borrowApplyRecordService.getById(TutorIdList.get(i))));
        }
        //返回一个对象，其中包含BorrowApplyRecord信息和两个NameList信息
        List<BorrowApplyRecordResponse> barrList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            BorrowApplyRecordResponse barr = new BorrowApplyRecordResponse(selectList.get(i), ApplicantNameList.get(i), TutorNameList.get(i));
            barrList.add(barr);
        }
        return barrList;
    }

    /**
     * 按照用户类型筛选借用申请单_设备管理员
     */
    @GetMapping("/getAllBSheetByUserType")
    public List<BorrowApplyRecordResponse> getAllBSheetByUserType(Integer rid, @RequestHeader(value = "token",required = false) String token) {

        int id = JWT.decode(token).getClaim("UserId").asInt();

        List<BorrowApplyRecord> selectList = borrowApplyRecordService.getAllBARByUType(rid);

        List<Integer> ApplicantIdList = null;
        List<Integer> TutorIdList = null;
        List<String> ApplicantNameList = null;
        List<String> TutorNameList = null;
        for (int i = 0; i < selectList.toArray().length; i++) {
            ApplicantNameList.set(i, String.valueOf(borrowApplyRecordService.getById(ApplicantIdList.get(i))));
            TutorNameList.set(i, String.valueOf(borrowApplyRecordService.getById(TutorIdList.get(i))));
        }
        //返回一个对象，其中包含BorrowApplyRecord信息和两个NameList信息
        List<BorrowApplyRecordResponse> barrList = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            BorrowApplyRecordResponse barr = new BorrowApplyRecordResponse(selectList.get(i), ApplicantNameList.get(i), TutorNameList.get(i));
            barrList.add(barr);
        }
        return barrList;
    }




}
