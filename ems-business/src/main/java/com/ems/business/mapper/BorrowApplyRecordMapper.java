package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.BorrowApplyRecord;
import com.ems.business.model.response.BorrowApplyRecordList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author 吴潮彬
* @description 针对表【BorrowApplyRecord(借用申请单表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.BorrowApplyRecord
*/
@Mapper
public interface BorrowApplyRecordMapper extends BaseMapper<BorrowApplyRecord> {

    //个人查询：根据UserID查询返回个人设备借用列表需要数据
    @Select("\n" +
            "select br.BorrowApplyID, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName \n" +
            "from BorrowApplyRecord br\n" +
            "inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on br.BorrowerID = u2.UserID \n" +
            "where u2.UserID =#{UserID};")
    List<BorrowApplyRecordList> getPersonBorrowApplyRecordList(int UserID);

    //管理员查询：返回所有设备借用列表需要数据
    @Select("\n" +
            "select br.BorrowApplyID, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName \n" +
            "from BorrowApplyRecord br\n" +
            "inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on br.BorrowerID = u2.UserID \n;")
    List<BorrowApplyRecordList> getAllBorrowApplyRecordList();

    //个人查询：根据UserID查询返回个人正在借出设备数量
    @Select("select count(*) from `BorrowApplySheet` "+
            "where `BorrowApplyID` in "+
            "(select `BorrowApplyID` from `BorrowApplyRecord` "+
            "where `BorrowerID` = #{UserID} and `BorrowApplyState` like '%借用%');")
    int getPersonBorrowDeviceNumber(int UserID );

    //个人查询：根据UserID查询返回个人已归还设备数量
    @Select("select count(*) from `BorrowApplySheet` "+
            "where `BorrowApplyID` in "+
            "(select `BorrowApplyID` from `BorrowApplyRecord` "+
            "where `BorrowerID` = #{UserID} and `BorrowApplyState` like '%归还%');")
    int getPersonReturnDeviceNumber(int UserID );

    //管理员查询：查询返回所有正在借出设备数量
    @Select("select count(*) from `BorrowApplySheet` "+
            "where `BorrowApplyID` in "+
            "(select `BorrowApplyID` from `BorrowApplyRecord` "+
            "where `BorrowApplyState` like '%借用%');")
    int getAllBorrowDeviceNumber();

    //管理员查询：根查询返回所有已归还设备数量
    @Select("select count(*) from `BorrowApplySheet` "+
            "where `BorrowApplyID` in "+
            "(select `BorrowApplyID` from `BorrowApplyRecord` "+
            "where `BorrowApplyState` like '%归还%');")
    int getAllReturnDeviceNumber();

    //根据借用申请单ID：BorrowApplyID查询借用申请说明
    @Select("select `ApplyDescription` from `BorrowApplyRecord` where `BorrowApplyID` = #{BorrowApplyID};")
    String getDescriptionByBorrowApplyID(int BorrowApplyID);


    //根据借用申请单表的BorrowApplyID查询借用表数据详情
    @Select("select * from `BorrowApplyRecord` where `BorrowApplyID` = #{BorrowApplyID};")
    BorrowApplyRecord getBorrowApplyRecordByBorrowApplyID(int BorrowApplyID);

}




