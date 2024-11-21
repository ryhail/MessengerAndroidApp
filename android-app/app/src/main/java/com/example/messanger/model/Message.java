package com.example.messanger.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Message {
    private Long id;
    private String content;
    private Date timestamp;
    private MessageType type;
    private Long senderId;

    public Message(String content, MessageType type, Long senderId) {
        this.content = content;
        this.senderId = senderId;
        this.type = type;
        this.timestamp = new Date();
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getMessageTime() {
        Date currentDate = new Date();
        if(currentDate.getTime() - 86400000 > timestamp.getTime()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd:MM");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            return dateFormat.format(timestamp);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return dateFormat.format(timestamp);
    }
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }
}
