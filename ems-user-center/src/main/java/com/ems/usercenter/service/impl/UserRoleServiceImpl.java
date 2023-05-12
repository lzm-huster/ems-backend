package com.ems.usercenter.service.impl;




import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.usercenter.mapper.UserRoleMapper;
import com.ems.usercenter.model.entity.UserRole;
import com.ems.usercenter.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【UserRole(用户角色表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

}



