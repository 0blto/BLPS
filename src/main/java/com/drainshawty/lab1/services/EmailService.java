package com.drainshawty.lab1.services;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component public class EmailService {

    @Value("${spring.mail.username}") String from;
    final JavaMailSender sender;

    @Autowired
    public EmailService(JavaMailSender sender) { this.sender = sender; }

    public void send(String to, String subject, String body) {
        this.sender.send(msg -> {
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setFrom(from);
            msg.setSubject(subject);
            msg.setText(body);
        });
    }
}
