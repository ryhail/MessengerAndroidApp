package com.example.messanger.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messanger.R;
import com.example.messanger.model.Message;
import com.example.messanger.model.MessageType;
import com.example.messanger.util.OrientationTransformation;

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
    public void addItems(List<Message> newItems) {
        int startPosition = messages.size();
        messages.addAll(newItems);
        notifyItemRangeInserted(startPosition, newItems.size());
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
            ((CurrentUserMessageViewHolder) holder).bind(message, context);
        } else if (holder instanceof OtherUserMessageViewHolder) {
            ((OtherUserMessageViewHolder) holder).bind(message, context);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class CurrentUserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        ImageView imageView;
        TextView timestamp;

        public CurrentUserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
            timestamp = itemView.findViewById(R.id.date_message);
            imageView = itemView.findViewById(R.id.current_user_image);
        }

        public void bind(Message message, Context context) {
            if(message.getType() == MessageType.text) {
                textMessage.setText(message.getContent());
            }
            if(message.getType() == MessageType.image) {
                textMessage.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(((AppCompatActivity)context).getString(R.string.auth_base_url) +  message.getContent())
                        .transform(new OrientationTransformation())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.no_image)
                        .into(imageView);
            }
            timestamp.setText(message.getMessageTime());
        }
    }

    // ViewHolder для сообщений других пользователей
    public static class OtherUserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        ImageView imageView;
        TextView timestamp;

        public OtherUserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
            imageView = itemView.findViewById(R.id.other_user_image);
            timestamp = itemView.findViewById(R.id.date_message);
        }

        public void bind(Message message, Context context) {
            if(message.getType() == MessageType.text) {
                textMessage.setText(message.getContent());
            }
            if(message.getType() == MessageType.image) {
                textMessage.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(((AppCompatActivity)context).getString(R.string.auth_base_url) +  message.getContent())
                        .transform(new OrientationTransformation())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.no_image)
                        .into(imageView);
            }
            timestamp.setText(message.getMessageTime());
        }
    }
}
