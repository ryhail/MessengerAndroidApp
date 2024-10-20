package com.example.chatservice.DTO;

import com.example.chatservice.model.Chatter;
import lombok.Data;

import java.util.List;

@Data
public class CreateChatRequest {
    private List<Chatter> chatters;
}
