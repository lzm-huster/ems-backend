package com.ems.business.controller;

import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.PurchaseApplyMapper;
import com.ems.business.model.entity.PurchaseApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ResponseResult
@RestController
@RequestMapping("/PurchaseApply")
public class PurchaseApplyController {

    @Autowired
    private PurchaseApplyMapper purchaseApplyMapper;



    @GetMapping("/getPurchaseApplies")
    //返回查看设备采购申请单详情数据
    public List<PurchaseApply> getPurchaseApplies(int PurchaseApplySheetID)
    {
        List<PurchaseApply> purchaseApplies=null;
        purchaseApplies=purchaseApplyMapper.selectByApplySheetId(PurchaseApplySheetID);

        return purchaseApplies;
    }

}
