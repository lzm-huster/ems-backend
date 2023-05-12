package com.ems.business.controller;

import com.ems.annotation.ResponseResult;
import com.ems.business.mapper.DeviceMapper;
import com.ems.business.service.impl.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangwy
 */

@ResponseResult
@RestController
@RequestMapping("/approval")
public class ApprovalController {

    @Autowired
    private DeviceServiceImpl deviceService;

    @GetMapping("/test")
    public void test(){
        deviceService.test();
    }





}
