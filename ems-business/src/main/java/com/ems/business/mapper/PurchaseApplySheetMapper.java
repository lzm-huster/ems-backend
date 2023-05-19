package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.PurchaseApplySheet;
import com.ems.business.model.response.PurchaseApplySheetList;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
* @author 吴潮彬
* @description 针对表【PurchaseApplySheet(采购申请单表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.PurchaseApplySheet
*/
@Mapper
public interface PurchaseApplySheetMapper extends BaseMapper<PurchaseApplySheet> {

    // 个人：根据申请人ID查询所有相关采购申请单数据：申请人ID
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName deviceList, u2.UserName ,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName approveTutorName \n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID \n" +
            "where u2.UserID = #{UserID};")
    public List<PurchaseApplySheetList> getPersonPurchaseApplySheetList(int UserID);


    // 管理员：查询所有相关采购申请单数据
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName, u2.UserName Applicant,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName TutorName\n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID ;\n")
    public List<PurchaseApplySheetList> getAllPurchaseApplySheetList();


    //更新一条采购申请单数据：申请单ID，申请时间，申请描述，申请人导师,申请单状态
    @Update("update PurchaseApplySheet set PurchaseApplyDate =#{PurchaseApplyDate}, PurchaseApplyDescription = #{PurchaseApplyDescription}"+
            " where PurchaseApplySheetID = #{PurchaseApplySheetID};")
    public int updateBorrowApplySheet(int PurchaseApplySheetID,DateTimeLiteralExpression.DateTime PurchaseApplyDate,String PurchaseApplyDescription,int ApproveTutorID,String PurchaseApplyState );

    //更新一条采购申请单的状态：申请单ID，申请单状态
    @Update("update PurchaseApplySheet set PurchaseApplyState = #{PurchaseApplyState} where PurchaseApplySheetID = #{PurchaseApplySheetID};")
    public int updateBorrowApplySheet(int PurchaseApplySheetID,String PurchaseApplyState);

    //删除一条采购申请记录：申请单ID
    @Delete("delete from PurchaseApplySheet where PurchaseApplicantID =#{PurchaseApplicantID};")
    public int deleteBorrowApplySheet(int PurchaseApplySheetID);



}




