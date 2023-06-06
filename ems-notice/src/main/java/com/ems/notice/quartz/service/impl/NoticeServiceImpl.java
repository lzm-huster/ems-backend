package com.ems.notice.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.notice.quartz.manager.WebSocketServer;
import com.ems.notice.quartz.mapper.NoticeMapper;
import com.ems.notice.quartz.model.entity.Notice;
import com.ems.notice.quartz.service.NoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
* @author 龙志明
* @description 针对表【Notice(通知信息)】的数据库操作Service实现
* @createDate 2023-06-04 19:25:27
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Override
    public Notice addNotice(Notice notice) throws IOException {
        boolean b = WebSocketServer.sendMessage(notice.getNoticeInfo(), String.valueOf(notice.getReceiverId()));
        if (b){
            notice.setIsSend(1);
        }
        boolean save = this.save(notice);
        if (!save){
            return null;
        }
        return notice;
    }
}




