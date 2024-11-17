package com.example.messanger.DTO;

import com.example.messanger.model.Chatter;
import com.example.messanger.model.MessageType;

public class NewMessageRequest {
    private Chatter sender;
    private String content;
    private MessageType type;

    public NewMessageRequest(Chatter sender, String content, MessageType type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
    }
}
