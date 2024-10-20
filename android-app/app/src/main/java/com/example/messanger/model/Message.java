package com.example.messanger.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Message {
    private Long id;
    private String text;
    private Date timestamp;
    private Long senderId;

    public Message(String text, Long senderId) {
        this.text = text;
        this.senderId = senderId;
        this.timestamp = new Date();
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getMessageTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return dateFormat.format(timestamp);
    }
}
