package com.example.messanger.model;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class Chat {
    private Long id;
    private String name;
    @Nullable
    private Message lastMessage;
    private Set<Chatter> chatters;

    public Long getId() {
        return id;
    }
    public String getName() {
        return this.name;
    }

    public String getLastMessage() {
        if(lastMessage != null)
            return lastMessage.getText();
        else
            return "Нет сообщений";
    }
    public String getMessageTime() {
        if(lastMessage != null) {
            Date timestamp = lastMessage.getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            return dateFormat.format(timestamp);
        } else return "";
    }
}
