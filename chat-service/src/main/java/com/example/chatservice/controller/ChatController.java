package com.example.chatservice.controller;


import com.example.chatservice.DTO.ChatInfoResponse;
import com.example.chatservice.DTO.CreateChatRequest;
import com.example.chatservice.DTO.NewMessageRequest;
import com.example.chatservice.model.Chat;
import com.example.chatservice.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

//    @GetMapping(value = "/{chatId}", produces = "application/json")
//    public Chat getChatById(@PathVariable Long chatId) {
//        return chatService.getChatById(chatId);
//    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewChat(@RequestBody CreateChatRequest createChatRequest) {
        chatService.createChat(createChatRequest);
    }
    @PostMapping("/get-or-create")
    public ChatInfoResponse ChatInfoResponse(@RequestBody CreateChatRequest createChatRequest) {
        return chatService.createOrGet(createChatRequest);
    }

    @GetMapping("/{userId}")
    public List<ChatInfoResponse> getUsersChats(@PathVariable Long userId) {
        return chatService.getChatsByChattersId(userId);
    }
}
