package com.ems.notice.quartz.model.request;

import lombok.Data;

@Data
public class TaskInfoReq {
    /**
     * 任务编号
     */
    private Integer taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务时间表达式
     */
    private String taskCron;

    /**
     * 任务状态
     */
    private String taskStatus;
    /**
     * 通知创建人
     */
    private Integer noticeCreatorID;
    /**
     * 通知接收人
     */
    private Integer noticeReceiverID;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 每页显示条数
     */
    private int pageSize=10;
    /**
     * 当前页数
     */
    private int pageCurrent=1;
}