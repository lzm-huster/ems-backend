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

    //根据采购申请单PurchaseApplySheetID查询对应的设备详情
    @Select("select * from PurchaseApply where PurchaseApplySheetID=#{PurchaseApplySheetID}")
    List<PurchaseApply> getPurchaseApplyDetailByPurchaseApplySheetID(int PurchaseApplySheetID);

    //根据采购申请单PurchaseApplySheetID删除对应的设备详情
  @Select("update `PurchaseApply` set `IsDeleted`=1 where `PurchaseApplySheetID`=#{PurchaseApplySheetID};")
    Integer deletePurchaseApplyByPurchaseApplyID(int PurchaseApplySheetID);

}



