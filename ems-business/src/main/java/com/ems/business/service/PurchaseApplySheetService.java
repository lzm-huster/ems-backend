package com.ems.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.business.model.entity.PurchaseApplySheet;
import com.ems.business.model.response.PurchaseApplySheetList;

import java.util.List;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【PurchaseApplySheet(采购申请单表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface PurchaseApplySheetService extends IService<PurchaseApplySheet> {

    public List<PurchaseApplySheetList> getPersonPurchaseApplySheetList(int PurchaseApplicantID);
    public List<PurchaseApplySheet> getPASByState(String state);



    public List<PurchaseApplySheetList> getAllPurchaseApplySheetList();
}