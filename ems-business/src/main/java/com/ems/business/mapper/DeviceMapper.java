package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.response.DeviceList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【Device(设备列表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.Device
*/
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    //个人查询：根据UserID查询负责的所有设备
    @Select("select d.DeviceID,d.DeviceName, d.DeviceType,d.DeviceModel,d.DeviceState,u.UserName,d.PurchaseDate\n" +
            "from Device d inner join User u on d.UserID = u.UserID\n" +
            "where d.UserID =#{UserID};")
    List<DeviceList> getPersonDeviceList(int UserID);

    //管理员查询：返回所有所有设备列表
    @Select("select d.DeviceID,d.DeviceName, d.DeviceType,d.DeviceModel,d.DeviceState,u.UserName,d.PurchaseDate\n" +
            "from Device d inner join User u on d.UserID = u.UserID\n;")
    List<DeviceList> getAllDeviceList();

    //普通用户查询：返回所有公用设备列表
    @Select("select d.DeviceID,d.DeviceName, d.DeviceType,d.DeviceModel,d.DeviceState,u.UserName,d.PurchaseDate\n" +
            "from Device d inner join User u on d.UserID = u.UserID\n" +
            "where d.IsPublic=1;")
    List<DeviceList> getPublicDevice();

}




