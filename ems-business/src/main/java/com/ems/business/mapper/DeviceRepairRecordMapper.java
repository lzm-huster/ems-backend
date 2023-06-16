package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.DeviceRepairRecord;
import com.ems.business.model.response.DeviceRepairListRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
* @author 龙志明
* @description 针对表【DeviceRepairRecord(设备维修记录表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.DeviceRepairRecord
*/
@Mapper
public interface DeviceRepairRecordMapper extends BaseMapper<DeviceRepairRecord> {

    @Select("select p.RepairID,p.DeviceID,p.RepairTime,q.DeviceName,p.RepairContent,p.RepairFee,q.AssetNumber "+
            "from User t "+
            "inner join Device q on t.UserID = q.UserID "+
            "inner join DeviceRepairRecord p on p.DeviceID = q.DeviceID "+
            "where t.UserID = #{userid} and q.IsDeleted = 0 and p.IsDeleted = 0 ")
    List<DeviceRepairListRes> getRepairlist(int userid); //普通用户查询：查询当前用户所有维修记录

    @Select("select p.RepairID,p.DeviceID,p.RepairTime,q.DeviceName,p.RepairContent,p.RepairFee,q.AssetNumber "+
            "from User t "+
            "inner join Device q on t.UserID = q.UserID "+
            "inner join DeviceRepairRecord p on p.DeviceID = q.DeviceID " +
            "where q.IsDeleted = 0 and p.IsDeleted = 0 ")
    List<DeviceRepairListRes> getAllRepairlist();  //管理员查询：查询所有维修记录


}




