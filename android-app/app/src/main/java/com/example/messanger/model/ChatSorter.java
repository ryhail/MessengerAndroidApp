package com.example.messanger.model;

import java.util.List;

public class ChatSorter {
    static {
        System.loadLibrary("native-lib"); // Load the native library
    }

    public native List<Chat> sortChats(List<Chat> chats);
}
