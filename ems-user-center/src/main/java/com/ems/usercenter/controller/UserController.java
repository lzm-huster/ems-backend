package com.ems.usercenter.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.AuthCheck;
import com.ems.annotation.ResponseResult;
import com.ems.common.ErrorCode;
import com.ems.cos.MinioUtil;
import com.ems.cos.config.MinioConfigProperties;
import com.ems.exception.BusinessException;
import com.ems.redis.RedisUtil;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.request.UserLoginReq;
import com.ems.usercenter.model.request.UserRegisterReq;
import com.ems.usercenter.model.request.UserAddReq;
import com.ems.usercenter.model.response.PermissionSimpleRes;
import com.ems.usercenter.model.response.UserCurrentRes;
import com.ems.usercenter.model.response.UserDetailRes;
import com.ems.usercenter.service.PermissionService;
import com.ems.usercenter.service.RoleService;
import com.ems.usercenter.service.UserService;
import com.ems.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private UserRedisConstant userRedisConstant;
    @Autowired
    private MinioConfigProperties minioProperties;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    private static final String avatarPrefix = "Avatar/";

    @Value("crypto.md5.salt")
    private String salt;




    @PostMapping("/login")
    public String login(@RequestBody UserLoginReq userLoginReq) {

        String userNumber = userLoginReq.getUserNumber();
        String userPassword = userLoginReq.getPassword();
        Integer loginType = userLoginReq.getLoginType();
        // 判断空
        if (StringUtils.isEmpty(userNumber) || StringUtils.isEmpty(userPassword) || ObjectUtil.isEmpty(loginType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在参数为空");
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
        User queryRes = null;
        if (ObjectUtil.equal(loginType,1)){
            user.setIDNumber(userNumber);
            queryRes = userService.userLoginByIDNumber(user);
        }
        if (ObjectUtil.equal(loginType,2)){
            user.setPhoneNumber(userNumber);
            queryRes = userService.userLoginByPhone(user);
        }
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
        userRedisConstant.storeUserInfoRedis(queryRes, token);
        return token;
    }

    @AuthCheck(mustAuth = {})
    @PostMapping("/currentUser")
    public UserCurrentRes getCurrentUser(@RequestHeader("token") String token) {
        // redis取信息
        Map<Object, Object> redisUserInfo = userRedisConstant.getRedisMapFromToken(token);
        // 获取基础User信息
        User user = (User) redisUserInfo.get(RedisConstant.UserInfo);
        Integer userID = user.getUserID();
        // 赋值给response类型
        UserCurrentRes userCurrentRes = new UserCurrentRes();
        BeanUtils.copyProperties(user, userCurrentRes);
        // 角色信息
        Object roleObj = redisUserInfo.get(RedisConstant.UserRole);
        List<String> roleList = (List<String>) roleObj;

        Object permissionObj = redisUserInfo.get(RedisConstant.UserPermission);
        List<PermissionSimpleRes> permissionList = (List<PermissionSimpleRes>) roleObj;

//        // 菜单权限信息
//        Object menuObj = redisUserInfo.get(RedisConstant.MenuPermission);
//        List<UserMenuPermissionRes> menuPermissionList = (List<UserMenuPermissionRes>) menuObj;
//        userCurrentRes.setMenuPermissionList(menuPermissionList);
//        // 表权限信息
//        Object permissionObj = redisUserInfo.get(RedisConstant.UserPermission);
//        List<String> userPermissionList = (List<String>) permissionObj;
//        userCurrentRes.setUserPermissionList(userPermissionList);
        return userCurrentRes;
    }


    @PostMapping("/register")
    public boolean register(@RequestBody UserRegisterReq userRegisterReq) {
        String userNumber = userRegisterReq.getUserNumber();
        String userPassword = userRegisterReq.getUserPassword();
        // 判空
        if (StringUtils.isBlank(userNumber) || StringUtils.isBlank(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或者参数为空");
        }
        // 判断正则
        String pattern = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,20}$";
        Matcher matcher = Pattern.compile(pattern).matcher(userPassword);
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不符合要求");
        }
        // 检查是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UserNumber", userNumber);
        User selectOne = userService.getOne(queryWrapper);
        if (!ObjectUtil.isNull(selectOne)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该学号已存在");
        }
        // 开始注册
        User user = new User();
        BeanUtils.copyProperties(userRegisterReq, user);
        MD5 md5 = new MD5(salt.getBytes());
        user.setPassword(md5.digestHex(userPassword));
        boolean save = userService.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "注册失败");
        }
        return true;
    }

    @AuthCheck(mustAuth = {"user:add"})
    @GetMapping("/query")
    public UserDetailRes queryUserDetail(@RequestParam("userId") Integer userId){
        if (ObjectUtil.isNull(userId)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"userId参数为空");
        }
        User userById = userService.getUserById(userId);
        if (ObjectUtil.isNull(userById)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"用户信息查询失败");
        }
        UserDetailRes userDetailRes = new UserDetailRes();
        BeanUtils.copyProperties(userById,userDetailRes);
        return userDetailRes;
    }
    @GetMapping("/info")
    public UserDetailRes getUserInfo(@RequestHeader("token") String token){
        Map<Object, Object> redisMapFromToken = userRedisConstant.getRedisMapFromToken(token);
        User user = (User)redisMapFromToken.get(RedisConstant.UserInfo);
        UserDetailRes userDetailRes = new UserDetailRes();
        BeanUtils.copyProperties(user,userDetailRes);
        return userDetailRes;
    }
    @PostMapping("/add")
    public boolean addUser(@RequestBody UserAddReq userAddReq){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IDNumber",userAddReq.getIDNumber()).or()
                .eq("Email",userAddReq.getEmail()).or()
                .eq("PhoneNumber",userAddReq.getPhoneNumber());
        List<User> users = userService.list(queryWrapper);
        if (!users.isEmpty()){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"已存在相同的学号/工号，邮箱，手机号的用户记录");
        }
        User user = new User();
        BeanUtils.copyProperties(userAddReq,user);
        if (StringUtils.isBlank(user.getPassword())){
            MD5 md5 = new MD5(salt.getBytes());
            user.setPassword(md5.digestHex("ems123456"));
        }
        boolean save = userService.save(user);
        if (!save){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"增加用户失败");
        }
        return true;
    }

    @GetMapping("/list")
    public List<UserDetailRes> getUserList(){
        List<User> userList = userService.list();
        return userList.stream().map(user -> {
            UserDetailRes userDetailRes = new UserDetailRes();
            BeanUtils.copyProperties(user, userDetailRes);
            return userDetailRes;
        }).collect(Collectors.toList());
    }
}
