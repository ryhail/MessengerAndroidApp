package com.example.auth.controller;


import com.example.auth.DTO.UserData;
import com.example.auth.model.User;
import com.example.auth.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserData getUserData() {
        return userService.getCurrentUserData();
    }
}
