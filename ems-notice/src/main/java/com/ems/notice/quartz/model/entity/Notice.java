package com.ems.notice.quartz.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知信息
 * @TableName Notice
 */
@TableName(value ="Notice")
@Data
public class Notice implements Serializable {
    /**
     * 
     */
    @TableId(value = "NoticeId", type = IdType.AUTO)
    private Integer noticeId;

    /**
     * 
     */
    @TableField(value = "CreatorId")
    private Integer creatorId;

    /**
     * 
     */
    @TableField(value = "ReceiverId")
    private Integer receiverId;

    /**
     * 
     */
    @TableField(value = "NoticeInfo")
    private String noticeInfo;

    /**
     * 
     */
    @TableField(value = "IsSend")
    private Integer isSend;

    /**
     * 
     */
    @TableField(value = "IsRead")
    private Integer isRead;

    /**
     * 
     */
    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    /**
     * 
     */
    @TableField(value = "CreateTime")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "UpdateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}