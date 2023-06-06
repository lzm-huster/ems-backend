package com.ems.notice.quartz.conroller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.annotation.ResponseResult;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.notice.quartz.constant.TaskStatus;
import com.ems.notice.quartz.constant.TaskUtil;
import com.ems.notice.quartz.model.entity.Notice;
import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.request.TaskAddReq;
import com.ems.notice.quartz.model.request.TaskInfoReq;
import com.ems.notice.quartz.model.response.TaskInfoRes;
import com.ems.notice.quartz.service.NoticeService;
import com.ems.notice.quartz.service.TaskInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

/**
 * 定时任务管理
 **/
@ResponseResult
@RestController
@RequestMapping("/task")
public class TaskInfoController {

    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private TaskUtil taskUtil;
    @Autowired
    private NoticeService noticeService;

    /**
     * 定时器列表
     */
    @PostMapping("/list")
    public PageInfo<TaskInfo> list(@RequestBody TaskInfoReq reqVo) {
//        return taskInfoService.selectTaskListByPage(reqVo);
        return null;
    }

    /**
     * 定时器修改
     */
    @PostMapping("/edit")
    public boolean edit(@RequestBody TaskInfoReq reqVo) {

//        return taskInfoService.updateJob(reqVo);
        return false;
    }


    /**
     * 暂停任务
     */
    @PostMapping("/pause")
    public boolean pause(Integer taskId) {

//        return taskInfoService.pauseJob(taskId);
        return false;
    }


    /**
     * 增加任务
     */
    @PostMapping("/add")
    public TaskInfoRes add(@RequestBody TaskAddReq taskAddReq) {
        if (StringUtils.isBlank(taskAddReq.getTaskName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务名称为空");
        }
        Date date = taskAddReq.getTaskDate();
        if (date == null || date.before(new Date())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务定时不合法为空");
        }
        if(Objects.isNull(taskAddReq.getTaskStatus())){
            taskAddReq.setTaskStatus(TaskStatus.START.getCode());
        }
        Notice notice = new Notice();
        BeanUtils.copyProperties(taskAddReq,notice);
        boolean save = noticeService.save(notice);
        if (save){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"通知保存失败");
        }
        TaskInfo taskInfo = taskUtil.taskAddInfo(taskAddReq);
        taskInfo.setNoticeId(notice.getNoticeId());
        return taskInfoService.addJob(taskInfo);
    }


    /**
     * 恢复任务
     */
    @PostMapping("/resume")
    public boolean resume(Integer taskId) {

//        return taskInfoService.resumeJob(taskId);
        return false;
    }

//    /**删除任务*/
//    @PostMapping("/del")
//    public Result delete(@RequestBody TaskInfoReq reqVo) {
//        return taskInfoService.delete(reqVo);
//    }
}