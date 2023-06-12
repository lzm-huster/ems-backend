package com.ems.notice.quartz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
import com.ems.notice.quartz.constant.TaskUtil;
import com.ems.notice.quartz.manager.TaskManager;
import com.ems.notice.quartz.mapper.NoticeMapper;
import com.ems.notice.quartz.mapper.TaskInfoMapper;
import com.ems.notice.quartz.model.entity.Notice;
import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.response.TaskInfoRes;
import com.ems.notice.quartz.service.NoticeService;
import com.ems.notice.quartz.service.TaskInfoService;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.service.UserService;
//import com.sun.corba.se.impl.orbutil.ObjectStreamClassUtil_1_3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TaskInfoServiceImpl implements TaskInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskInfoServiceImpl.class);

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private TaskUtil taskUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;

    //    @Override
//    public PageInfo<TaskInfo> selectTaskListByPage(TaskInfoReq taskInfoReq) {
//        PageHelper.startPage(taskInfoReq.getPageCurrent(), taskInfoReq.getPageSize());
//        List<TaskInfo> list = taskInfoMapper.selectTaskInfos(taskInfoReq);
//        PageInfo<TaskInfo> pageInfo = new PageInfo<>(list);
//        return pageInfo;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean updateJob(TaskInfoReq taskInfoReq) {
//        if (!CronExpression.isValidExpression(taskInfoReq.getTaskCron())) {
//            LOGGER.error("更新任务失败，表达式有误：{}", taskInfoReq.getTaskId());
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"更新任务失败，表达式有误");
//        }
//        TaskInfo isExistData = taskInfoMapper.selectByJobName(taskInfoReq.getTaskName());
//        //当任务不存在，则更改失败
//        if ((!Objects.isNull(isExistData)) && (!isExistData.getTaskID().equals(taskInfoReq.getTaskId()))) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"更新任务失败，该任务已存在");
//        }
//        TaskInfo data = taskInfoMapper.selectByPrimaryKey(taskInfoReq.getTaskId());
//        if (data == null) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"更新任务失败，该任务不存在");
//        }
//
//        BeanUtils.copyProperties(taskInfoReq, data);
//        data.setUpdateTime(new Date());
//        taskInfoMapper.updateByPrimaryKeySelective(data);
//
//        if (!taskManager.updateJob(data)) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新任务失败，出现未知异常");
//        }
//        return true;
//    }
//
//    @Override
//    public boolean pauseJob(Integer taskId) {
//        TaskInfo data = taskInfoMapper.selectByPrimaryKey(taskId);
//        if (data == null) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,"暂停任务失败，该任务不存在");
//        }
//        if (!taskManager.pauseJob(data)) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,"暂停任务失败，出现未知异常");
//        }
//        data.setTaskStatus(TaskStatus.STOP.getCode());
//        taskInfoMapper.updateByPrimaryKeySelective(data);
//        return true;
//    }
//
//    @Override
//    public boolean resumeJob(Integer taskId) {
//        TaskInfo data = taskInfoMapper.selectByPrimaryKey(taskId);
//        if (data == null) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,"恢复任务失败，该任务不存在");
//        }
//        if (!taskManager.resumeJob(data)) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,"恢复任务失败，出现未知异常");
//        }
//        data.setTaskStatus(TaskStatus.START.getCode());
//        taskInfoMapper.updateByPrimaryKeySelective(data);
//        return true;
//    }
//
    @Transactional
    @Override
    public TaskInfoRes addJob(TaskInfo taskInfo) {
        QueryWrapper<TaskInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("TaskName", taskInfo.getTaskName());
        int res = 0;
        //当任务不存在，则返回成功插入
        if (!Objects.isNull(taskInfoMapper.selectOne(queryWrapper))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "增加任务失败，该任务已存在");
        }
        res =taskInfoMapper.insert(taskInfo);
        if (res == 0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据库插入任务失败，请重新添加");
        }
        if (!taskManager.addJob(taskInfo)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据库增加成功，但任务调度增加失败，请刷新后重启任务");
        }
        Integer noticeId = taskInfo.getNoticeId();
        Notice notice = noticeService.getById(noticeId);
        if (ObjectUtil.isNull(notice)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"通知内容不存在");
        }
        User creator = userService.getUserById(notice.getCreatorId());
        User receiver = userService.getUserById(notice.getReceiverId());
        TaskInfoRes taskInfoRes = taskUtil.taskInfoRes(taskInfo, creator.getUserName(), receiver.getUserName());
        return taskInfoRes;
    }

    //    @Override
//    public Result delete(TaskInfoReq reqVo) {
//        try {
//            //TODO 删除任务只是做了暂停，如果是 Quartz Jdbc 模式下添加重复任务可能加不进去，并没有真正删除(可自行调整)
//            Result result = this.pauseJob(reqVo.getId());
//            //只有暂停成功的任务才能删除
//            if (CodeMsg.SUCCESS == result.getCode()) {
//                taskInfoMapper.deleteByPrimaryKey(reqVo.getId());
//                return ResponseFactory.build();
//            } else {
//                return ResponseFactory.build(CodeMsg.TASK_EXCEPTION);
//            }
//        } catch (Exception e) {
//            return ResponseFactory.build(CodeMsg.TASK_EXCEPTION);
//        }
//    }
//
    @Override
    public List<TaskInfo> selectTasks() {
        return taskInfoMapper.selectList(new QueryWrapper<>());
    }


}
