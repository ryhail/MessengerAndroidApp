package com.example.messanger.service;

import com.example.messanger.DTO.ImageRequest;
import com.example.messanger.model.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProfileService {
    @GET("/profiles/{userId}")
    Call<Profile> getProfile(@Path("userId") Long userId);
    @GET("/profiles/search/{arg}")
    Call<List<Profile>> searchProfiles(@Path("arg") String arg);
    @POST("/profiles/update-pfp")
    Call<Profile> updatePfp(@Body ImageRequest image);
    @POST("/profiles/update")
    Call<Profile> update(@Body Profile newProfile);
}
