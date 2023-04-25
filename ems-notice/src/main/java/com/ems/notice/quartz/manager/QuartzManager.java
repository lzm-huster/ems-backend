package com.ems.notice.quartz.manager;

import com.ems.notice.quartz.constant.TaskStatus;
import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.vo.TaskInfoReq;
import com.ems.notice.quartz.service.TaskInfoService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class QuartzManager {

    private Logger logger = LoggerFactory.getLogger(QuartzManager.class);

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SpringJobFactory springJobFactory;
    @Autowired
    private TaskInfoService taskInfoService;

    @PostConstruct
    public void start() {
        //启动所有任务
        try {
            scheduler.setJobFactory(springJobFactory);
            // scheduler.clear();
            List<TaskInfo> tasks = taskInfoService.selectTasks();
            for (TaskInfo taskInfo : tasks) {
                if (TaskStatus.START.getCode().equals(taskInfo.getTaskStatus()) && !StringUtils.isEmpty(taskInfo.getTaskCron())) {
                    TaskInfoReq data=new TaskInfoReq();
                    BeanUtils.copyProperties(taskInfo,data);
                    taskInfoService.addJob(data);
                }
            }
            logger.info("定时任务启动完成");
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("定时任务初始化失败");
        }
    }
}