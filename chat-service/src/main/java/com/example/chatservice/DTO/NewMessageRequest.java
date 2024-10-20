package com.example.chatservice.DTO;

import com.example.chatservice.model.Chatter;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
@Data
public class NewMessageRequest {
    @NotBlank
    private String text;
    @NotNull
    private Chatter sender;
}
