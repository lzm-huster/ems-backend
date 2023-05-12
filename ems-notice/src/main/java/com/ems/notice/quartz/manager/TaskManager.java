package com.ems.notice.quartz.manager;

import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.notice.quartz.constant.DefaultName;
import com.ems.notice.quartz.model.request.TaskAddReq;
import com.ems.notice.quartz.strategy.NoticeHandlerFactory;
import com.ems.utils.CronUtil;
import com.ems.utils.SpringContextUtil;
import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.request.TaskInfoReq;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);


    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SpringContextUtil springContextUtils;
    @Autowired
    private NoticeHandlerFactory noticeHandlerFactory;
    @Autowired
    private CronUtil cronUtil;

    /**
     * 添加任务
     */
    public boolean addJob(TaskInfo taskInfo) {
        boolean flag = true;
        String cron = taskInfo.getTaskCron();
        if (!CronExpression.isValidExpression(cron)) {
            LOGGER.error("定时任务表达式有误：{}", cron);
            return false;
        }
        try {
            String className = taskInfo.getTaskClass();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("TaskInfo",taskInfo);
            JobDetail jobDetail = JobBuilder.newJob().withIdentity(new JobKey(taskInfo.getTaskName(), DefaultName.JOB_DEFAULT_GROUP_NAME))
                    .ofType((Class<Job>) Class.forName(className))
                    .usingJobData(jobDataMap)
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .withIdentity(new TriggerKey(taskInfo.getTaskName(), DefaultName.TRIGGER_DEFAULT_GROUP_NAME))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        }
        catch (SchedulerException e){
            LOGGER.error("设置时间不合法：{}", e.getMessage());
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,"设置时间不合法");
        }
        catch (Exception e) {
            LOGGER.error("添加定时任务异常：{}", e.getMessage());
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,e.getMessage());
        }
        return flag;
    }

    /**
     * 更新任务
     */
    public boolean updateJob(TaskInfo taskInfo) {
        boolean flag = true;
        try {
            JobKey jobKey = new JobKey(taskInfo.getTaskName(), DefaultName.JOB_DEFAULT_GROUP_NAME);
            TriggerKey triggerKey = new TriggerKey(taskInfo.getTaskName(), DefaultName.TRIGGER_DEFAULT_GROUP_NAME);
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
            scheduler.pauseJob(JobKey.jobKey(taskInfo.getTaskName(), DefaultName.JOB_DEFAULT_GROUP_NAME));
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
            scheduler.resumeJob(JobKey.jobKey(taskInfo.getTaskName(), DefaultName.JOB_DEFAULT_GROUP_NAME));
            LOGGER.info("任务恢复成功：{}", taskInfo.getTaskID());
            return true;
        } catch (SchedulerException e) {
            LOGGER.error("恢复定时任务失败:{}", e.getMessage(), e);
            return false;
        }
    }
}