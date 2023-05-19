package com.ems.usercenter.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.mapper.UserMapper;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
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

    @Override
    public User userLoginByIDNumber(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IDNumber",user.getIDNumber());
        User queryUser = userMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(queryUser)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"无此用户");
        }
        if (!queryUser.getPassword().equals(user.getPassword())){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"密码错误");
        }
        //  int insert = userMapper.insert(user);
        return queryUser;
    }

    @Override
    public User userLoginByPhone(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PhoneNumber",user.getPhoneNumber());
        User queryUser = userMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(queryUser)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"无此用户");
        }
        if (!queryUser.getPassword().equals(user.getPassword())){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"密码错误");
        }
        //  int insert = userMapper.insert(user);
        return queryUser;
    }

//    @Override
//    public User userLogin(User user) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("IDNumber",user.getIDNumber());
//        User queryUser = userMapper.selectOne(queryWrapper);
//        if (ObjectUtil.isNull(queryUser)){
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"无此用户");
//        }
//        if (!queryUser.getPassword().equals(user.getPassword())){
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"密码错误");
//        }
//        //  int insert = userMapper.insert(user);
//        return queryUser;
//    }
}
