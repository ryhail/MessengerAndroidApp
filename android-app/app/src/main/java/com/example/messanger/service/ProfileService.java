package com.example.messanger.service;

import com.example.messanger.model.Chat;
import com.example.messanger.model.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProfileService {
    @GET("/profiles/{userId}")
    Call<Profile> getProfile(@Path("userId") Long userId);
}
