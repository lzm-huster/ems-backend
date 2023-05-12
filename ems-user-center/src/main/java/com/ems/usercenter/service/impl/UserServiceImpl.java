package com.ems.usercenter.service.impl;

import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.mapper.UserMapper;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUserById(Integer id) {
        User user = userMapper.selectById(id);
        if (Objects.isNull(user)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"查找用户不存在");
        }
        return user;
    }
}
