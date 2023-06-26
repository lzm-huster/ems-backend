package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.BorrowApplySheet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
* @author 吴潮彬
* @description 针对表【BorrowApplySheet(借用申请表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.BorrowApplySheet
*/
@Mapper
public interface BorrowApplySheetMapper extends BaseMapper<BorrowApplySheet> {

    //根据借用申请单表的BorrowApplyID查询借用单表数据详情
    @Select("select * from `BorrowApplySheet` where `BorrowApplyID` = #{BorrowApplyID};")
    List<BorrowApplySheet> getBorrowApplySheetByBorrowApplyID(int BorrowApplyID);

    //根据借用申请单表的BorrowApplyID删除借用单表数据
    @Update("update `BorrowApplySheet` set `IsDeleted`=1 where `BorrowApplyID`=#{BorrowApplyID};")
    public Integer deleteBorrowApplySheetByBorrowApplyID(int BorrowApplyID);


    //归还设备：根据传入的BorrowApplyID修改设备的状态
    @Update("update `Device` set `DeviceState`=#{DeviceState}\n" +
            " where `DeviceID` in (select DeviceID from BorrowApplySheet where BorrowApplyID=#{BorrowApplyID});")
    public Integer updateDeviceStateByBorrowApplyID(String DeviceState,int BorrowApplyID);

    //归还设备：根据传入的BorrowApplyID修改实际归还时间
    @Update("update BorrowApplySheet as b join Device as d on " +
            "b.DeviceID = d.DeviceID set ActualReturnTime =now()" +
            " where b.BorrowApplyID =#{BorrowApplyID};")
    public Integer updateActualReturnTimeByBorrowApplyID(int BorrowApplyID);

}




