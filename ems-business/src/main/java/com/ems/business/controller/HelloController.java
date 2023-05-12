package com.ems.business.controller;



import com.ems.annotation.AuthCheck;
import com.ems.annotation.ResponseResult;
import com.ems.common.ErrorCode;
import com.ems.exception.BusinessException;
//import com.ems.notice.quartz.job.EmailNoticeJob;
//import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@ResponseResult
@RestController
@RequestMapping("/hello")
public class HelloController {

//    @Autowired
//    private Scheduler scheduler;
//    @AuthCheck("")
    @GetMapping("/test")
    public int test() {
        System.out.println("test  test......");
        return 1;
    }

    @GetMapping("/error")
    public int error() {
        System.out.println("error  error......");
        throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
    }

//    @GetMapping("/startJob")
//    public void startJob() throws SchedulerException {
//        JobDetail EmailJob = JobBuilder.newJob(EmailNoticeJob.class)
//                .withIdentity("EmailJob")
//                .withDescription("任务描述：用于输出冬奥欢迎语")
//                .build();
//        SimpleTrigger trigger = TriggerBuilder.newTrigger().forJob(EmailJob)
//                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
//                .build();
//        scheduler.scheduleJob(EmailJob,trigger);
//        scheduler.start();
//    }


}
