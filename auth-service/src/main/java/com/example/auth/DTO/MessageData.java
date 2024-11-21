package com.example.auth.DTO;

import com.example.auth.model.MessageType;
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
