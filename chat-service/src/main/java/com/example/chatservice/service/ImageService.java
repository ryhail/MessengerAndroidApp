package com.example.chatservice.service;

import com.example.chatservice.DTO.ImageRequest;
import com.example.chatservice.DTO.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
public class ImageService {
    public ImageResponse uploadImage(String base64Image) {
        WebClient client = WebClient.create("http://localhost:8080");
        Mono<ImageResponse> response = client.post()
                .uri("/images/upload")
                .bodyValue(new ImageRequest(base64Image))
                .retrieve()
                .bodyToMono(ImageResponse.class);
        return response.block();
    }
}
