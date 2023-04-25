package com.ems.notice.quartz.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 通知任务表
 * @TableName TaskInfo
 */
@TableName(value ="TaskInfo")
@Data
public class TaskInfo implements Serializable {
    /**
     * 任务编号
     */
    @TableId(value = "TaskID", type = IdType.AUTO)
    private Integer taskID;

    /**
     * 任务组
     */
    @TableField(value = "TaskGroup")
    private String taskGroup;

    /**
     * 任务名称
     */
    @TableField(value = "TaskName")
    private String taskName;

    /**
     * 任务描述
     */
    @TableField(value = "TaskDescription")
    private String taskDescription;

    /**
     * 任务状态
     */
    @TableField(value = "TaskStatus")
    private Integer taskStatus;

    /**
     * 执行时间
     */
    @TableField(value = "TaskCron")
    private String taskCron;

    /**
     * 任务类名
     */
    @TableField(value = "TaskClass")
    private String taskClass;

    /**
     * 通知创建人
     */
    @TableField(value = "NoticeCreatorID")
    private Integer noticeCreatorID;

    /**
     * 通知接收人
     */
    @TableField(value = "NoticeReceiverID")
    private Integer noticeReceiverID;

    /**
     * 通知内容
     */
    @TableField(value = "NoticeInfo")
    private String noticeInfo;

    /**
     * 是否删除
     */
    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UpdateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}