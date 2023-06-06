package com.ems.notice.quartz.model.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class NoticeAddReq {

    /**
     *
     */
    private Integer receiverId;

    /**
     *
     */
    private String noticeInfo;
}
