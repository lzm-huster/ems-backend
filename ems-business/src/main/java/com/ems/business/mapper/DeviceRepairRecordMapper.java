package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.DeviceRepairRecord;
import com.ems.business.model.request.DeviceRepairListreq;
import com.ems.business.model.response.DeviceRepairListRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
/**
* @author 龙志明
* @description 针对表【DeviceRepairRecord(设备维修记录表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.DeviceRepairRecord
*/
@Mapper
public interface DeviceRepairRecordMapper extends BaseMapper<DeviceRepairRecord> {

    @Select("select p.RepairID,p.DeviceID,p.RepairTime,q.DeviceName,p.RepairContent,p.RepairFee "+
            "from User t "+
            "inner join Device q on t.UserID = q.UserID "+
            "inner join DeviceRepairRecord p on p.DeviceID = q.DeviceID "+
            "where t.UserID = #{userid}")
    List<DeviceRepairListRes> getRepairlist(int userid);

    @Select("select p.RepairID,p.DeviceID,p.RepairTime,q.DeviceName,p.RepairContent,p.RepairFee "+
            "from User t "+
            "inner join Device q on t.UserID = q.UserID "+
            "inner join DeviceRepairRecord p on p.DeviceID = q.DeviceID ")
    List<DeviceRepairListRes> getAllRepairlist();


}




