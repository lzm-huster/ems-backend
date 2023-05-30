package com.ems.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.usercenter.model.entity.User;

public interface UserService extends IService<User> {
    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     */
    User getUserById(Integer id);

    /***
     * 学号/工号登录
     * @param user
     * @return
     */
    User userLoginByIDNumber(User user);
    /***
     * 手机号登录
     * @param user
     * @return
     */
    User userLoginByPhone(User user);
    /***
     * 邮箱注册
     * @param user
     * @return
     */
    boolean registerByEmail(User user);
    /***
     * 手机号注册
     * @param user
     * @return
     */
    boolean registerByPhone(User user);
}
