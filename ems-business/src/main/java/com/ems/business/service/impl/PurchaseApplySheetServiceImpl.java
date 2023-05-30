package com.ems.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.business.mapper.PurchaseApplyMapper;

import com.ems.business.mapper.DeviceMapper;
import com.ems.business.mapper.PurchaseApplySheetMapper;
import com.ems.business.model.entity.PurchaseApplySheet;
import com.ems.business.model.response.PurchaseApplySheetList;
import com.ems.business.service.PurchaseApplySheetService;
import com.ems.usercenter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【PurchaseApplySheet(采购申请单表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Transactional
@Service
public class PurchaseApplySheetServiceImpl extends ServiceImpl<PurchaseApplySheetMapper, PurchaseApplySheet>
    implements PurchaseApplySheetService {

    @Autowired
    private PurchaseApplySheetMapper purchaseApplySheetMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PurchaseApplyMapper purchaseApplyMapper;


    //个人：获取采购申请列表页面需要的数据
    @Override
    public List<PurchaseApplySheetList> getPersonPurchaseApplySheetList(int PurchaseApplicantID) {
        List<PurchaseApplySheetList> purchaseApplySheetLists = null;
        purchaseApplySheetLists = purchaseApplySheetMapper.getPersonPurchaseApplySheetList(PurchaseApplicantID);

        return purchaseApplySheetLists;

    }

    @Override
    public List<PurchaseApplySheet> getPASByState(String state) {
        return null;
    }

    //管理员：获取所有采购申请页面需要数据
    @Override
    public List<PurchaseApplySheetList> getAllPurchaseApplySheetList()
    {
        List<PurchaseApplySheetList> purchaseApplySheetLists = null;
        purchaseApplySheetLists = purchaseApplySheetMapper.getAllPurchaseApplySheetList();

        return purchaseApplySheetLists;
    }




}








