package com.ems.usercenter.constant;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.redis.RedisUtil;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.service.UserService;
import com.ems.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRedisConstant {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;
    public void storeUserInfoRedis(User user, String token) {
        // redis存token及信息
        String key = RedisConstant.UserPrefix + user.getUserID();
        Map<String, Object> value = new HashMap<>();
        Integer userId = user.getUserID();
        value.put(RedisConstant.UserToken, token);
        value.put(RedisConstant.UserInfo, user);
//        List<UserMenuPermissionRes> menuPermissionList = userService.getMenuPermissionList(userId);
//        value.put(RedisConstant.MenuPermission, menuPermissionList);
//        value.put(RedisConstant.UserPermission, userService.getUserPermissionList(userId));
        redisUtil.hmset(key, value);
    }
    public Map<Object,Object> getRedisMapFromToken(String token){
        boolean tokenExpired = jwtUtil.isTokenExpired(token);
        if (tokenExpired) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "token已过期");
        }
        Claims claimsFormToken = jwtUtil.getClaimsFromToken(token);
        if (ObjectUtil.isNull(claimsFormToken)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "token已失效");
        }
        String userId = claimsFormToken.get("UserId", String.class);
        String key = RedisConstant.UserPrefix + userId;
        Map<Object, Object> redisUserInfo = redisUtil.hmget(key);
        if (ObjectUtil.isNull(redisUserInfo)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("UserId", userId);
            User user = userService.getOne(queryWrapper);
            storeUserInfoRedis(user, token);
            redisUserInfo = redisUtil.hmget(key);
        }
        // redis取信息
         return redisUserInfo;
    }

}
