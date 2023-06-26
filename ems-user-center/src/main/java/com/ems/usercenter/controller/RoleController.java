package com.ems.usercenter.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.mapper.RoleMapper;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.model.entity.RolePermission;
import com.ems.usercenter.model.request.RoleAddReq;
import com.ems.usercenter.model.request.RoleUpdateReq;
import com.ems.usercenter.model.response.PermissionSimpleRes;
import com.ems.usercenter.model.response.RoleDetailRes;
import com.ems.usercenter.model.response.RoleSimpleRes;
import com.ems.usercenter.service.PermissionService;
import com.ems.usercenter.service.RolePermissionService;
import com.ems.usercenter.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.context.Theme;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ResponseResult
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;

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
        List<Integer> permissionIdList = roleAddReq.getPermissionIdList();
        Integer roleID = role.getRoleID();
        List<RolePermission> collect = permissionIdList.stream().map(p -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleID(roleID);
            rolePermission.setPermissionID(p);
            return rolePermission;
        }).collect(Collectors.toList());
        boolean saveBatch = rolePermissionService.saveBatch(collect);
        if (!saveBatch){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"权限添加失败");
        }
        RoleSimpleRes roleSimpleRes = new RoleSimpleRes();
        BeanUtils.copyProperties(role,roleSimpleRes);
        return roleSimpleRes;
    }
    @GetMapping("/detail")
    public RoleDetailRes roleDetail(Integer roleId){
        if (ObjectUtil.isNull(roleId)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"角色Id不能为空");
        }
        Role role = roleService.getById(roleId);
        if (ObjectUtil.isNull(role)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"该角色不存在");
        }

        List<PermissionSimpleRes> permissionListByRoleId = permissionService.getPermissionListByRoleId(roleId);
        if (ObjectUtil.isNull(permissionListByRoleId)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        RoleDetailRes roleDetailRes = new RoleDetailRes();
        BeanUtils.copyProperties(role,roleDetailRes);
        roleDetailRes.setPermissionSimpleResListList(permissionListByRoleId);
        return roleDetailRes;
    }
    @Transactional
    @PostMapping("/update")
    public boolean roleUpdate(@RequestBody RoleUpdateReq roleUpdateReq){
        Integer roleId = roleUpdateReq.getRoleId();
        Role role = roleService.getById(roleId);
        if (ObjectUtil.isNull(role)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"当前角色不存在");
        }
        BeanUtils.copyProperties(roleUpdateReq,role);
        boolean update = roleService.updateById(role);
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"角色信息更新失败");
        }

        boolean remove = rolePermissionService.removeAllRolePermission(roleId);
        if(!remove){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新权限信息失败");
        }
        List<Integer> permissionIdList = roleUpdateReq.getPermissionIdList();
        List<RolePermission> collect = permissionIdList.stream().map(p -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleID(roleId);
            rolePermission.setPermissionID(p);
            return rolePermission;
        }).collect(Collectors.toList());
        boolean saveBatch = rolePermissionService.saveOrUpdateBatchByMultiId(collect);
        if (!saveBatch){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"权限更新失败");
        }
        return true;
    }
}
