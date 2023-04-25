package com.ems.notice.quartz.strategy.impl;

import com.ems.notice.quartz.job.PhoneNoticeJob;
import com.ems.notice.quartz.strategy.NoticeHandlerStrategy;
import org.springframework.stereotype.Component;

@Component("PhoneNotice")
public class PhoneNotice implements NoticeHandlerStrategy {
    @Override
    public String handlerNotice() {
        return PhoneNoticeJob.class.getName();
    }
}
