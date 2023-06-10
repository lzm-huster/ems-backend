package com.ems.usercenter.controller;

import com.ems.annotation.ResponseResult;
import com.ems.usercenter.model.entity.Permission;
import com.ems.usercenter.model.response.PermissionSimpleRes;
import com.ems.usercenter.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@ResponseResult
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @GetMapping("/list")
    public List<PermissionSimpleRes> getPermissionList(){
        List<Permission> permissions = permissionService.list();
        List<PermissionSimpleRes> collect = permissions.stream().map(p -> {
            PermissionSimpleRes permissionSimpleRes = new PermissionSimpleRes();
            BeanUtils.copyProperties(p, permissionSimpleRes);
            return permissionSimpleRes;
        }).collect(Collectors.toList());
        return collect;
    }
}
