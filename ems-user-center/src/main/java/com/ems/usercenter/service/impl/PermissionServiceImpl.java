package com.ems.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.usercenter.mapper.PermissionMapper;
import com.ems.usercenter.model.entity.Permission;
import com.ems.usercenter.service.PermissionService;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【Permission(权限表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService {

}




