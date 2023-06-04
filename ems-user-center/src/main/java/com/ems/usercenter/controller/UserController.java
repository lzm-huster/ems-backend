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
import com.ems.cos.service.CosService;
import com.ems.exception.BusinessException;
import com.ems.redis.RedisUtil;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.entity.UserRole;
import com.ems.usercenter.model.request.UserAddReq;
import com.ems.usercenter.model.request.UserLoginReq;
import com.ems.usercenter.model.request.UserRegisterReq;
import com.ems.usercenter.model.request.UserUpdateReq;
import com.ems.usercenter.model.response.UserCurrentRes;
import com.ems.usercenter.model.response.UserDetailRes;
import com.ems.usercenter.service.PermissionService;
import com.ems.usercenter.service.RoleService;
import com.ems.usercenter.service.UserRoleService;
import com.ems.usercenter.service.UserService;
import com.ems.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private UserRedisConstant userRedisConstant;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private MinioConfigProperties minioProperties;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private CosService cosService;

    private static final String avatarPrefix = "Avatar/";

    @Value("${crypto.md5.salt}")
    private String salt;
    @Value("${default.avatar}")
    private String defaultAvatar;
    @Value("${default.password}")
    private String defaultPassword;


    /**
     * 登录接口   loginType -1 学号/工号登录
     * loginType -2 手机号登录
     *
     * @param userLoginReq
     * @return
     */
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
        if (ObjectUtil.equal(loginType, 1)) {
            user.setIDNumber(userNumber);
            queryRes = userService.userLoginByIDNumber(user);
        }
        if (ObjectUtil.equal(loginType, 2)) {
            user.setPhoneNumber(userNumber);
            queryRes = userService.userLoginByPhone(user);
        }
        if (ObjectUtil.isNull(queryRes)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户名或密码错误");
        }
        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("UserId", queryRes.getUserID());
        claims.put("UserName", queryRes.getUserName());
        claims.put("IDNumber", queryRes.getIDNumber());
        String token = jwtUtil.generateToken(queryRes.getUserID().toString(), claims);
        // redis存token及信息
        userRedisConstant.storeUserInfoRedis(queryRes, token);
        return token;
    }

    /**
     * 获取当前用户信息
     *
     * @param token
     * @return
     */
    @AuthCheck()
    @GetMapping("/currentUser")
    public UserCurrentRes getCurrentUser(@RequestHeader(value = "token", required = false) String token) {
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
        userCurrentRes.setRoleList(roleList);

        Object permissionObj = redisUserInfo.get(RedisConstant.UserPermission);
        List<String> permissionList = (List<String>) permissionObj;
        userCurrentRes.setUserPermissionList(permissionList);
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

    /**
     * 获取验证码
     *
     * @return
     */
    @PostMapping("/getCaptcha")
    public String getCaptcha() {
        // TODO 处理消息发送，验证码调用
        return "1234";
    }

    /**
     * 注册 1- 邮箱注册 2-手机号注册
     *
     * @param userRegisterReq
     * @return
     */
    @PostMapping("/register")
    public boolean register(@RequestBody UserRegisterReq userRegisterReq) {
        String idNumber = userRegisterReq.getIDNumber();
        String userPassword = userRegisterReq.getPassword();
        String confirm = userRegisterReq.getConfirm();
        // 判空
        if (StringUtils.isBlank(userPassword) || StringUtils.isBlank(confirm)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码和确认密码不能为空");
        }
        // 判断正则
        String pattern = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,20}$";
        Matcher matcher = Pattern.compile(pattern).matcher(userPassword);
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不符合要求");
        }
        // 开始注册
        User user = new User();
        BeanUtils.copyProperties(userRegisterReq, user);
        MD5 md5 = new MD5(salt.getBytes());
        user.setPassword(md5.digestHex(userPassword));
        Integer registerType = userRegisterReq.getRegisterType();
        user.setAvatar(defaultAvatar);
        boolean save = false;
        // 根据不同类型进行注册
        if (ObjectUtil.equal(registerType, 1)) {
            save = userService.registerByEmail(user);
        } else if (ObjectUtil.equal(registerType, 1)) {
            save = userService.registerByPhone(user);
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册类型有误");
        }
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "注册失败");
        }
        return true;
    }

    /**
     * 获取当前用户详细信息
     *
     * @param token
     * @return
     */
    @GetMapping("/info")
    public UserDetailRes getUserInfo(@RequestHeader(value = "token", required = false) String token) {
        Map<Object, Object> redisMapFromToken = userRedisConstant.getRedisMapFromToken(token);
        User user = (User) redisMapFromToken.get(RedisConstant.UserInfo);
        UserDetailRes userDetailRes = new UserDetailRes();
        BeanUtils.copyProperties(user, userDetailRes);
        return userDetailRes;
    }

    /**
     * 获取用户详细信息 - 用户管理
     *
     * @param userId
     * @return
     */

    @GetMapping("/query")
    public UserDetailRes queryUserDetail(@RequestParam("userId") Integer userId) {
        if (ObjectUtil.isNull(userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "userId参数为空");
        }
        User userById = userService.getUserById(userId);
        if (ObjectUtil.isNull(userById)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户信息查询失败");
        }
        return userService.getUserDetail(userId);
    }

    /**
     * 用户添加 - 用户管理
     *
     * @param userAddReq
     * @return
     */
    @Transactional
    @AuthCheck(mustAuth = {"user:add"})
    @PostMapping("/add")
    public UserDetailRes addUser(@RequestBody UserAddReq userAddReq) {
        Integer roleId = userAddReq.getRoleId();
        if (ObjectUtil.isNull(roleId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户角色不能为空");
        }
        String phoneNumber = userAddReq.getPhoneNumber();
        if (ObjectUtil.isNull(phoneNumber)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户联系方式不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IDNumber", userAddReq.getIDNumber()).or()
                .eq("Email", userAddReq.getEmail()).or()
                .eq("PhoneNumber", userAddReq.getPhoneNumber());
        List<User> users = userService.list(queryWrapper);
        if (!users.isEmpty()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已存在相同的学号/工号或邮箱或手机号的用户记录");
        }
        User user = new User();
        BeanUtils.copyProperties(userAddReq, user);
        if (StringUtils.isBlank(user.getPassword())) {
            MD5 md5 = new MD5(salt.getBytes());
            user.setPassword(md5.digestHex("ems123456"));
        }
        boolean save = userService.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "增加用户失败");
        }
        Integer userID = user.getUserID();
        UserRole userRole = new UserRole();
        userRole.setUserID(userID);
        userRole.setRoleID(userAddReq.getRoleId());
        boolean save1 = userRoleService.save(userRole);
        if (!save1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "增加用户失败");
        }
        return userService.getUserDetail(userID);
    }

    /**
     * 用户列表 - 用户管理
     *
     * @return
     */
    @GetMapping("/list")
    public List<UserDetailRes> getUserList() {
        return userService.getAllDetail();
    }

    @PostMapping("/updateAvatar")
    public String updateAvatar(@RequestPart("file") MultipartFile file,@RequestHeader("token") String token){
        String path = cosService.uploadFile(file, avatarPrefix);
        if (StringUtils.isBlank(path)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"文件上传失败");
        }
        // redis取信息
        Map<Object, Object> redisUserInfo = userRedisConstant.getRedisMapFromToken(token);
        // 获取基础User信息
        User user = (User) redisUserInfo.get(RedisConstant.UserInfo);
        user.setAvatar(path);
        boolean update = userService.updateById(user);
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"头像更新失败");
        }
        userRedisConstant.storeUserInfoRedis(user,token);
        return path;
    }
    @Transactional
    @PostMapping("/updateInfo")
    public boolean updateInfo(@RequestBody UserUpdateReq userUpdateReq,@RequestHeader("token") String token){
        Integer roleId = userUpdateReq.getRoleId();
        String userName = userUpdateReq.getUserName();
        String phoneNumber = userUpdateReq.getPhoneNumber();
        if (ObjectUtil.isNull(roleId)||ObjectUtil.isNull(userName)||ObjectUtil.isNull(phoneNumber)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"部分必需参数为空");
        }
        // redis取信息
        Map<Object, Object> redisUserInfo = userRedisConstant.getRedisMapFromToken(token);
        // 获取基础User信息
        User user = (User) redisUserInfo.get(RedisConstant.UserInfo);
        BeanUtils.copyProperties(userUpdateReq,user);
        boolean update = userService.updateById(user);
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新用户信息失败");
        }
        userRedisConstant.storeUserInfoRedis(user,token);
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UserID",user.getUserID());
        UserRole one = userRoleService.getOne(queryWrapper);
        if (ObjectUtil.isNotNull(one)){
            if (ObjectUtil.notEqual(one.getRoleID(),roleId)){
                boolean b = userRoleService.deleteByMultiId(one);
                if (!b){
                    throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新失败");
                }
                one.setUserID(user.getUserID());
                one.setRoleID(roleId);
                boolean save = userRoleService.saveOrUpdateByMultiId(one);
                if (!save){
                    throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新信息失败");
                }
            }
        }
        return true;
    }
    @PostMapping("/password/reset")
    public boolean reset(@RequestHeader(value = "token",required = false) String token){
        // redis取信息
        Map<Object, Object> redisUserInfo = userRedisConstant.getRedisMapFromToken(token);
        // 获取基础User信息
        User user = (User) redisUserInfo.get(RedisConstant.UserInfo);
        if (ObjectUtil.isEmpty(user)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
        }
        user.setPassword(defaultPassword);
        boolean update = userService.updateById(user);
        if (update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
        }
        userRedisConstant.storeUserInfoRedis(user,token);
        return true;
    }
}
