package com.example.chatservice.DTO;

import com.example.chatservice.model.Chatter;
import com.example.chatservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
public class ChatInfoResponse {
    private Long id;
    private String name;
    private Message lastMessage;
    private Set<Chatter> chatters;
}
