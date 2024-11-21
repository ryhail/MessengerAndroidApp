package com.example.auth.controller;

import com.example.auth.DTO.NotificationRequest;
import com.example.auth.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/notify")
public class NotificationController {
    private final NotificationService service;
    @PostMapping("/new-message")
    public void sendNotification(@RequestBody NotificationRequest request) {
        service.sendNotification(request);
    }
}
