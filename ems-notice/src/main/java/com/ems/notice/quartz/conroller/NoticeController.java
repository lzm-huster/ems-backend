package com.ems.notice.quartz.conroller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ems.annotation.ResponseResult;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.notice.quartz.constant.NoticeType;
import com.ems.notice.quartz.manager.WebSocketServer;
import com.ems.notice.quartz.model.entity.Notice;
import com.ems.notice.quartz.model.request.NoticeAddReq;
import com.ems.notice.quartz.service.NoticeService;
import com.ems.redis.constant.RedisConstant;
import com.ems.usercenter.constant.UserRedisConstant;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@ResponseResult
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private UserRedisConstant userRedisConstant;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List getNoticeList(@RequestHeader(value="token",required = false) String token){
        // redis取信息
        Map<Object, Object> redisUserInfo = userRedisConstant.getRedisMapFromToken(token);
        // 获取基础User信息
        User user = (User) redisUserInfo.get(RedisConstant.UserInfo);
        Integer userID = user.getUserID();
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ReceiverId",userID).or().eq("ReceiverId", NoticeType.ALL);
        List<Notice> noticeList = noticeService.list(queryWrapper);
        return noticeList;
    }
    @GetMapping("/detail")
    public Notice getNoticeDetail(Integer noticeId){
        if (ObjectUtil.isNull(noticeId)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空");
        }
        Notice byId = noticeService.getById(noticeId);
        if (ObjectUtil.isNull(byId)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"该通知不存在");
        }
        return byId;
    }
    @PostMapping("/add")
    public boolean addNotice(@RequestBody NoticeAddReq noticeAddReq,@RequestHeader(value = "token",required = false) String token) throws IOException {
        Integer receiverId = noticeAddReq.getReceiverId();
        String noticeInfo = noticeAddReq.getNoticeInfo();
        if (ObjectUtil.isNull(receiverId)||ObjectUtil.isNull(noticeInfo)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"部分参数为空");
        }
        if (!receiverId.equals(-1)){
            User userById = userService.getUserById(receiverId);
            if (ObjectUtil.isNull(userById)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接收者不存在");
            }
        }
        // redis取信息
        Map<Object, Object> redisUserInfo = userRedisConstant.getRedisMapFromToken(token);
        // 获取基础User信息
        User user = (User) redisUserInfo.get(RedisConstant.UserInfo);
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeAddReq,notice);
        notice.setCreatorId(user.getUserID());
        boolean b = WebSocketServer.sendMessage(noticeInfo, String.valueOf(receiverId));
        if (b){
            notice.setIsSend(1);
        }
        boolean save = noticeService.save(notice);
        if (!save){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"信息保存失败");
        }
        return true;
    }
}
