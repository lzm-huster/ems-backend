package com.ems.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.business.mapper.ApprovalRecordMapper;
import com.ems.business.mapper.PurchaseApplySheetMapper;
import com.ems.business.model.entity.ApprovalRecord;
import com.ems.business.model.entity.PurchaseApply;
import com.ems.business.model.response.ApprovalRecordResponse;
import com.ems.business.model.response.BorrowApplyRecordList2;
import com.ems.business.model.response.DeviceScrapList;
import com.ems.business.model.response.PurchaseApplySheetList2;
import com.ems.business.service.ApprovalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author 龙志明
* @description 针对表【ApprovalRecord(申请审批记录表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class ApprovalRecordServiceImpl extends ServiceImpl<ApprovalRecordMapper, ApprovalRecord>
    implements ApprovalRecordService {

    @Autowired
    private ApprovalRecordMapper approvalRecordMapper;


    @Override
    public List<PurchaseApplySheetList2> purchaseApprovalListTe(Integer id, String state) {
        List<PurchaseApplySheetList2> List1 = null;
        List1 = approvalRecordMapper.purchaseApprovalListTe(id,state);
        return List1;
    }

    @Override
    public List<PurchaseApplySheetList2> purchaseApprovalList(String state) {
        List<PurchaseApplySheetList2> List1 = null;
        List1 = approvalRecordMapper.purchaseApprovalList(state);
        return List1;
    }

    @Override
    public List<BorrowApplyRecordList2> borrowApprovalListTe(Integer id, String state) {
        List<BorrowApplyRecordList2> List1 = null;
        List1 = approvalRecordMapper.borrowApprovalListTe(id, state);
        return List1;
    }

    @Override
    public List<BorrowApplyRecordList2> borrowApprovalList(String state) {
        List<BorrowApplyRecordList2> List1 = null;
        List1 = approvalRecordMapper.borrowApprovalList(state);
        return List1;
    }

    @Override
    public List<DeviceScrapList> scrapApprovalList(String state) {
        List<DeviceScrapList> List1 = null;
        List1 = approvalRecordMapper.scrapApprovalList(state);
        return List1;
    }

    @Override
    public List<ApprovalRecordResponse> allApprovalRecord(Integer id) {
        List<ApprovalRecordResponse> List1 = null;
        List1 = approvalRecordMapper.allApprovalRecord(id);
        return List1;
    }

    @Override
    public List<ApprovalRecordResponse> allApprovalRecordNotNull(Integer id) {
        List<ApprovalRecordResponse> List1 = null;
        List1 = approvalRecordMapper.allApprovalRecordNotNull(id);
        return List1;
    }

    @Override
    public List<ApprovalRecordResponse> allApprovalRecordNull(Integer id) {
        List<ApprovalRecordResponse> List1 = null;
        List1 = approvalRecordMapper.allApprovalRecordNull(id);
        return List1;
    }

    @Override
    public List<PurchaseApplySheetList2> getPSheetByTimeTe(Date mindate, Date maxdate, Integer id) {
        List<PurchaseApplySheetList2> List1 = null;
        List1 = approvalRecordMapper.getPSheetByTimeTe(mindate, maxdate, id);
        return List1;
    }

    @Override
    public List<PurchaseApplySheetList2> getPSheetByTime(Date mindate, Date maxdate) {
        List<PurchaseApplySheetList2> List1 = null;
        List1 = approvalRecordMapper.getPSheetByTime(mindate, maxdate);
        return List1;
    }

    @Override
    public List<BorrowApplyRecordList2> getBSheetByTimeTe(Date mindate, Date maxdate, Integer id) {
        List<BorrowApplyRecordList2> List1 = null;
        List1 = approvalRecordMapper.getBSheetByTimeTe(mindate, maxdate, id);
        return List1;
    }

    @Override
    public List<BorrowApplyRecordList2> getBSheetByTime(Date mindate, Date maxdate) {
        List<BorrowApplyRecordList2> List1 = null;
        List1 = approvalRecordMapper.getBSheetByTime(mindate, maxdate);
        return List1;
    }

    @Override
    public List<DeviceScrapList> getSSheetByTime(Date mindate, Date maxdate) {
        List<DeviceScrapList> List1 = null;
        List1 = approvalRecordMapper.getSSheetByTime(mindate, maxdate);
        return List1;
    }

    @Override
    public List<PurchaseApplySheetList2> getPSheetByPrizeTe(double minprize, double maxprize, Integer id) {
        List<PurchaseApplySheetList2> List1 = null;
        List1 = approvalRecordMapper.getPSheetByPrizeTe(minprize, maxprize, id);
        return List1;
    }

    @Override
    public List<PurchaseApplySheetList2> getPSheetByPrize(double minprize, double maxprize) {
        List<PurchaseApplySheetList2> List1 = null;
        List1 = approvalRecordMapper.getPSheetByPrize(minprize, maxprize);
        return List1;
    }

    @Override
    public List<BorrowApplyRecordList2> getAllBSheetByUserType(Integer rid) {
        List<BorrowApplyRecordList2> List1 = null;
        List1 = approvalRecordMapper.getAllBSheetByUserType(rid);
        return List1;
    }
}




