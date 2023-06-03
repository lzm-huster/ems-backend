package com.ems.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.business.mapper.DeviceCheckRecordMapper;
import com.ems.business.model.entity.DeviceCheckRecord;
import com.ems.business.model.response.DeviceCheckListRes;
import com.ems.business.service.DeviceCheckRecordService;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【DeviceCheckRecord(设备核查表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class DeviceCheckRecordServiceImpl extends ServiceImpl<DeviceCheckRecordMapper, DeviceCheckRecord>
    implements DeviceCheckRecordService {
    @Autowired
    DeviceCheckRecordMapper deviceCheckRecordMapper;
    @Override
    public List<DeviceCheckListRes> getCheckList(int userID)
    {
        if(ObjectUtil.isEmpty(userID)) throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
        else return deviceCheckRecordMapper.getCheckList(userID);
    }
    @Override
    public List<DeviceCheckListRes> getCheckListAll() {
        return deviceCheckRecordMapper.getCheckListAll();
    }
    @Override
    public int getCheckList_CheckingNum_All()
    {
        return deviceCheckRecordMapper.getCheckList_CheckingNum_All();
    };
    @Override
    public int getCheckList_CheckingNum(int userID)
    {
        if(ObjectUtil.isEmpty(userID)) throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
        else return deviceCheckRecordMapper.getCheckList_CheckingNum(userID);
    };


}




