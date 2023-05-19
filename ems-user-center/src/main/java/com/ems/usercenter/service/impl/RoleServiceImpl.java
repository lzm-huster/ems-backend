package com.ems.usercenter.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.exception.BusinessException;
import com.ems.usercenter.mapper.RoleMapper;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Override
    public List<String> getRoleListByUserId(Integer userId) {
        if (ObjectUtil.isNull(userId)){
            return null;
        }
        return roleMapper.getRoleListByUserId(userId);
    }
}




