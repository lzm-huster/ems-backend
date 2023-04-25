package com.ems.notice.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.notice.quartz.model.entity.TaskInfo;
import com.ems.notice.quartz.model.vo.TaskInfoReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 龙志明
* @description 针对表【TaskInfo(通知任务表)】的数据库操作Mapper
* @createDate 2023-04-24 09:50:04
* @Entity generator.domain.TaskInfo
*/
@Mapper
public interface TaskInfoMapper extends BaseMapper<TaskInfo> {
//    TaskInfo selectByJobName(String jobName);
//    List<TaskInfo> selectAll();
//    List<TaskInfo> selectTaskInfos(TaskInfoReq taskInfo);
//    int deleteByPrimaryKey(Integer id);
//    int insertSelective(TaskInfo record);
//    TaskInfo selectByPrimaryKey(Integer id);
//    int updateByPrimaryKeySelective(TaskInfo record);
}




