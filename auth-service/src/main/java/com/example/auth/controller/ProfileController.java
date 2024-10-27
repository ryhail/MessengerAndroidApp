package com.example.auth.controller;

import com.example.auth.DTO.ProfileData;
import com.example.auth.model.Profile;
import com.example.auth.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService service;

    @GetMapping("/{userId}")
    public ProfileData getProfileById(@PathVariable Long userId) {
        return service.getProfileById(userId);
    }
}
