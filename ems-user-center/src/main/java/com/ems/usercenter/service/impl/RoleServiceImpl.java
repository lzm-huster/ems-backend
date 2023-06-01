package com.ems.usercenter.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.mapper.RoleMapper;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.model.entity.RolePermission;
import com.ems.usercenter.model.request.RoleAddReq;
import com.ems.usercenter.model.response.RoleSimpleRes;
import com.ems.usercenter.service.RolePermissionService;
import com.ems.usercenter.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 龙志明
 * @description 针对表【Role(角色表)】的数据库操作Service实现
 * @createDate 2023-04-24 09:03:00
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Override
    public List<String> getRoleListByUserId(Integer userId) {
        if (ObjectUtil.isNull(userId)) {
            return null;
        }
        return roleMapper.getRoleListByUserId(userId);
    }

    @Override
    public List<RoleSimpleRes> getRoleList() {
        return roleMapper.getRoleList();
    }

    @Transactional
    @Override
    public Role addRole(RoleAddReq roleAddReq) {
        Role role = new Role();
        BeanUtils.copyProperties(roleAddReq, role);
        int insert = roleMapper.insert(role);
        if (insert <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
        }
        Integer roleID = role.getRoleID();
        List<Integer> permissionIdList = roleAddReq.getPermissionIdList();
        Set<Integer> permissionSet = new HashSet<>(permissionIdList);
        List<RolePermission> collect = permissionSet.stream().map(p -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleID(roleID);
            rolePermission.setPermissionID(p);
            return rolePermission;
        }).collect(Collectors.toList());
        boolean b = rolePermissionService.saveBatch(collect);
        if (!b){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
        }
        return role;
    }

}




