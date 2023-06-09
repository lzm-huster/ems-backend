package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.BorrowApplyRecord;
import com.ems.business.model.response.BorrowApplyRecordList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    @Select("select br.BorrowApplyID,d.AssetNumber, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName \n" +
            "  from BorrowApplyRecord br\n" +
            "  inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "  inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "  inner join User u2 on br.BorrowerID = u2.UserID \n" +
            "  inner join Device d on bs.DeviceID = d.DeviceID\n" +
            "  where u2.UserID =#{UserID} and br.IsDeleted=0 ;")
    List<BorrowApplyRecordList> getPersonBorrowApplyRecordList(int UserID);

    //管理员查询：返回所有设备借用列表需要数据
    @Select("select br.BorrowApplyID,d.AssetNumber, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName \n" +
            "  from BorrowApplyRecord br\n" +
            "  inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "  inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "  inner join User u2 on br.BorrowerID = u2.UserID \n" +
            "  inner join Device d on bs.DeviceID = d.DeviceID\n" +
            "  where  br.IsDeleted=0 ;")
    List<BorrowApplyRecordList> getAllBorrowApplyRecordList();

    //个人查询：根据UserID查询返回个人正在借出设备数量
    @Select("select count(*) from `BorrowApplySheet` "+
            "where `BorrowApplyID` in "+
            "(select `BorrowApplyID` from `BorrowApplyRecord` "+
            "where `BorrowerID` = #{UserID} and `BorrowApplyState` like '%借用%') and IsDeleted=0 ;")
    int getPersonBorrowDeviceNumber(int UserID );

    //个人查询：根据UserID查询返回个人已归还设备数量
    @Select("select count(*) from `BorrowApplySheet` "+
            "where `BorrowApplyID` in "+
            "(select `BorrowApplyID` from `BorrowApplyRecord` "+
            "where `BorrowerID` = #{UserID} and `BorrowApplyState` like '%归还%') and IsDeleted=0 ;")
    int getPersonReturnDeviceNumber(int UserID );

    //管理员查询：查询返回所有正在借出设备数量
    @Select("select count(*) from `BorrowApplySheet` "+
            "where `BorrowApplyID` in "+
            "(select `BorrowApplyID` from `BorrowApplyRecord` "+
            "where `BorrowApplyState` like '%借用%') and IsDeleted=0 ;")
    int getAllBorrowDeviceNumber();

    //管理员查询：根查询返回所有已归还设备数量
    @Select("select count(*) from `BorrowApplySheet` "+
            "where `BorrowApplyID` in "+
            "(select `BorrowApplyID` from `BorrowApplyRecord` "+
            "where `BorrowApplyState` like '%归还%') and IsDeleted=0 ;")
    int getAllReturnDeviceNumber();

    //根据借用申请单ID：BorrowApplyID查询借用申请说明
    @Select("select `ApplyDescription` from `BorrowApplyRecord` where `BorrowApplyID` = #{BorrowApplyID};")
    String getDescriptionByBorrowApplyID(int BorrowApplyID);


    //根据借用申请单表的BorrowApplyID查询借用表数据详情
    @Select("select * from `BorrowApplyRecord` where `BorrowApplyID` = #{BorrowApplyID};")
    BorrowApplyRecord getBorrowApplyRecordByBorrowApplyID(int BorrowApplyID);

    //返回最近添加的记录的BorrowApplyID
    @Select("select BorrowApplyID from `BorrowApplyRecord` where BorrowerID=#{BorrowerID} order by UpdateTime desc limit 1 ;")
    public Integer getLatestBorrowApplyID(int BorrowerID);

    //删除传入BorrowApplyID删除借用申请单记录：BorrowApplyID
    @Update("update `BorrowApplyRecord` set `IsDeleted`=1 where `BorrowApplyID`=#{BorrowApplyID};")
    public Integer deleteBorrowApplyRecordByBorrowApplyID(int BorrowApplyID);

    //传入BorrowApplyID，将借用申请记录的状态更改为已归还
    @Update("update BorrowApplyRecord set BorrowApplyState=#{BorrowApplyState} \n" +
            "where BorrowApplyID=#{BorrowApplyID};")
    public Integer updateBorrowApplyStateByBorrowApplyID(String BorrowApplyState, Integer BorrowApplyID);

    //院外人员：传入BorrowApplyID,设备归还时统计设备借用计费
    @Update("update BorrowApplySheet as b join Device as d on b.DeviceID = d.DeviceID " +
            "set b.BorrowFee = datediff(ActualReturnTime,b.CreateTime)*d.BorrowRate*UnitPrice " +
            "where b.BorrowApplyID = #{BorrowApplyID};")
    public Integer updateOutBorrowFeeByBorrowApplyID(Integer BorrowApplyID);
    //院内人员：传入BorrowApplyID,设备归还时统计设备借用计费
    @Update("update BorrowApplySheet set BorrowFee = 0 where BorrowApplyID =#{BorrowApplyID};")
    public Integer updateInBorrowFeeByBorrowApplyID(Integer BorrowApplyID);

    //按照用户类型筛选借用申请单的列表——教师（联表：BorrowApplyRecord、UserRole）
    @Select("select p from BorrowApplyRecord p"+
            "inner join UserRole ur on p.BorrowerID = ur.UserID"+
            "where ur.RoleID = #{rid} and p.ApproveTutorID = #{tid}"+
            ""
    )
    List<BorrowApplyRecord> getTeBARByUType(Integer tid, Integer rid);

    //按照用户类型筛选借用申请单的列表——设备管理员（联表：BorrowApplyRecord、UserRole）
    @Select("select p from BorrowApplyRecord p"+
            "inner join UserRole ur on p.BorrowerID = ur.UserID"+
            "where ur.RoleID = #{rid}"+
            ""
    )
    List<BorrowApplyRecord> getAllBARByUType(Integer rid);

}




