package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.response.DeviceScrapListRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【DeviceScrapRecord(设备报废表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.DeviceScrapRecord
*/
@Mapper
public interface DeviceScrapRecordMapper extends BaseMapper<DeviceScrapRecord> {
    @Select("select t.UserName,p.DeviceName,q.ScrapID,q.DeviceID,q.ScrapTime,q.ScrapReason,p.AssetNumber "+
            "from Device p "+
            "inner join DeviceScrapRecord q on p.DeviceID = q.DeviceID "+
            "inner join User t on t.UserID = p.UserID "+
            "where t.UserID=#{userID} and p.IsDeleted = 0 and q.IsDeleted = 0 ")
    List<DeviceScrapListRes> getScrapList(Integer userID);    //普通用户查询：查询当前用户所有维修记录
    @Select("select t.UserName,p.DeviceName,q.ScrapID,q.DeviceID,q.ScrapTime,q.ScrapReason,p.AssetNumber "+
            "from Device p "+
            "inner join DeviceScrapRecord q on p.DeviceID = q.DeviceID "+
            "inner join User t on t.UserID = p.UserID " +
            "where p.IsDeleted = 0 and q.IsDeleted = 0")
    List<DeviceScrapListRes> getScrapListAll();   //管理员查询：查询所有维修记录
}




