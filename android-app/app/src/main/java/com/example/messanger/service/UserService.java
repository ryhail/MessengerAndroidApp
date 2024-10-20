package com.example.messanger.service;

import com.example.messanger.DTO.UserDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("/user")
    Call<UserDataResponse> userDataRequest();
}
