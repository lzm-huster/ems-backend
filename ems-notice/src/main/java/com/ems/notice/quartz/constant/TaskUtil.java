package com.ems.notice.quartz.constant;

import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.request.TaskAddReq;
import com.ems.notice.quartz.model.response.TaskInfoRes;
import com.ems.notice.quartz.strategy.NoticeHandlerFactory;
import com.ems.utils.CronUtil;
import com.ems.utils.SpringContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskUtil {
    @Autowired
    private CronUtil cronUtil;
    @Autowired
    private NoticeHandlerFactory noticeHandlerFactory;
    @Autowired
    private SpringContextUtil springContextUtil;
    public TaskInfo taskAddInfo(TaskAddReq taskAddReq) {
        TaskInfo taskInfo = new TaskInfo();
        BeanUtils.copyProperties(taskAddReq, taskInfo);
        taskInfo.setTaskGroup(DefaultName.JOB_DEFAULT_GROUP_NAME);
        taskInfo.setTaskCron(cronUtil.DateToCron(taskAddReq.getTaskDate()));
        taskInfo.setTaskClass(noticeHandlerFactory.getNoticeClazz(taskAddReq.getTaskType()));
        return taskInfo;
    }
    public TaskInfoRes taskInfoRes(TaskInfo taskInfo, String creator, String receiver) {
        TaskInfoRes taskInfoRes = new TaskInfoRes();
        BeanUtils.copyProperties(taskInfo, taskInfoRes);
        String[] types = springContextUtil.getName(taskInfo.getTaskClass());
        if (types.length > 0 ){
            taskInfoRes.setTaskType(types[0]);
        }
        taskInfoRes.setNoticeCreatorName(creator);
        taskInfoRes.setNoticeReceiverName(receiver);
        taskInfoRes.setTaskDate(cronUtil.CronToDate(taskInfo.getTaskCron()));
        return taskInfoRes;
    }
}
