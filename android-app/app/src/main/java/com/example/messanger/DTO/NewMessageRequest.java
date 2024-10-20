package com.example.messanger.DTO;

import com.example.messanger.model.Chatter;

public class NewMessageRequest {
    private Chatter sender;
    private String text;

    public NewMessageRequest(Chatter sender, String text) {
        this.sender = sender;
        this.text = text;
    }
}
