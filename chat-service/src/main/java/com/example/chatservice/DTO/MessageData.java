package com.example.chatservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessageData {
    private Long id;
    private String text;
    private Date timestamp;
    private Long senderId;
}
