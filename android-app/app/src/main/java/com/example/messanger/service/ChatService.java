package com.example.messanger.service;

import com.example.messanger.model.Chat;
import com.example.messanger.model.Message;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ChatService {
    @GET("/chats/{userId}")
    Call<List<Chat>> getChats(@Path("userId") Long userId);

}
