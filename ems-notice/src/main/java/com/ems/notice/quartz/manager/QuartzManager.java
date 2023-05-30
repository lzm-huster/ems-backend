package com.ems.notice.quartz.manager;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.notice.quartz.constant.TaskStatus;
import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.service.TaskInfoService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;
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
    @Autowired
    private TaskManager taskManager;

    @PostConstruct
    public void start() {
        //启动所有任务
        try {
            scheduler.setJobFactory(springJobFactory);
            // scheduler.clear();
            List<TaskInfo> tasks = taskInfoService.selectTasks();
            for (TaskInfo taskInfo : tasks) {
                if (TaskStatus.START.getCode().equals(taskInfo.getTaskStatus())) {
                    break;
                }
                String cron = taskInfo.getTaskCron();
                if (StringUtils.isBlank(cron)) {
                    break;
                }
                CronTriggerImpl triggerImpl = new CronTriggerImpl();
                triggerImpl.setCronExpression(cron);
                Date date = triggerImpl.computeFirstFireTime(null);
                if (ObjectUtil.isNotNull(date) && date.after(new Date())) {
                    taskManager.addJob(taskInfo);
                }
            }
            logger.info("定时任务启动完成");
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("定时任务初始化失败");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}