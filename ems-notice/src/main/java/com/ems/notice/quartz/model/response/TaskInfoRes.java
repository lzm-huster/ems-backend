package com.ems.notice.quartz.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class TaskInfoRes {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务时间表达式
     */
    private Date taskDate;
    /**
     * 任务状态
     */
    private Integer taskStatus;
    /**
     * 任务描述
     */
    private String taskDescription;
    /**
     * 通知创建人
     */
    private String noticeCreatorName;
    /**
     * 通知接收人
     */
    private String noticeReceiverName;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     * 通知内容
     */
    private String noticeInfo;
}
