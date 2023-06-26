package com.ems.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.usercenter.mapper.RolePermissionMapper;
import com.ems.usercenter.model.entity.RolePermission;
import com.ems.usercenter.service.RolePermissionService;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【RolePermission(角色权限表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class RolePermissionServiceImpl extends MppServiceImpl<RolePermissionMapper, RolePermission>
    implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public boolean removeAllRolePermission(Integer roleId) {
        return rolePermissionMapper.removeAllRolePermission(roleId);
    }
}




