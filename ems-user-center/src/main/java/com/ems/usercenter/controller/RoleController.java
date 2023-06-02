package com.ems.usercenter.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.model.request.RoleAddReq;
import com.ems.usercenter.model.response.RoleSimpleRes;
import com.ems.usercenter.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ResponseResult
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public List<RoleSimpleRes> getRoleList(){
        return roleService.getRoleList();
    }

    @PostMapping("/add")
    public RoleSimpleRes addRole(@RequestBody RoleAddReq roleAddReq){
        String roleName = roleAddReq.getRoleName();
        String roleDesc = roleAddReq.getRoleDescription();
        if (StringUtils.isBlank(roleDesc)||StringUtils.isBlank(roleName)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"存在参数为空");
        }
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("RoleName",roleName);
        Role one = roleService.getOne(queryWrapper);
        if (ObjectUtil.isNotEmpty(one)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"该角色名已存在");
        }
        Role role = roleService.addRole(roleAddReq);
        if (ObjectUtil.isNull(role)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"新增角色失败，请重试");
        }
        RoleSimpleRes roleSimpleRes = new RoleSimpleRes();
        BeanUtils.copyProperties(role,roleSimpleRes);
        return roleSimpleRes;
    }
}
