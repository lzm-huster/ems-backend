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
    @Select("select t.UserName,p.DeviceName,q.ScrapID,q.DeviceID,q.ScrapTime,q.ScrapReason "+
            "from Device p "+
            "inner join DeviceScrapRecord q on p.DeviceID = q.DeviceID "+
            "inner join User t on t.UserID = p.UserID "+
            "where t.UserID=#{userID}")
    List<DeviceScrapListRes> getScrapList(Integer userID);
    @Select("select t.UserName,p.DeviceName,q.ScrapID,q.DeviceID,q.ScrapTime,q.ScrapReason "+
            "from Device p "+
            "inner join DeviceScrapRecord q on p.DeviceID = q.DeviceID "+
            "inner join User t on t.UserID = p.UserID ")
    List<DeviceScrapListRes> getScrapListAll();
}




