package com.ems.notice.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ems.notice.quartz.model.entity.Notice;

import java.io.IOException;

/**
* @author 龙志明
* @description 针对表【Notice(通知信息)】的数据库操作Service
* @createDate 2023-06-04 19:25:27
*/
public interface NoticeService extends IService<Notice> {
    Notice addNotice(Notice notice) throws IOException;
    Notice addNoticeWithTask(Notice notice);
}
