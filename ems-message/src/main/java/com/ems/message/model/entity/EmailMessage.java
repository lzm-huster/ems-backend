package com.ems.message.model.entity;

import lombok.Data;

@Data
public class EmailMessage {
    private String receiver;
    private String content;
}
