package com.example.messanger.DTO;

import com.example.messanger.model.User;

public class UserDataResponse {
    private Long id;
    private String username;
    private String email;

    public UserDataResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public User getUser() {
        return new User(id, username, email);
    }
}
