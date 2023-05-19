package com.ems.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.business.mapper.BorrowApplyRecordMapper;
import com.ems.business.model.entity.BorrowApplyRecord;
import com.ems.business.model.response.BorrowApplyRecordList;
import com.ems.business.service.BorrowApplyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【BorrowApplyRecord(借用申请单表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Transactional
@Service
public class BorrowApplyRecordServiceImpl extends ServiceImpl<BorrowApplyRecordMapper, BorrowApplyRecord>
    implements BorrowApplyRecordService {

    @Autowired
    BorrowApplyRecordMapper borrowApplyRecordMapper;

    //获取个人借用列表数据
    public List<BorrowApplyRecordList> getPersonBorrowApplyRecordList(int UserID)
    {
        List<BorrowApplyRecordList> borrowApplyRecordLists=null;
        borrowApplyRecordLists=borrowApplyRecordMapper.getPersonBorrowApplyRecordList(UserID);
        return borrowApplyRecordLists;
    }

    //获取所有用户借用列表数据
    public List<BorrowApplyRecordList> getAllBorrowApplyRecordList()
    {
        List<BorrowApplyRecordList> borrowApplyRecordLists=null;
        borrowApplyRecordLists=borrowApplyRecordMapper.getAllBorrowApplyRecordList();
        return borrowApplyRecordLists;
    }

}




