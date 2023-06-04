package com.ems.message.service.impl;

import com.ems.message.model.entity.EmailMessage;
import com.ems.message.service.EmailService;
import com.ems.utils.VerCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceImpl implements EmailService {
    //	引入邮件接口
    @Autowired
    private JavaMailSender mailSender;
    @Value("${email.username}")
    private String from;
    @Override
    public boolean sendEmailCaptcha(EmailMessage emailMessage) {
        //        创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);

        message.setTo(emailMessage.getTos());

        message.setSubject("您本次的验证码是");

        String verCode = VerCodeGenerateUtil.generateVerCode();

        message.setText("\n本次请求的邮件验证码为:" + verCode + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

        mailSender.send(message);
        return true;
    }
}
