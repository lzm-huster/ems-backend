package com.ems.message.service;

import com.ems.message.model.entity.EmailMessage;

public interface EmailService {

    boolean sendEmailCaptcha(EmailMessage emailMessage);
}
