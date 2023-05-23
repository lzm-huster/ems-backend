package com.ems.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.BorrowApplyRecordMapper;
import com.ems.business.mapper.BorrowApplySheetMapper;
import com.ems.business.model.entity.BorrowApplySheet;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@ResponseResult
@RestController
@RequestMapping("/BorrowApplySheet")
public class BorrowApplySheetController {

    @Autowired
    private  BorrowApplyRecordMapper borrowApplyRecordMapper;
    @Autowired
    private  BorrowApplySheetMapper borrowApplySheetMapper;



    @GetMapping("/getBorrowApplySheets")
    //返回查看设备采购申请单对应设备详情数据
    public List<BorrowApplySheet> getBorrowApplySheets(int BorrowApplyID)
    {
        List<BorrowApplySheet> borrowApplySheets=null;
        borrowApplySheets=borrowApplySheetMapper.getBorrowApplySheetByBorrowApplyID(BorrowApplyID);

        return borrowApplySheets;
    }

    @GetMapping("/getBorrowDescription")

    //根据BorrowApplyID查看设备采购申请单详情数据(一条申请单数据)
    public String getBorrowDescription(int BorrowApplyID)
    {
        String description=null;
        description=borrowApplyRecordMapper.getDescriptionByBorrowApplyID(BorrowApplyID);

        return description;
    }

    @PutMapping("/insertBorrowApplySheet")
    //插入一条借用设备信息返回受影响条数，成功返回1，失败返回0
    public int insertBorrowApplySheet(@NotNull BorrowApplySheet borrowApplySheet)
    {
        //提取传入实体信息
        Integer deviceID = borrowApplySheet.getDeviceID();
        String deviceName = borrowApplySheet.getDeviceName();
        String deviceType = borrowApplySheet.getDeviceType();
        String deviceModel = borrowApplySheet.getDeviceModel();
        Date expectedReturnTime = borrowApplySheet.getExpectedReturnTime();

        if (ObjectUtil.isNull(deviceID )|| StringUtils.isBlank(deviceName)||
                StringUtils.isBlank(deviceType)|| ObjectUtil.isNull(deviceModel)
                ||ObjectUtil.isNull(expectedReturnTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
        }
        int Number=0;
        Number=borrowApplySheetMapper.insert(borrowApplySheet);
        return Number;

    }

    @PutMapping("/updateBorrowApplySheet")
    //更新一条借用设备信息返回受影响条数，成功返回1，失败返回0
    public int updateBorrowApplySheet(@NotNull BorrowApplySheet borrowApplySheet)
    {
        //判断主键是否为空
        Integer borrowID = borrowApplySheet.getBorrowID();

        if(ObjectUtil.isNull(borrowID))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入实体主键BorrowID为空");
        }

        borrowApplySheet.setUpdateTime(new Date());

        int Number=0;
        Number=borrowApplySheetMapper.updateById(borrowApplySheet);

        return Number;
    }


}
