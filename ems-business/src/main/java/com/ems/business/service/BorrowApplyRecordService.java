package com.ems.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.business.model.entity.BorrowApplyRecord;
import com.ems.business.model.response.BorrowApplyRecordList;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【BorrowApplyRecord(借用申请单表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface BorrowApplyRecordService extends IService<BorrowApplyRecord> {
    //获取个人借用记录
    public List<BorrowApplyRecordList> getPersonBorrowApplyRecordList(int UserID);
    //获取所有借用记录
    public List<BorrowApplyRecordList> getAllBorrowApplyRecordList();

}
