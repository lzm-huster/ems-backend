package com.ems.business.controller;


import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.PurchaseApplySheetMapper;
import com.ems.business.model.entity.PurchaseApplySheet;
import com.ems.business.model.response.PurchaseApplySheetList;
import com.ems.business.service.impl.PurchaseApplySheetServiceImpl;
import com.ems.usercenter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/getPurchaseApplySheetList")
    //返回设备采购申请单列表数据
    public List<PurchaseApplySheetList> getPurchaseApplySheetList(int UserID)
    {

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

    @GetMapping("/etPurchaseApplySheetByID")
    //  根据采购申请单查询申请单详情
    public PurchaseApplySheet getPurchaseApplySheetByID(int PurchaseApplySheetID)
    {
        PurchaseApplySheet purchaseApplySheet=null;
        purchaseApplySheet=purchaseApplySheetMapper.getPurchaseApplySheetByID(PurchaseApplySheetID);

        return purchaseApplySheet;
    }

}