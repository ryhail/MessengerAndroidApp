package com.example.auth.DTO;

import lombok.Data;

@Data
public class NotificationRequest {
    private String chatName;
    Long recipient_id;
    Long chat_id;
    MessageData msg;
}
