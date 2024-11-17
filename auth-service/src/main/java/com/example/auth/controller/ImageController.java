package com.example.auth.controller;

import com.example.auth.DTO.ImageRequest;
import com.example.auth.DTO.ImageResponse;
import com.example.auth.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService service;
    @PostMapping("/upload")
    public ImageResponse upload(@RequestBody ImageRequest request) {
        return service.uploadImage(request.getImage().replaceAll("\n", ""));
    }
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        return service.getImage(filename);
    }
}
