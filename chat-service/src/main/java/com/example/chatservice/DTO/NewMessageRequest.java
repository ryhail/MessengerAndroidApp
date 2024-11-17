package com.example.chatservice.DTO;

import com.example.chatservice.model.Chatter;
import com.example.chatservice.model.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
@Data
public class NewMessageRequest {
    @NotBlank
    private String content;
    @NotNull
    private Chatter sender;
    @NotNull
    private MessageType type;
}
