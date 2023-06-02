package com.ems.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.business.mapper.DeviceRepairRecordMapper;
import com.ems.business.model.entity.DeviceRepairRecord;
import com.ems.business.model.response.DeviceRepairListRes;
import com.ems.business.service.DeviceRepairRecordService;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【DeviceRepairRecord(设备维修记录表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class DeviceRepairRecordServiceImpl extends ServiceImpl<DeviceRepairRecordMapper, DeviceRepairRecord>
    implements DeviceRepairRecordService {
    @Autowired
    private DeviceRepairRecordMapper deviceRepairRecordMapper;

    @Override
    public List<DeviceRepairListRes> getRepairlist(int userID) {
        if(ObjectUtil.isEmpty(userID)) throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重要参数为空");
        else return deviceRepairRecordMapper.getRepairlist(userID);
    }

    @Override
    public List<DeviceRepairListRes> getAllRepairlist()
    {
        return deviceRepairRecordMapper.getAllRepairlist();
    }


}




