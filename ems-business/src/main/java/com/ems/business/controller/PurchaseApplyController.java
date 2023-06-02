package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.PurchaseApplyMapper;
import com.ems.business.model.entity.PurchaseApply;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@ResponseResult
@RestController
@RequestMapping("/PurchaseApply")
public class PurchaseApplyController {

    @Autowired
    private PurchaseApplyMapper purchaseApplyMapper;


    @PutMapping("/insertPurchaseApply")
    //插入一条采购设备数据,返回受影响行数，0表示不成功，1表示成功
    public int insertPurchaseApply(PurchaseApply purchaseApply)
    {

        //提取传入实体的数据
        Integer purchaseApplySheetID = purchaseApply.getPurchaseApplySheetID();
        String deviceName = purchaseApply.getDeviceName();
        String deviceType = purchaseApply.getDeviceType();
        String deviceModel = purchaseApply.getDeviceModel();
        Integer deviceQuantity = purchaseApply.getDeviceQuantity();
        BigDecimal purchaseBudget = purchaseApply.getPurchaseBudget();

        if (ObjectUtil.isNull(purchaseApplySheetID)|| StringUtils.isBlank(deviceName)||
                StringUtils.isBlank(deviceType)|| ObjectUtil.isNull(deviceModel)
                ||ObjectUtil.isNull(deviceQuantity)||
                ObjectUtil.isNull(purchaseBudget)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        int Number=0;
        Number=purchaseApplyMapper.insert(purchaseApply);
        return Number;

    }

    @PutMapping("/updatePurchaseApply")
    //更新一条采购设备数据，返回受影响函数，0表示不成功，1表示成功
    public int updatePurchaseApply(PurchaseApply purchaseApply)
    {
        //判断主键是否为空
        Integer purchaseApplyID = purchaseApply.getPurchaseApplyID();

        if(ObjectUtil.isNull(purchaseApplyID))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键PurchaseApplyID为空");
        }

        purchaseApply.setUpdateTime(new Date());

        int Number=0;
        Number=purchaseApplyMapper.updateById(purchaseApply);

        return Number;
    }



}
