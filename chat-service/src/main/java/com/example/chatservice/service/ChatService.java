package com.example.chatservice.service;

import com.example.chatservice.DTO.*;
import com.example.chatservice.model.Chat;
import com.example.chatservice.model.Chatter;
import com.example.chatservice.model.Message;
import com.example.chatservice.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository repository;
    private final ChatterService chatterService;
    private final NotificationService notificationService;

    public List<ChatInfoResponse> getChatsByChattersId(Long id) {
        List<Chat> chats = repository.getChatsByChattersId(id);
        List<ChatInfoResponse> chatsInfo = chats.stream().map(chat -> new ChatInfoResponse(
                        chat.getId(),
                        chat.getNameForUser(id),
                        chat.getMessages().isEmpty() ? null : chat.getMessages().get(chat.getMessages().size() - 1),
                        chat.getChatters()))
                .toList();
        if (chatsInfo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (chats.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return chatsInfo;
    }

    public Chat createChat(CreateChatRequest request) {
        Chat chat = new Chat();
        if (request.getChatters().isEmpty() || request.getChatters().size() < 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        for (Chatter chatter : request.getChatters()) {
            if (chatterService.existsById(chatter.getId()))
                chat.getChatters().add(chatterService.getChatterById(chatter.getId()));
            else
                chat.getChatters().add(chatter);
        }
        String chatName = "";
        for (Chatter chatter : request.getChatters()) {
            chatName += chatter.getName() + " ";
        }
        chat.setName(chatName);
        return repository.save(chat);
    }

    public Chat getChatById(Long id) {
        if (repository.existsById(id)) {
            return repository.getReferenceById(id);
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    public List<Message> getMessagesAfterTimestamp(Long chatId, Date time) {
        return repository.getMessagesAfterTimestamp(chatId, time);
    }
    public void addMessage(NewMessageRequest messageRequest, Long id) {
        if (repository.existsById(id)) {
            Chat chat = repository.getReferenceById(id);
            Message msg = new Message();
            msg.setContent(messageRequest.getContent());
            msg.setSender(messageRequest.getSender());
            msg.setType(messageRequest.getType());
            msg.setTimestamp(new Date());
            chat.addMessage(msg);
            chat = repository.save(chat);
            if(chat != null) {
                NotificationRequest notReq = new NotificationRequest();
                notReq.setChat_id(chat.getId());
                notReq.setChatName(chat.getName());
                notReq.setRecipient_id(chat.getChatters().stream().filter(chr -> chr.getId() != messageRequest.getSender().getId()).findFirst().get().getId());
                notReq.setMsg(new MessageData(
                       msg.getId(),
                        msg.getContent(),
                        msg.getTimestamp(),
                        msg.getSender().getId(),
                        msg.getType()
                ));
                notificationService.notify(notReq);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    public ChatInfoResponse createOrGet(CreateChatRequest createChatRequest) {
        Optional<Chat> chat = repository.getChatByChatters(createChatRequest.getChatters(), createChatRequest.getChatters().size());
        if(chat.isPresent())
            return new ChatInfoResponse(
                    chat.get().getId(),
                    chat.get().getName(),
                    chat.get().getMessages().isEmpty() ? null : chat.get().getMessages().get(chat.get().getMessages().size() - 1),
                    chat.get().getChatters()
            );
        Chat chat_ = createChat(createChatRequest);
        Hibernate.initialize(chat_.getChatters());
        return new ChatInfoResponse(
                chat_.getId(),
                chat_.getName(),
                chat_.getMessages().isEmpty() ? null : chat_.getMessages().get(chat_.getMessages().size() - 1),
                chat_.getChatters().stream().map(chatter -> new Chatter(chatter.getId(),chatter.getName()))
                        .collect(Collectors.toSet())
        );
    }
}

