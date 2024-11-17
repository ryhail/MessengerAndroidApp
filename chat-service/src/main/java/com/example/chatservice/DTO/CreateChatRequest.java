package com.example.chatservice.DTO;

import com.example.chatservice.model.Chatter;
import lombok.Data;

import java.util.Set;

@Data
public class CreateChatRequest {
    private Set<Chatter> chatters;
}
