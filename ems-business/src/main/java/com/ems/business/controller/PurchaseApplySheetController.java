package com.ems.business.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.PurchaseApplySheetMapper;
import com.ems.business.model.entity.PurchaseApplySheet;
import com.ems.business.model.response.PurchaseApplySheetList;
import com.ems.business.service.impl.PurchaseApplySheetServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.mapper.UserMapper;
import com.ems.usercenter.model.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserRedisConstant redisConstant;

    @GetMapping("/getPurchaseApplySheetList")
    //返回设备采购申请单列表数据
    public List<PurchaseApplySheetList> getPurchaseApplySheetList(@RequestHeader("token") String token)
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

    @GetMapping("/getPurchaseApplySheetByID")
    //  根据采购申请单查询申请单详情
    public PurchaseApplySheet getPurchaseApplySheetByID(int PurchaseApplySheetID)
    {
        PurchaseApplySheet purchaseApplySheet=null;
        purchaseApplySheet=purchaseApplySheetMapper.getPurchaseApplySheetByID(PurchaseApplySheetID);

        return purchaseApplySheet;
    }

    @PutMapping("/insertPurchaseApplySheet")
    //插入一条采购申请单数据,返回受影响行数，0表示不成功，1表示成功
    public int insertPurchaseApplySheet(@NotNull PurchaseApplySheet purchaseApplySheet)
    {

        //提取传入实体部分数据
        Integer approveTutorID = purchaseApplySheet.getApproveTutorID();
        String purchaseApplyDescription = purchaseApplySheet.getPurchaseApplyDescription();
        Integer purchaseApplicantID = purchaseApplySheet.getPurchaseApplicantID();
        //部分数据系统赋值
        purchaseApplySheet.setPurchaseApplyDate(new Date());

        if (ObjectUtil.isNull(approveTutorID)|| StringUtils.isBlank(purchaseApplyDescription)||ObjectUtil.isNull(purchaseApplicantID)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        int Number=0;
        Number= purchaseApplySheetMapper.insert(purchaseApplySheet);
        return Number;

    }


    @PutMapping("/updatePurchaseApplySheet")
    //更新一条采购申请单数据,返回受影响行数，0表示不成功，1表示成功
    public int updatePurchaseApplySheet(@NotNull PurchaseApplySheet purchaseApplySheet)
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

    @GetMapping("getLatestPurchaseApplySheetID")
    //在添加记录时获取刚添加记录的DeviceID
    public int getLatestPurchaseApplySheetID(@RequestHeader("token") String token)
    {
        Map<Object, Object> userInfo = redisConstant.getRedisMapFromToken(token);
        User user = (User)userInfo.get(RedisConstant.UserInfo);
        Integer UserID =user.getUserID();

        int Number=0;
        Number=purchaseApplySheetMapper.getLatestPurchaseApplySheetID(UserID);

        return Number;
    }


}