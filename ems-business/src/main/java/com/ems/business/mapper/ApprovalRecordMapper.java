package com.ems.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.business.model.entity.ApprovalRecord;
import com.ems.business.model.entity.PurchaseApply;
import com.ems.business.model.entity.DeviceScrapRecord;
import com.ems.business.model.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;


/**
 * @author 龙志明
 * @description 针对表【ApprovalRecord(申请审批记录表)】的数据库操作Mapper
 * @createDate 2023-04-24 09:03:00
 * @Entity generator.domain.ApprovalRecord
 */
@Mapper
public interface ApprovalRecordMapper extends BaseMapper<ApprovalRecord> {

    // 1、返回需要审批的采购申请单：老师   PurchaseApplySheetList2
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName deviceList, u2.UserName,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName approveTutorName, r.RoleName, p.PurchaseBudget purchaseBudget \n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID \n"+
            "inner join UserRole ur on ps.PurchaseApplicantID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where ps.ApproveTutorID=#{id} and ps.PurchaseApplyState = #{state} and ps.IsDeleted = 0;")
    public List<PurchaseApplySheetList2> purchaseApprovalListTe(Integer id, String state);

    // 1、返回需要审批的采购申请单：设备管理员/院领导 （全部）   PurchaseApplySheetList2
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName deviceList, u2.UserName ,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName approveTutorName, r.RoleName, p.PurchaseBudget purchaseBudget \n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID \n"+
            "inner join UserRole ur on ps.PurchaseApplicantID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where ps.PurchaseApplyState = #{state} and ps.IsDeleted = 0;")
    public List<PurchaseApplySheetList2> purchaseApprovalList(String state);


    // 2、返回需要审批的借用申请单：老师   BorrowApplyRecordList2
    @Select("\n" +
            "select br.BorrowApplyID, d.AssetNumber, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName, r.RoleName \n" +
            "from BorrowApplyRecord br\n" +
            "inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on br.BorrowerID = u2.UserID \n" +
            "inner join Device d on bs.DeviceID=d.DeviceID \n"+
            "inner join UserRole ur on br.BorrowerID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where br.ApproveTutorID=#{id} and br.BorrowApplyState = #{state} and br.IsDeleted = 0;")
    public List<BorrowApplyRecordList2> borrowApprovalListTe(Integer id, String state);

    // 2、返回需要审批的借用申请单：设备管理员 （全部）   BorrowApplyRecordList2
    @Select("\n" +
            "select br.BorrowApplyID, d.AssetNumber, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName, r.RoleName \n" +
            "from BorrowApplyRecord br\n" +
            "inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on br.BorrowerID = u2.UserID \n" +
            "inner join Device d on bs.DeviceID=d.DeviceID \n"+
            "inner join UserRole ur on br.BorrowerID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where br.BorrowApplyState = #{state} and br.IsDeleted = 0;")
    public List<BorrowApplyRecordList2> borrowApprovalList(String state);

    // 3、返回需要审批的报废申请单：设备管理员 （全部）   DeviceScrapListRes

    @Select("\n" +
            "select q.ScrapID, q.DeviceID, p.AssetNumber, q.ScrapTime, q.ScrapReason, q.ScrapState, p.DeviceName, q.ScrapPerson, r.RoleName \n"+
            "from DeviceScrapRecord q \n"+
            "inner join Device p on p.DeviceID=q.DeviceID \n"+
            "inner join User u on q.ScrapPerson = u.UserName \n"+
            "inner join UserRole ur on u.UserID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where q.ScrapState = #{state} and q.IsDeleted = 0;")
    public List<DeviceScrapList> scrapApprovalList(String state);


    //11、根据审批人ID，返回他的所有审批记录列表   ApprovalRecordResponse

    @Select("select a.ApprovalID, a.ApplySheetID, a.ApplyType, a.ApproverID, u.UserName, a.ApprovalDate, a.ApprovalDescription \n" +
            "from ApprovalRecord a \n" +
            "inner join User u on a.ApproverID = u.UserID \n" +
            "where a.IsDeleted = 0;")
    public List<ApprovalRecordResponse> allApprovalRecord(Integer id);

    //22、根据审批人ID，返回他的审批记录列表（已审批）   ApprovalRecordResponse
    @Select("select a.ApprovalID, a.ApplySheetID, a.ApplyType, a.ApproverID, u.UserName, a.ApprovalDate, a.ApprovalDescription \n" +
            "from ApprovalRecord a \n" +
            "inner join User u on a.ApproverID = u.UserID \n" +
            "where a.ApprovalDate is not null and a.IsDeleted = 0;")
    public List<ApprovalRecordResponse> allApprovalRecordNotNull(Integer id);

    //22、根据审批人ID，返回他的审批记录列表（未审批）   ApprovalRecordResponse
    @Select("select a.ApprovalID, a.ApplySheetID, a.ApplyType, a.ApproverID, u.UserName, a.ApprovalDate, a.ApprovalDescription \n" +
            "from ApprovalRecord a \n" +
            "inner join User u on a.ApproverID = u.UserID \n" +
            "where a.ApprovalDate is null and a.IsDeleted = 0;")
    public List<ApprovalRecordResponse> allApprovalRecordNull(Integer id);



    //1、按照设备申请时间筛选采购申请单：老师
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName deviceList, u2.UserName ,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName approveTutorName, r.RoleName, p.PurchaseBudget purchaseBudget \n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID \n"+
            "inner join UserRole ur on ps.PurchaseApplicantID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where ps.ApproveTutorID=#{id} and ps.PurchaseApplyDate >= #{mindate} and ps.PurchaseApplyDate <= #{maxdate} and ps.IsDeleted = 0;")
    public List<PurchaseApplySheetList2> getPSheetByTimeTe(Date mindate, Date maxdate, Integer id);

    //1、按照设备申请时间筛选采购申请单：设备管理员/院领导 （全部）
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName deviceList, u2.UserName ,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName approveTutorName, r.RoleName, p.PurchaseBudget purchaseBudget \n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID \n"+
            "inner join UserRole ur on ps.PurchaseApplicantID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where ps.PurchaseApplyDate >= #{mindate} and ps.PurchaseApplyDate <= #{maxdate} and ps.IsDeleted = 0;")
    public List<PurchaseApplySheetList2> getPSheetByTime(Date mindate, Date maxdate);

    //2、按照设备申请时间筛选借用申请单：老师
    @Select("\n" +
            "select br.BorrowApplyID, d.AssetNumber, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName, r.RoleName  \n" +
            "from BorrowApplyRecord br\n" +
            "inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on br.BorrowerID = u2.UserID \n" +
            "inner join Device d on bs.DeviceID=d.DeviceID \n"+
            "inner join UserRole ur on br.BorrowerID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where br.ApproveTutorID=#{id} and br.BorrowApplyState >= #{mindate} and br.BorrowApplyState <= #{maxdate} and br.IsDeleted = 0;")
    public List<BorrowApplyRecordList2> getBSheetByTimeTe(Date mindate, Date maxdate, Integer id);

    //2、按照设备申请时间筛选借用申请单：设备管理员 （全部）
    @Select("\n" +
            "select br.BorrowApplyID, d.AssetNumber, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName, r.RoleName  \n" +
            "from BorrowApplyRecord br\n" +
            "inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on br.BorrowerID = u2.UserID \n" +
            "inner join Device d on bs.DeviceID=d.DeviceID \n"+
            "inner join UserRole ur on br.BorrowerID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where br.BorrowApplyState >= #{mindate} and br.BorrowApplyState <= #{maxdate} and br.IsDeleted = 0;")
    public List<BorrowApplyRecordList2> getBSheetByTime(Date mindate, Date maxdate);

    //3、按照设备申请时间筛选报废申请单 （全部）
    @Select("\n" +
            "select q.ScrapID, q.DeviceID, p.AssetNumber, q.ScrapTime, q.ScrapReason, q.ScrapState, p.DeviceName, q.ScrapPerson, r.RoleName \n"+
            "from DeviceScrapRecord q \n"+
            "inner join Device p on p.DeviceID=q.DeviceID \n"+
            "inner join User u on q.ScrapPerson = u.UserName \n"+
            "inner join UserRole ur on u.UserID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where q.ScrapTime >= #{mindate} and q.ScrapTime <+ #{maxdate} and q.IsDeleted = 0;")
    public List<DeviceScrapList> getSSheetByTime(Date mindate, Date maxdate);

    //4、照价格区间筛选采购申请单：老师
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName deviceList, u2.UserName ,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName approveTutorName, r.RoleName, p.PurchaseBudget purchaseBudget \n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID \n"+
            "inner join UserRole ur on ps.PurchaseApplicantID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where ps.ApproveTutorID=#{id} and p.PurchaseBudget >= #{minprize} and p.PurchaseBudget <= #{maxprize} and ps.IsDeleted = 0;")
    public List<PurchaseApplySheetList2> getPSheetByPrizeTe(double minprize, double maxprize,Integer id);

    //4、照价格区间筛选采购申请单：设备管理员、院领导（全部）
    @Select("\n" +
            "select ps.PurchaseApplySheetID, p.DeviceName deviceList, u2.UserName ,ps.PurchaseApplyDate, ps.PurchaseApplyState,u1.UserName approveTutorName, r.RoleName, p.PurchaseBudget purchaseBudget \n" +
            "from PurchaseApplySheet ps \n" +
            "inner join PurchaseApply p on ps.PurchaseApplySheetID = p.PurchaseApplySheetID \n" +
            "inner join User u1 on ps.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on ps.PurchaseApplicantID = u2.UserID \n"+
            "inner join UserRole ur on ps.PurchaseApplicantID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where p.PurchaseBudget >= #{minprize} and p.PurchaseBudget <= #{maxprize}and ps.IsDeleted = 0; ")
    public List<PurchaseApplySheetList2> getPSheetByPrize(double minprize, double maxprize);

    //5、按照提出借用申请的用户类型筛选借用申请单：设备管理员（全部）
    @Select("\n" +
            "select br.BorrowApplyID, d.AssetNumber, bs.DeviceName deviceList, u2.UserName ,br.BorrowApplyDate,br.BorrowApplyState,u1.UserName approveTutorName, r.RoleName \n" +
            "from BorrowApplyRecord br\n" +
            "inner join BorrowApplySheet bs on br.BorrowApplyID = bs.BorrowApplyID\n" +
            "inner join User u1 on br.ApproveTutorID = u1.UserID \n" +
            "inner join User u2 on br.BorrowerID = u2.UserID\n" +
            "inner join UserRole ur on u2.UserId = ur.UserID \n" +
            "inner join Device d on bs.DeviceID=d.DeviceID \n"+
            "inner join UserRole ur on br.BorrowerID = ur.UserID \n"+
            "inner join Role r on ur.RoleID = r.RoleID \n"+
            "where ur.RoleId = #{rid} and br.IsDeleted = 0;")
    public List<BorrowApplyRecordList2> getAllBSheetByUserType(Integer rid);

}




