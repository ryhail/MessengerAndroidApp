package com.example.chatservice.DTO;

import com.example.chatservice.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessageData {
    private Long id;
    private String content;
    private Date timestamp;
    private Long senderId;
    private MessageType type;
}
