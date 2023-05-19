package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.PurchaseApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author 吴潮彬
* @description 针对表【PurchaseApply(采购申请表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.PurchaseApply
*/
@Mapper
public interface PurchaseApplyMapper extends BaseMapper<PurchaseApply> {

    @Select("select DeviceName from PurchaseApply where PurchaseApplicantID=#{PurchaseApplicantID};")
    List<String> getDeviceName(int PurchaseApplicantID);

    @Select("select * from PurchaseApply where PurchaseApplySheetID=#{PurchaseApplySheetID}")
    List<PurchaseApply> selectByApplySheetId(int PurchaseApplySheetID);

}





