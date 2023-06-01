package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.DeviceCheckRecord;
import com.ems.business.model.response.DeviceCheckListRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【DeviceCheckRecord(设备核查表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.DeviceCheckRecord
*/
@Mapper
public interface DeviceCheckRecordMapper extends BaseMapper<DeviceCheckRecord> {
    @Select("select q."+
            "from Device q"+
            "inner join DeviceCheckRecord p on q.DeviceID==p,Device"+
            "where q.UserID= #{userID}")
    List<DeviceCheckListRes> getCheckList(int userID);

    @Select("select q."+
            "from Device q"+
            "inner join DeviceCheckRecord p on q.DeviceID==p,Device")
    List<DeviceCheckListRes> getCheckListAll();

}




