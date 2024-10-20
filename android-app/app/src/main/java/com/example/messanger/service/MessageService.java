package com.example.messanger.service;

import com.example.messanger.DTO.NewMessageRequest;
import com.example.messanger.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {
    @GET("/msg/{chatId}")
    Call<List<Message>> getMessages(@Path("chatId") Long chatId);
    @POST("msg/new/{chatId}")
    Call<Void> addNewMessage(@Path("chatId") Long chatId, @Body NewMessageRequest newMsg);
}
