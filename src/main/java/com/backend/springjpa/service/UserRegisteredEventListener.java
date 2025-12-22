package com.backend.springjpa.service;

import com.backend.springjpa.dto.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableAsync
public class UserRegisteredEventListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handle(UserRegisteredEvent event) {
        emailService.sendVerification(
                event.getEmail(),
                event.getName(),
                event.getToken()
        );
    }
}
