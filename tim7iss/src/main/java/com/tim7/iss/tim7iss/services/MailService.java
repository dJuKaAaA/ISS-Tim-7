package com.tim7.iss.tim7iss.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService {


    @Autowired
    private JavaMailSender mailSender;

    public void sendTextEmail(String toEmail, String subject, String emailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ubert472@gmail.com"); // TODO NE DIRATI OVO POLJE
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(emailContent);

        mailSender.send(message);

    }
}
