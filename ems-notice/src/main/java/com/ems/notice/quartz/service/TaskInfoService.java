package com.ems.notice.quartz.service;

import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.request.TaskAddReq;
import com.ems.notice.quartz.model.request.TaskInfoReq;
import com.ems.notice.quartz.model.response.TaskInfoRes;

import java.util.List;

/**
 * 定时任务接口
 **/
public interface TaskInfoService {
//    /**
//     * 获取任务列表分页
//     * */
//    PageInfo<TaskInfo> selectTaskListByPage(TaskInfoReq taskInfoReq);
    /**
     * 添加定时任务
     * */
    TaskInfoRes addJob(TaskInfo taskInfo);

//    /**
//     * 更新任务
//     * */
//    boolean updateJob(TaskInfoReq taskInfoReq);
//    /**
//     * 暂停任务
//     * */
//    boolean pauseJob(Integer taskId);
//    /**
//     * 恢复任务
//     * */
//    boolean resumeJob(Integer taskId);
    /**
     * 获取所有任务
     * */
    List<TaskInfo> selectTasks();
    /**删除任务*/
//    Result delete(TaskInfoReq reqVo);
}