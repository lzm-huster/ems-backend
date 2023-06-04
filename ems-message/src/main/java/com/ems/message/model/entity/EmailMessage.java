package com.ems.message.model.entity;

import lombok.Data;

@Data
public class EmailMessage {
    //    邮件接收方
    private String[] tos;
    //    邮件主题
    private String subject;
    //    邮件内容
    private String content;
}
