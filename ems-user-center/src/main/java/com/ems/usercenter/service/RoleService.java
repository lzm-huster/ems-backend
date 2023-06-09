package com.ems.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.model.request.RoleAddReq;
import com.ems.usercenter.model.response.RoleSimpleRes;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【Role(角色表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface RoleService extends IService<Role> {
    List<String> getRoleListByUserId(Integer userId);

    List<RoleSimpleRes> getRoleList();

    Role addRole(RoleAddReq roleAddReq);

}
