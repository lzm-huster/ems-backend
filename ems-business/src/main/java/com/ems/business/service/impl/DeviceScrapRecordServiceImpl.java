package com.ems.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.business.mapper.DeviceScrapRecordMapper;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.response.DeviceScrapListRes;
import com.ems.business.service.DeviceScrapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【DeviceScrapRecord(设备报废表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class DeviceScrapRecordServiceImpl extends ServiceImpl<DeviceScrapRecordMapper, DeviceScrapRecord>
    implements DeviceScrapRecordService {
    @Autowired
    private DeviceScrapRecordMapper deviceScrapRecordMapper;
    @Override
    public List<DeviceScrapListRes> getScrapList(int userID){
        return deviceScrapRecordMapper.getScrapList(userID);
    }
    @Override
    public List<DeviceScrapListRes> getScrapListAll(){
        return deviceScrapRecordMapper.getScrapListAll();
    }

}




