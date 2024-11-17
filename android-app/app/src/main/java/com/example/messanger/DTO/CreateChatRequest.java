package com.example.messanger.DTO;

import com.example.messanger.model.Chatter;

import java.util.Set;

public class CreateChatRequest {
    private Set<Chatter> chatters;

    public CreateChatRequest(Set<Chatter> chatters) {
        this.chatters = chatters;
    }
}
