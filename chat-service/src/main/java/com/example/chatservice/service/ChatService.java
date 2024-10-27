package com.example.chatservice.service;
import com.example.chatservice.DTO.ChatInfoResponse;
import com.example.chatservice.DTO.CreateChatRequest;
import com.example.chatservice.DTO.NewMessageRequest;
import com.example.chatservice.model.Chat;
import com.example.chatservice.model.Chatter;
import com.example.chatservice.model.Message;
import com.example.chatservice.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository repository;
    private final ChatterService chatterService;
    public List<ChatInfoResponse> getChatsByChattersId(Long id) {
        List<Chat> chats = repository.getChatsByChattersId(id);
        List<ChatInfoResponse> chatsInfo = chats.stream().map(chat -> new ChatInfoResponse(
                chat.getId(),
                chat.getNameForUser(id),
                chat.getMessages().isEmpty() ? null: chat.getMessages().get(chat.getMessages().size()-1),
                chat.getChatters()))
                .toList();
        if(chatsInfo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(chats.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return chatsInfo;
    }

    public void createChat(CreateChatRequest request) {
        Chat chat = new Chat();
        if(request.getChatters().isEmpty() || request.getChatters().size() < 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        for(Chatter chatter: request.getChatters()) {
            if(chatterService.existsById(chatter.getId()))
                chat.getChatters().add(chatterService.getChatterById(chatter.getId()));
            else
                chat.getChatters().add(chatter);
        }
        chat.setName(request.getChatters().get(0).getName() + " и " + request.getChatters().get(1).getName());
        repository.save(chat);
    }

    public Chat getChatById(Long id) {
        if(repository.existsById(id)) {
            return repository.getReferenceById(id);
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void addMessage(NewMessageRequest messageRequest, Long id) {
        if (repository.existsById(id)) {
            Chat chat = repository.getReferenceById(id);
            Message msg = new Message();
            msg.setText(messageRequest.getText());
            msg.setSender(messageRequest.getSender());
            msg.setTimestamp(new Date());
            chat.addMessage(msg);
            repository.save(chat);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
