package com.ems.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.usercenter.mapper.RoleMapper;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.service.RoleService;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【Role(角色表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {

}




