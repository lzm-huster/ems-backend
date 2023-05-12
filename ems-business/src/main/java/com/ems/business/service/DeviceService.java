package com.ems.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.business.model.entity.Device;


/**
* @author 龙志明
* @description 针对表【Device(设备列表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface DeviceService extends IService<Device> {

    void test();

}
