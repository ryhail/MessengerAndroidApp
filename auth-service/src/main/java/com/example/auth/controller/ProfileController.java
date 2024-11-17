package com.example.auth.controller;

import com.example.auth.DTO.ImageRequest;
import com.example.auth.DTO.ImageResponse;
import com.example.auth.DTO.ProfileData;
import com.example.auth.model.Profile;
import com.example.auth.service.ProfileService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService service;
    private final ImageController image;

    @GetMapping("/{userId}")
    public ProfileData getProfileById(@PathVariable Long userId) {
        return service.getProfileById(userId);
    }
    @GetMapping("/search/{arg}")
    public List<ProfileData> searchProfiles(@PathVariable String arg) {
        return service.findProfiles(arg);
    }
    @PostMapping("/update-pfp")
    public ProfileData updatePfp(@RequestBody ImageRequest request) {
        ImageResponse url = image.upload(request);
        return service.updateProfilePicture(url.getUrl());
    }
    @PostMapping("/update")
    public ProfileData update(@RequestBody ProfileData request) {
        return service.updateProfile(request);
    }
}
