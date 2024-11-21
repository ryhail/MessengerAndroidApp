package com.example.messanger.DTO;

public class LoginRequest {
    private String username;
    private String password;
    private String device;

    public LoginRequest(String username, String password, String device) {
        this.username = username;
        this.password = password;
        this.device = device;
    }
}