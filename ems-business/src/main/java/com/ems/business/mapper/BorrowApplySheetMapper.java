package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.BorrowApplySheet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author 吴潮彬
* @description 针对表【BorrowApplySheet(借用申请表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.BorrowApplySheet
*/
@Mapper
public interface BorrowApplySheetMapper extends BaseMapper<BorrowApplySheet> {
    //根据借用申请单表的BorrowApplyID查询借用列表的详情
    @Select("select * from `BorrowApplySheet` where `BorrowApplyID` = #{BorrowApplyID};")
    List<BorrowApplySheet> getBorrowApplySheetByBorrowApplyID(int BorrowApplyID);

}




