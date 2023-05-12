package com.ems.notice.quartz.job;

import com.ems.notice.quartz.model.entity.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
public class EmailNoticeJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // 创建一个事件，下面仅创建一个输出语句作演示
        System.out.println(Thread.currentThread().getName() + "--"
                + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Object o = jobDataMap.get("TaskInfo");
        TaskInfo taskInfo = null;
        if (o instanceof TaskInfo){
            taskInfo = (TaskInfo) o;
        }
        if(!Objects.isNull(taskInfo)) {
            log.info(taskInfo.toString());
        }
    }
}
