package com.ems.notice.quartz.other;

import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.Map;

public interface QuartzService {

    /**
     * 增加一个任务job
     * @param jobClass  任务job实现类
     * @param jobName   任务job名称（保证唯一性）
     * @param jobGroupName  任务job组名
     * @param jobTime   任务时间间隔（秒）
     * @param jobTimes  任务运行次数（若<0，则不限次数）
     * @param jobData   任务参数
     */
    void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, int jobTime,
                int jobTimes, Map jobData);

    /**
     * 增加一个任务job
     * @param jobClass  任务job实现类
     * @param jobName   任务job名称（保证唯一性）
     * @param jobGroupName  任务job组名
     * @param jobTime   任务时间表达式
     * @param jobData   任务参数
     */
    void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobTime, Map jobData);

    /**
     * 修改一个任务job
     * @param jobName 任务名称
     * @param jobGroupName  任务组名
     * @param jobTime   cron时间表达式
     */
    void updateJob(String jobName, String jobGroupName, String jobTime);

    /**
     * 删除一个任务job
     * @param jobName
     * @param jobGroupName
     */
    void deleteJob(String jobName, String jobGroupName);

    /**
     * 暂停一个任务job
     * @param jobName
     * @param jobGroupName
     */
    void pauseJob(String jobName, String jobGroupName);

    /**
     * 恢复一个任务job
     * @param jobName
     * @param jobGroupName
     */
    void resumeJob(String jobName, String jobGroupName);

    /**
     * 立即执行一个任务job
     * @param jobName
     * @param jobGroupName
     */
    void runAJobNow(String jobName, String jobGroupName);

    /**
     * 获取所有任务job
     * @return
     */
    List<Map<String, Object>> queryAllJob();

    /**
     * 获取正在运行的任务job
     * @return
     */
    List<Map<String, Object>> queryRunJob();


}
