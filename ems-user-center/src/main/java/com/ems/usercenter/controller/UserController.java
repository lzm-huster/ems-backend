package com.ems.usercenter.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.common.ErrorCode;
import com.ems.cos.MinioUtil;
import com.ems.cos.config.MinioConfigProperties;
import com.ems.exception.BusinessException;
import com.ems.redis.RedisUtil;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.request.UserLoginReq;
import com.ems.usercenter.service.UserService;
import com.ems.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ResponseResult
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioConfigProperties minioProperties;

    private static final String avatarPrefix = "Avatar/";

    @Value("crypto.md5.salt")
    private String salt;



    @ResponseResult
    @PostMapping("/login")
    public String login(@RequestBody UserLoginReq userLoginReq) {
        String userNumber = userLoginReq.getUserNumber();
        String userPassword = userLoginReq.getUserPassword();
        // 判断空
        if (StringUtils.isEmpty(userNumber) || StringUtils.isEmpty(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码为空");
        }
        // 判断正则模式
        String pattern = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,20}$";
        Matcher matcher = Pattern.compile(pattern).matcher(userPassword);
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不符合要求");
        }
        // 判断是否存在数据库中
        User user = new User();
        BeanUtils.copyProperties(userLoginReq, user);
        MD5 md5 = new MD5(salt.getBytes());
        user.setPassword(md5.digestHex(userPassword));
        User queryRes = userService.userLogin(user);
        if (ObjectUtil.isNull(queryRes)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户名或密码错误");
        }
        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("UserId",queryRes.getUserID());
        claims.put("UserName", queryRes.getUserName());
        claims.put("IDNumber", queryRes.getIDNumber());
        String token = jwtUtil.generateToken(queryRes.getUserID().toString(), claims);
        // redis存token及信息
        storeUserInfoRedis(queryRes, token);
        return token;
    }

    private void storeUserInfoRedis(User user, String token) {
        // redis存token及信息
        String key = RedisConstant.UserPrefix + user.getUserID();
        Map<String, Object> value = new HashMap<>();
        Integer userId = user.getUserID();
//        value.put(RedisConstant.UserToken, token);
//        value.put(RedisConstant.UserInfo, user);
//        List<UserMenuPermissionRes> menuPermissionList = userService.getMenuPermissionList(userId);
//        value.put(RedisConstant.MenuPermission, menuPermissionList);
//        value.put(RedisConstant.UserPermission, userService.getUserPermissionList(userId));
        redisUtil.hmset(key, value);
    }
}
