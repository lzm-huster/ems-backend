package com.ems.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.business.model.entity.ApprovalRecord;
import com.ems.business.model.entity.PurchaseApply;
import com.ems.business.model.response.*;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;


/**
* @author 龙志明
* @description 针对表【ApprovalRecord(申请审批记录表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface ApprovalRecordService extends IService<ApprovalRecord> {

    // 1、返回需要审批的采购申请单：老师   PurchaseApplySheetList2
    public List<PurchaseApplySheetList2> purchaseApprovalListTe(Integer id, String state);

    // 1、返回需要审批的采购申请单：设备管理员/院领导 （全部）   PurchaseApplySheetList2
    public List<PurchaseApplySheetList2> purchaseApprovalList(String state);


    // 2、返回需要审批的借用申请单：老师   BorrowApplyRecordList2
    public List<BorrowApplyRecordList2> borrowApprovalListTe(Integer id, String state);

    // 2、返回需要审批的借用申请单：设备管理员 （全部）   BorrowApplyRecordList2
    public List<BorrowApplyRecordList2> borrowApprovalList(String state);

    // 3、返回需要审批的报废申请单：设备管理员 （全部）   DeviceScrapListRes
    public List<DeviceScrapList> scrapApprovalList(String state);


    //11、根据审批人ID，返回他的所有审批记录列表   ApprovalRecordResponse
    public List<ApprovalRecordResponse> allApprovalRecord(Integer id);

    //22、根据审批人ID，返回他的审批记录列表（已审批）   ApprovalRecordResponse
    public List<ApprovalRecordResponse> allApprovalRecordNotNull(Integer id);

    //22、根据审批人ID，返回他的审批记录列表（未审批）   ApprovalRecordResponse
    public List<ApprovalRecordResponse> allApprovalRecordNull(Integer id);



    //1、按照设备申请时间筛选采购申请单：老师
    public List<PurchaseApplySheetList2> getPSheetByTimeTe(Date mindate, Date maxdate, Integer id);

    //1、按照设备申请时间筛选采购申请单：设备管理员/院领导 （全部）
    public List<PurchaseApplySheetList2> getPSheetByTime(Date mindate, Date maxdate);

    //2、按照设备申请时间筛选借用申请单：老师
    public List<BorrowApplyRecordList2> getBSheetByTimeTe(Date mindate, Date maxdate, Integer id);

    //2、按照设备申请时间筛选借用申请单：设备管理员 （全部）
    public List<BorrowApplyRecordList2> getBSheetByTime(Date mindate, Date maxdate);

    //3、按照设备申请时间筛选报废申请单 （全部）
    public List<DeviceScrapList> getSSheetByTime(Date mindate, Date maxdate);

    //4、照价格区间筛选采购申请单：老师
    public List<PurchaseApplySheetList2> getPSheetByPrizeTe(double minprize, double maxprize, Integer id);

    //4、照价格区间筛选采购申请单：设备管理员、院领导（全部）
    public List<PurchaseApplySheetList2> getPSheetByPrize(double minprize, double maxprize);


    //5、按照用户类型筛选借用申请单：设备管理员（全部）
    public List<BorrowApplyRecordList2> getAllBSheetByUserType(Integer rid);


}
