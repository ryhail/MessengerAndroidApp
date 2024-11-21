package com.example.chatservice.service;

import com.example.chatservice.DTO.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
public class NotificationService {
    public void notify(NotificationRequest request) {
        WebClient client = WebClient.create("http://localhost:8080");
        Mono<Void> response = client.post()
                .uri("/notify/new-message")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.TYPE);
        response.block();
    }
}
