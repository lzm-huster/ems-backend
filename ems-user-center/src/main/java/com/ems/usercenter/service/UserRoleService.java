package com.ems.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.usercenter.model.entity.UserRole;
import com.github.jeffreyning.mybatisplus.service.IMppService;

/**
* @author 龙志明
* @description 针对表【UserRole(用户角色表)】的数据库操作Service
* @createDate 2023-04-24 09:03:00
*/
public interface UserRoleService extends IMppService<UserRole> {
    /**
     * 注册时初始化用户角色
     * @param userId
     * @param roleId
     * @return
     */
    boolean initUserRole(Integer userId,Integer roleId);
}
