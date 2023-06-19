package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.PurchaseApplySheet;
import com.ems.business.model.response.PurchaseApplySheetList;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
            "where u2.UserID = #{UserID} and p.IsDeleted=0;")
    public List<PurchaseApplySheetList> getPersonPurchaseApplySheetList(int UserID);


    // 管理员：查询所有相关采购申请单数据
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName deviceList, u2.UserName ,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName approveTutorName\n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID "+
            "where ps.IsDeleted=0;\n")
    public List<PurchaseApplySheetList> getAllPurchaseApplySheetList();


    //更新一条采购申请单数据：申请单ID，申请时间，申请描述，申请人导师,申请单状态
    @Update("update PurchaseApplySheet set PurchaseApplyDate =#{PurchaseApplyDate}, PurchaseApplyDescription = #{PurchaseApplyDescription}" +
            " where PurchaseApplySheetID = #{PurchaseApplySheetID} ;")
    public Integer updatePurchaseApplySheet(int PurchaseApplySheetID, DateTimeLiteralExpression.DateTime PurchaseApplyDate, String PurchaseApplyDescription, int ApproveTutorID, String PurchaseApplyState);

    //更新一条采购申请单的状态：申请单ID，申请单状态
    @Update("update PurchaseApplySheet set PurchaseApplyState = #{PurchaseApplyState} where PurchaseApplySheetID = #{PurchaseApplySheetID};")
    public Integer updatePurchaseApplySheet(int PurchaseApplySheetID, String PurchaseApplyState);

    //删除一条采购申请记录：申请单ID
    @Delete("update `PurchaseApplySheet` set `IsDeleted`=1 where `PurchaseApplySheetID`=#{PurchaseApplySheetID};")
    public Integer deletePurchaseApplySheetByPurchaseApplySheetID(int PurchaseApplySheetID);

    //  根据采购申请单查询申请单详情
    @Select("select * from `PurchaseApplySheet` where `PurchaseApplySheetID` = #{PurchaseApplySheetID};")
    public PurchaseApplySheet getPurchaseApplySheetByID(int PurchaseApplySheetID);

    //获取最近添加的数据的PurchaseApplySheetID
    @Select("select PurchaseApplySheetID from `PurchaseApplySheet` where PurchaseApplicantID=#{PurchaseApplicantID} order by UpdateTime desc limit 1;")
    public Integer getLatestPurchaseApplySheetID(int PurchaseApplicantID);

//    @Select("select p.PurchaseApplicantID from PurchaseApplySheet where p.PurchaseApplyState='未审批'")
//    List<String> getIdByState(String State);

    @Select("select p.PurchaseApplicantID, p.PurchaseApplyDate, p.PurchaseApplyDescription, p.ApproveTutorID, p.PurchaseApplyState"+
            "from PurchaseApplySheet p "+
            "inner join User u on u.UserID = p.PurchaseApplicantID "+
            ""+
            "where mp.PurchaseApplyState=#{state} and mp.IsDeleted=FALSE ")
    List<PurchaseApplySheet> getInfoByState(String state);

    @Select("select UserName from User u where u.UserID=#{state}")
    List<String> getNameById(int Id);


//    //按照价格区间，筛选采购申请单 PurchaseApply 的列表 （联表：PurchaseApply、Device、PurchaseApplySheet）(没写完)
//    @Select("select p from PurchaseApplySheet p"+
//            ""+
//            "where p.PurchaseBudget<#{maxprize} and p.PurchaseBudget>#{minprize}"+
//            ""
//    )
//    List<PurchaseApply> getPAByPrice(double minprize, double maxprize);



    ////// 单表操作(只需要id的话)不需要在Mapper这里写，直接在Controller里面写就可以了。这里用于多表查询（Mapper-Service-ServiceImpl-Controller）






}



