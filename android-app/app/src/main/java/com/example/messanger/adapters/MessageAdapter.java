package com.example.messanger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messanger.R;
import com.example.messanger.model.Message;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_CURRENT_USER = 1;
    private static final int VIEW_TYPE_OTHER_USER = 2;
    private List<Message> messages;
    private Long currentUserId;
    private Context context;

    public MessageAdapter(List<Message> messages, Long currentUserId, Context context) {
        this.messages = messages;
        this.currentUserId = currentUserId;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CURRENT_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_current_user, parent, false);
            return new CurrentUserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_other_user, parent, false);
            return new OtherUserMessageViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_CURRENT_USER;
        } else {
            return VIEW_TYPE_OTHER_USER;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof CurrentUserMessageViewHolder) {
            ((CurrentUserMessageViewHolder) holder).bind(message);
        } else if (holder instanceof OtherUserMessageViewHolder) {
            ((OtherUserMessageViewHolder) holder).bind(message);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class CurrentUserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView timestamp;

        public CurrentUserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
            timestamp = itemView.findViewById(R.id.date_message);
        }

        public void bind(Message message) {
            textMessage.setText(message.getText());
            timestamp.setText(message.getMessageTime());
        }
    }

    // ViewHolder для сообщений других пользователей
    public static class OtherUserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView timestamp;

        public OtherUserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
            timestamp = itemView.findViewById(R.id.date_message);
        }

        public void bind(Message message) {
            textMessage.setText(message.getText());
            timestamp.setText(message.getMessageTime());
        }
    }
}
