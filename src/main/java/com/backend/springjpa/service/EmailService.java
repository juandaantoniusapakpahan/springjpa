package com.backend.springjpa.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerification(String to, String name, String token) {
        String link = "http://localhost:8080/auth/verify?token="+token;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Verify your Account");
            helper.setText("""
                     <h3>Hello %s 👋</h3>
                                    <p>Please verify your account by clicking the link below:</p>
                                    <a href="%s">Verify Account</a>
                                    <p>This link will expire in 15 minutes.</p>
                    """.formatted(name, link));

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send verification email", e);
        }
    }
}
