package com.ems.notice.quartz.strategy.impl;

import com.ems.notice.quartz.job.EmailNoticeJob;
import com.ems.notice.quartz.strategy.NoticeHandlerStrategy;
import org.springframework.stereotype.Component;

@Component("EmailNotice")
public class EmailNotice implements NoticeHandlerStrategy {
    @Override
    public String handlerNotice() {
        return EmailNoticeJob.class.getName();
    }
}
