package com.example.chatservice.controller;

import com.example.chatservice.DTO.MessageData;
import com.example.chatservice.DTO.NewMessageRequest;
import com.example.chatservice.model.Message;
import com.example.chatservice.service.ChatService;
import com.example.chatservice.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/msg")
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    MessageService messageService;

    @GetMapping("/{chatId}")
    public List<MessageData> getMessages(@PathVariable Long chatId) {
        return messageService.getMessagesInChat(chatId);
    }
    @PostMapping("/new/{chatId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewMessage(@RequestBody @Valid NewMessageRequest newMessageRequest,
                              @PathVariable Long chatId) {
        messageService.addMessage(newMessageRequest, chatId);
    }
}
