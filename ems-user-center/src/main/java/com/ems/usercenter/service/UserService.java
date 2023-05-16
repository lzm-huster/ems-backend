package com.ems.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.usercenter.model.entity.User;

public interface UserService extends IService<User> {
    User getUserById(Integer id);

    User userLogin(User user);
}
