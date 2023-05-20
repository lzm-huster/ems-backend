package com.ems.business.controller;

import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.BorrowApplyRecordMapper;
import com.ems.business.mapper.BorrowApplySheetMapper;
import com.ems.business.model.entity.BorrowApplySheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ResponseResult
@RestController
@RequestMapping("/BorrowApplySheet")
public class BorrowApplySheetController {

    @Autowired
    private  BorrowApplyRecordMapper borrowApplyRecordMapper;
    @Autowired
    private  BorrowApplySheetMapper borrowApplySheetMapper;



    @GetMapping("/getBorrowApplySheets")
    //返回查看设备采购申请单对应设备详情数据
    public List<BorrowApplySheet> getBorrowApplySheets(int BorrowApplyID)
    {
        List<BorrowApplySheet> borrowApplySheets=null;
        borrowApplySheets=borrowApplySheetMapper.getBorrowApplySheetByBorrowApplyID(BorrowApplyID);

        return borrowApplySheets;
    }

    @GetMapping("/getBorrowDescription")

    //根据BorrowApplyID查看设备采购申请单详情数据(一条申请单数据)
    public String getBorrowDescription(int BorrowApplyID)
    {
        String description=null;
        description=borrowApplyRecordMapper.getDescriptionByBorrowApplyID(BorrowApplyID);

        return description;
    }



}
