package com.example.messanger.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messanger.R;
import com.example.messanger.model.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> chatList;
    private OnChatClickListener onChatClickListener;
    public ChatAdapter(List<Chat> chatList, OnChatClickListener onChatClickListener) {
        this.chatList = chatList;
        this.onChatClickListener = onChatClickListener;
    }
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView chatTitle;
        public TextView lastMessage;
        public TextView messageTime;

        public ChatViewHolder(View itemView) {
            super(itemView);
            chatTitle = itemView.findViewById(R.id.chatTitle);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            messageTime = itemView.findViewById(R.id.messageTime);
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.chatTitle.setText(chat.getId().toString());
        if(chat.getLastMessage() != null) {
            holder.lastMessage.setText(chat.getLastMessage());
            holder.messageTime.setText(chat.getMessageTime());
        }
        holder.itemView.setOnClickListener(
                v -> onChatClickListener.onChatClick(chat)
        );
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }
}