package com.example.messanger.DTO;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String device;

    public RegisterRequest(String username, String email, String password, String device) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.device = device;
    }
}
