package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.Device;
import com.ems.business.model.response.DeviceList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
* @author 龙志明
* @description 针对表【Device(设备列表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.Device
*/
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    //个人查询：根据UserID查询负责的所有设备
    @Select("select d.DeviceID,d.DeviceName, d.DeviceType,d.DeviceModel,d.DeviceState,u.UserName,d.PurchaseDate " +
            "from Device d inner join User u on d.UserID = u.UserID " +
            "where d.UserID = #{UserID} and d.IsDeleted=0;")
    List<DeviceList> getPersonDeviceList(int UserID);

    //管理员查询：返回所有所有设备列表
    @Select("select d.DeviceID,d.DeviceName, d.DeviceType,d.DeviceModel,d.DeviceState,u.UserName,d.PurchaseDate " +
            "from Device d inner join User u on d.UserID = u.UserID and d.IsDeleted=0;")
    List<DeviceList> getAllDeviceList();

    //普通用户查询：返回所有公用设备列表
    @Select("select d.DeviceID,d.DeviceName, d.DeviceType,d.DeviceModel,d.DeviceState,u.UserName,d.PurchaseDate " +
            "from Device d inner join User u on d.UserID = u.UserID " +
            "where d.IsPublic=1 and d.IsDeleted=0;")
    List<DeviceList> getPublicDevice();

    //根据DeviceID查询详细信息
    @Select("select * from `Device` where `DeviceID` = #{DeviceID};")
    Device getDeviceByDeviceID(int DeviceID);

    //返回用户最新添加的一条数据的主键DeviceID
    @Select("select DeviceID from `Device` where UserID=#{UserID} order by UpdateTime desc limit 1;")
    Integer getLatestDeviceID(int UserID);

    //根据DeviceID删除一条数据
    @Update("update `Device` set `IsDeleted`=1 where `DeviceID`=#{DeviceID};")
    Integer deleteDeviceByDeviceID(int DeviceID);

    //返回ID与资产编号键值对
    @Select("select DeviceID ,AssetNumber from Device WHERE IsDeleted=0;")
    List<Map<Integer,String>> getAllDeviceIDAndAssetNumber();

    @Select("select count(*) "+
            "from Device "+
            "where ExpectedScrapDate < #{date} and UserID = #{userID}")
    int getNumScarping(Date date,int userID);

    @Select("select count(*) "+
            "from Device "+
            "where ExpectedScrapDate < #{date} ")
    int getNumScarpingAll(Date date);

}




