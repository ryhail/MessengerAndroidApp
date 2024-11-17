package com.example.auth.service;

import com.example.auth.DTO.ImageResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageService {
    private final Path fileStorageLocation = Paths.get("images");

    public ImageResponse uploadImage(String imageBase64) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            Path filePath = Paths.get("images/" + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, imageBytes);

            return new ImageResponse("/images/" + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ResponseEntity<Resource> getImage(String filename) {
        try {
            Path imagePath = Paths.get("images", filename);
            File imageFile = imagePath.toFile();

            if (!imageFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            byte[] imageBytes = Files.readAllBytes(imagePath);
            ByteArrayResource resource = new ByteArrayResource(imageBytes);

            MediaType contentType = getImageContentType(filename);

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private MediaType getImageContentType(String imageName) {
        if (imageName.toLowerCase().endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (imageName.toLowerCase().endsWith(".jpg") || imageName.toLowerCase().endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (imageName.toLowerCase().endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        }
        return MediaType.IMAGE_JPEG;
    }
}
