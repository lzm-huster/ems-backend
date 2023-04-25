package com.ems.notice.quartz.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NoticeHandlerFactory {
    @Autowired
    private final Map<String, NoticeHandlerStrategy> strategyMap = new ConcurrentHashMap<>();

    public NoticeHandlerFactory(Map<String, NoticeHandlerStrategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v)-> this.strategyMap.put(k, v));
    }
    public String getNoticeClazz(String noticeType){
        String clazzName = strategyMap.get(noticeType).handlerNotice();
        return clazzName;
    }
}
