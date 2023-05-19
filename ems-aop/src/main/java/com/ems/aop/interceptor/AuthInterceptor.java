package com.ems.aop.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ems.annotation.AuthCheck;
import com.ems.common.ErrorCode;
import com.ems.redis.constant.RedisConstant;
import com.ems.exception.BusinessException;
import com.ems.redis.RedisUtil;
import com.ems.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class AuthInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;
    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String[] mustAuths = authCheck.mustAuth();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取请求头中的token
        String token = request.getHeader("token");
        // 判空
        if(token == null){
            throw new BusinessException(ErrorCode.Header_Error,"请求头未包含token");
        }
        // 从token中获取userId
        String userId = jwtUtil.getUserIdFormToken(token);
        // 判断userId是否空
        if (userId == null){
            throw new BusinessException(ErrorCode.Header_Error,"token有误");
        }
        // 判断过期
        boolean isTokenExpired = jwtUtil.isTokenExpired(token);
        if (isTokenExpired){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"token已过期");
        }
        // 从redis中获取token
        String redisUserTokenKey = RedisConstant.UserToken;
        String redisUserKey = RedisConstant.UserPrefix + userId;
        String redisUserPermissionKey = RedisConstant.UserPermission;
        // 判断redis中token是否存在
        Map<Object, Object> userInfo = redisUtil.hmget(redisUserKey);
        if (ObjectUtil.isNull(userInfo)){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户登录信息已丢失");
        }
        // 判断token是否一致
        Object tokenObj = userInfo.get(RedisConstant.UserToken);
        if (ObjectUtil.isNull(tokenObj)){
            throw new BusinessException(ErrorCode.Header_Error,"token不正确");
        }
        // 从redis中获取权限列表
        Object userPermission = userInfo.get(RedisConstant.UserPermission);
        if (userPermission == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"无权限访问");
        }
        if (ArrayUtil.isEmpty(mustAuths)){
            return joinPoint.proceed();
        }
        // 判断是否有权限
        List userPermissionList = (List)userPermission;
        for (String auth: mustAuths) {
            if (!userPermissionList.contains(auth)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"无权限访问");
            }
        }
//        if (!userPermissionList.contains(mustAuth)){
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"无权限访问");
//        }
        // 放行
        return joinPoint.proceed();
    }
}
