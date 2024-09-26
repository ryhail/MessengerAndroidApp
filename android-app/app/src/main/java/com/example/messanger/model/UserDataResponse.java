package com.example.messanger.model;

import com.example.messanger.User;

public class UserDataResponse {
    private String username;
    private String password;
    private String email;

    public UserDataResponse(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User getUser() {
        return new User(username, password, email);
    }
}
