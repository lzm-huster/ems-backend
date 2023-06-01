package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.DeviceMaintenanceRecord;
import com.ems.business.model.request.DeviceMaintenanceRecordRequest;
import com.ems.business.model.response.DeviceMaintenanceRecordList;
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
/*    //根据maintenanceid查询设备保养数据
    @Select("select m.MaintenanceID, d.DeviceID, d.DeviceName, m.MaintenanceTime, m.MaintenanceContent, m.Remark from DeviceMaintenanceRecord m join Device d " +
            "from Device d " +
            "inner join DeviceMaintenanceRecord m on d.DeviceID = m.DeviceID" +
            "where m.MaintenanceID=#{maintenanceid}")
    List<DeviceMaintenanceRecordRequest> getDeviceMaintenanceRecordRequest(Integer maintenanceid);
*/
    //根据UserID查询保养中的设备数量
    @Select("select count(m.MaintenanceID) as MaintenanceCount"+
            "from Device d inner join DeviceMaintenanceRecord m on"+
            "d.DeviceID = m.DeviceID where d.UserID = {UserID};")
    int getAllMaintenanceDeviceNumber(Integer UserID);

    //根据UserID查询设备保养数据
    @Select("select m.MaintenanceID, d.DeviceID, d.DeviceName, m.MaintenanceTime, m.MaintenanceContent " +
            "from Device d " +
            "inner join DeviceMaintenanceRecord m on d.DeviceID = m.DeviceID" +
            "where m.MaintenanceID=#{UserID};")
    List<DeviceMaintenanceRecordList> getDeviceMaintenanceRecordList(Integer UserID);
}




