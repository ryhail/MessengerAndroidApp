package com.example.messanger.service;
import com.example.messanger.DTO.LoginRequest;
import com.example.messanger.DTO.LoginResponse;
import com.example.messanger.DTO.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/auth/sign-in")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("/auth/sign-up")
    Call<LoginResponse> register(@Body RegisterRequest registerRequest);
}