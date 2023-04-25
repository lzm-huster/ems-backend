package com.ems.notice.quartz.manager;

import com.ems.notice.quartz.strategy.NoticeHandlerFactory;
import com.ems.utils.SpringContextUtil;
import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.vo.TaskInfoReq;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);
    public static final String JOB_DEFAULT_GROUP_NAME = "JOB_DEFAULT_GROUP_NAME";
    public static final String TRIGGER_DEFAULT_GROUP_NAME = "TRIGGER_DEFAULT_GROUP_NAME";

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SpringContextUtil springContextUtils;
    @Autowired
    private NoticeHandlerFactory noticeHandlerFactory;

    /**
     * 添加任务
     */
    public boolean addJob(TaskInfoReq taskInfo) {
        boolean flag = true;
        if (!CronExpression.isValidExpression(taskInfo.getTaskCron())) {
            LOGGER.error("定时任务表达式有误：{}", taskInfo.getTaskCron());
            return false;
        }
        try {
            String className = noticeHandlerFactory.getNoticeClazz(taskInfo.getTaskType());
//            String className = springContextUtils.getBean(taskInfo.getTaskName()).getClass().getName();
            JobDetail jobDetail = JobBuilder.newJob().withIdentity(new JobKey(taskInfo.getTaskName(), JOB_DEFAULT_GROUP_NAME))
                    .ofType((Class<Job>) Class.forName(className))
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(taskInfo.getTaskCron()))
                    .withIdentity(new TriggerKey(taskInfo.getTaskName(), TRIGGER_DEFAULT_GROUP_NAME))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (Exception e) {
            LOGGER.error("添加定时任务异常：{}", e.getMessage(), e);
            flag = false;
        }
        return flag;
    }

    /**
     * 更新任务
     */
    public boolean updateJob(TaskInfo taskInfo) {
        boolean flag = true;
        try {
            JobKey jobKey = new JobKey(taskInfo.getTaskName(), JOB_DEFAULT_GROUP_NAME);
            TriggerKey triggerKey = new TriggerKey(taskInfo.getTaskName(), TRIGGER_DEFAULT_GROUP_NAME);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
                Trigger newTrigger = TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .withSchedule(CronScheduleBuilder.cronSchedule(taskInfo.getTaskCron()))
                        .withIdentity(triggerKey)
                        .build();
                scheduler.rescheduleJob(triggerKey, newTrigger);
            } else {
                LOGGER.info("更新任务失败，任务不存在，任务名称：{}，表达式：{}", taskInfo.getTaskName(), taskInfo.getTaskCron());
            }
            LOGGER.info("更新任务成功，任务名称：{}，表达式：{}", taskInfo.getTaskName(), taskInfo.getTaskCron());
        } catch (SchedulerException e) {
            LOGGER.error("更新定时任务失败:{}", e.getMessage(), e);
            flag = false;
        }
        return flag;
    }

    /**
     * 暂停任务
     */
    public boolean pauseJob(TaskInfo taskInfo) {
        try {
            scheduler.pauseJob(JobKey.jobKey(taskInfo.getTaskName(), JOB_DEFAULT_GROUP_NAME));
            LOGGER.info("任务暂停成功：{}", taskInfo.getTaskID());
            return true;
        } catch (SchedulerException e) {
            LOGGER.error("暂停定时任务失败:{}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 恢复任务
     */
    public boolean resumeJob(TaskInfo taskInfo) {
        try {
            scheduler.resumeJob(JobKey.jobKey(taskInfo.getTaskName(), JOB_DEFAULT_GROUP_NAME));
            LOGGER.info("任务恢复成功：{}", taskInfo.getTaskID());
            return true;
        } catch (SchedulerException e) {
            LOGGER.error("恢复定时任务失败:{}", e.getMessage(), e);
            return false;
        }
    }
}