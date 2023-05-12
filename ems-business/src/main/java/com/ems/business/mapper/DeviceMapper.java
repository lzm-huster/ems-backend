package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.Device;
import org.apache.ibatis.annotations.Mapper;


/**
* @author 龙志明
* @description 针对表【Device(设备列表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.Device
*/
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {

}




