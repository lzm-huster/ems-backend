package com.ems.notice.quartz.conroller;

import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.vo.TaskInfoReq;
import com.ems.notice.quartz.service.TaskInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务管理
 **/
@RestController
@RequestMapping("/task")
public class TaskInfoController {

    @Autowired
    private TaskInfoService taskInfoService;

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
    public boolean add(@RequestBody TaskInfoReq taskInfoReq) {
//        return taskInfoService.addJob(taskInfoReq);
        return false;
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