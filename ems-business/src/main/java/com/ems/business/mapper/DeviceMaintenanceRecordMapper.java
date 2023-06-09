package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.DeviceMaintenanceRecord;
import com.ems.business.model.response.DeviceMaintenanceRecordList;
import com.ems.business.model.response.DeviceMaintenanceRecordResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【DeviceMaintenanceRecord(设备保养表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.DeviceMaintenanceRecord
*/
@Mapper
public interface DeviceMaintenanceRecordMapper extends BaseMapper<DeviceMaintenanceRecord> {
    //根据UserID查询保养设备数量
    @Select("select count(m.MaintenanceID) as MaintenanceCount "+
            "from Device d inner join DeviceMaintenanceRecord m on "+
            "d.DeviceID = m.DeviceID where d.UserID = #{UserID} and m.IsDeleted=0;")
    int getAllMaintenanceDeviceNumber(Integer UserID);

    //根据UserID查询设备保养数据
    @Select("select m.MaintenanceID, d.DeviceID, d.AssetNumber, d.DeviceName, m.MaintenanceTime, m.MaintenanceContent " +
            "from Device d " +
            "inner join DeviceMaintenanceRecord m on d.DeviceID = m.DeviceID " +
            "where d.UserID = #{UserID} and m.IsDeleted=0;")
    List<DeviceMaintenanceRecordList> getDeviceMaintenanceRecordList(Integer UserID);
    //根据MaintenanceID查询设备保养表
    @Select("select d.AssetNumber, m.MaintenanceID, m.DeviceID, m.MaintenanceTime, m.MaintenanceContent, m.remark from Device d inner join DeviceMaintenanceRecord m on d.DeviceID = m.DeviceID where `MaintenanceID` = #{MaintenanceID} and m.IsDeleted=0;")
    List<DeviceMaintenanceRecordResponse> selectById(Integer MaintenanceID);
    //根据MaintenanceID删除设备保养记录，成功返回1，失败返回0
    @Select("update DeviceMaintenanceRecord set IsDeleted = 1 " +
            "where MaintenanceID = #{MaintenanceID} and IsDeleted=0;")
    public Integer deleteDeviceMaintenanceRecordByMaintenanceID(int MaintenanceID);
}




