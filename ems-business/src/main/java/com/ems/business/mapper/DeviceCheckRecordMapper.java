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
    @Select("select p.CheckID,p.DeviceID,q.DeviceName,q.UserID,p.CheckTime,p.DeviceState,q.AssetNumber "+
            "from Device q "+
            "inner join DeviceCheckRecord p on q.DeviceID = p.DeviceID "+
            "where q.UserID= #{userID} and q.IsDeleted = 0 and p.IsDeleted = 0")
    List<DeviceCheckListRes> getCheckList(int userID);    //普通用户：获取当前用户设备核查列表

    @Select("select p.CheckID,p.DeviceID,q.DeviceName,q.UserID,p.CheckTime,p.DeviceState,q.AssetNumber "+
            "from Device q "+
            "inner join DeviceCheckRecord p on q.DeviceID = p.DeviceID " +
            "where q.IsDeleted = 0 and p.IsDeleted = 0")
    List<DeviceCheckListRes> getCheckListAll();  //管理员：获取所有设备的核查列表

    @Select("select count(*) "+
            "from Device q "+
            "inner join DeviceCheckRecord p on q.DeviceID = p.DeviceID "+
            "where p.DeviceState like '%待核查%' and q.IsDeleted = 0 and p.IsDeleted = 0")
    int getCheckList_CheckingNum_All();   //管理员：获取所有待核查的设备数量

    @Select("select count(*) "+
            "from Device q "+
            "inner join DeviceCheckRecord p on q.DeviceID = p.DeviceID "+
            "where p.DeviceState like '%待核查%' and q.UserID = #{userID} and q.IsDeleted = 0 and p.IsDeleted = 0 ")
    int getCheckList_CheckingNum(int userID);  //普通用户：获取当前用户待核查的设备数量

}




