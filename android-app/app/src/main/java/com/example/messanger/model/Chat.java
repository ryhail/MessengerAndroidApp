package com.example.messanger.model;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        if(lastMessage != null) {
            if (lastMessage.getType() == MessageType.image)
                return "изображение";
            return lastMessage.getContent();
        }
        else
            return "Нет сообщений";
    }
    public String getMessageTime() {
        if(lastMessage != null) {
            Date timestamp = lastMessage.getTimestamp();
            Date currentDate = new Date();
            if(currentDate.getTime() - 86400000 > timestamp.getTime()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:HH:mm");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                return dateFormat.format(timestamp);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            return dateFormat.format(timestamp);
        } else return "";
    }
}
