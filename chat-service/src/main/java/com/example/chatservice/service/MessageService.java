package com.example.chatservice.service;

import com.example.chatservice.DTO.MessageData;
import com.example.chatservice.DTO.NewMessageRequest;
import com.example.chatservice.model.Chat;
import com.example.chatservice.model.Message;
import com.example.chatservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository repository;
    private final ChatService chatService;

    public List<MessageData> getMessagesInChat(Long chatId) {
        List<MessageData> messages = chatService.getChatById(chatId).getMessages()
                .stream()
                .map(msg -> new MessageData(
                        msg.getId(),
                        msg.getText(),
                        msg.getTimestamp(),
                        msg.getSender().getId()
                ))
                .toList();
        if(messages == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(messages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return messages;
    }

    public void addMessage(NewMessageRequest newMessageRequest, Long chatId) {
        chatService.addMessage(newMessageRequest, chatId);
    }
}
