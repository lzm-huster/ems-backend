package com.ems.usercenter.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.usercenter.mapper.UserMapper;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.response.UserDetailRes;
import com.ems.usercenter.service.UserRoleService;
import com.ems.usercenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleService userRoleService;
    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     */
    @Override
    public User getUserById(Integer id) {
        User user = userMapper.selectById(id);
        if (Objects.isNull(user)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"查找用户不存在");
        }
        return user;
    }
    /***
     * 学号/工号登录
     * @param user
     * @return
     */
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
        return queryUser;
    }
    /***
     * 手机号登录
     * @param user
     * @return
     */
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
        return queryUser;
    }
    /***
     * 邮箱注册
     * @param user
     * @return
     */
    @Override
    public boolean registerByEmail(User user) {
        // TODO 还要处理验证码，此处暂时未处理
        String idNumber = user.getIDNumber();
        if(StringUtils.isBlank(user.getUserName())){
            String tempName = "用户"+ System.currentTimeMillis();
            user.setUserName(tempName);
        }
        // 检查是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IDNumber", idNumber);
        User selectOne = userMapper.selectOne(queryWrapper);
        if (!ObjectUtil.isNull(selectOne)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该学号/工号已注册");
        }
        int insert = userMapper.insert(user);
        userRoleService.initUserRole(user.getUserID(),5);
        return insert > 0;
    }
    /***
     * 手机号注册
     * @param user
     * @return
     */
    @Override
    public boolean registerByPhone(User user) {
        // TODO 还要处理验证码，此处暂时未处理
        String phoneNumber = user.getPhoneNumber();
        if(StringUtils.isBlank(user.getUserName())){
            String tempName = "用户"+ System.currentTimeMillis();
            user.setUserName(tempName);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PhoneNumber",phoneNumber);
        User selectOne = userMapper.selectOne(queryWrapper);
        if (!ObjectUtil.isNull(selectOne)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该手机号已注册");
        }
        int insert = userMapper.insert(user);
        userRoleService.initUserRole(user.getUserID(),7);
        return insert > 0;
    }

    @Override
    public UserDetailRes getUserDetail(int userId) {
        if (ObjectUtil.isNull(userId)){
            return null;
        }
        return userMapper.getUserDetail(userId);
    }

    @Override
    public List<UserDetailRes> getAllDetail() {
        return userMapper.getAllDetail();
    }

}
