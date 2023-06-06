package com.ems.notice.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ems.notice.quartz.mapper.NoticeMapper;
import com.ems.notice.quartz.model.entity.Notice;
import com.ems.notice.quartz.service.NoticeService;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【Notice(通知信息)】的数据库操作Service实现
* @createDate 2023-06-04 19:25:27
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService {

}




