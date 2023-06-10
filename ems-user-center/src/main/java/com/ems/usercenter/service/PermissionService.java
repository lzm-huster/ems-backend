package com.ems.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.usercenter.model.entity.Permission;
import com.ems.usercenter.model.response.PermissionSimpleRes;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【Permission(权限表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface PermissionService extends IService<Permission> {
    List<String> getPermissionListByUserId(Integer userID);

    List<PermissionSimpleRes> getPermissionListByRoleId(Integer roleId);
}
