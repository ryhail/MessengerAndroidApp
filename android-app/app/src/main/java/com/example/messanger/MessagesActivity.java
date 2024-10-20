package com.example.messanger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messanger.DTO.NewMessageRequest;
import com.example.messanger.adapters.ChatAdapter;
import com.example.messanger.adapters.MessageAdapter;
import com.example.messanger.model.Chat;
import com.example.messanger.model.Chatter;
import com.example.messanger.model.Message;
import com.example.messanger.model.User;
import com.example.messanger.service.ChatService;
import com.example.messanger.service.MessageService;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessagesActivity extends AppCompatActivity {
    private String user_token;
    private SharedPreferences preferences;
    private TextView chatName;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private User currentUser;
    private Long chatId;
    private MessageService msgService;
    private MaterialToolbar toolbar;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        user_token = preferences.getString("jwt_token", "NONE");
        if (user_token.isEmpty() || user_token.equals("NONE")) {
            Intent intent = new Intent(MessagesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        toolbar = findViewById(R.id.chat_toolbar);

        toolbar.setNavigationIcon(R.drawable.arrow_back);
        //chatName = findViewById(R.id.chat_name);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        toolbar.setOnClickListener(v -> {
            finish();
        });
        // Получаем имя чата и ID текущего пользователя из Intent
        Intent intent = getIntent();
        chatId = intent.getLongExtra("CHAT_ID", -1);
        currentUser = new User(intent.getLongExtra("CURRENT_USER_ID", -1),
                            intent.getStringExtra("CURRENT_USER_NAME"),"");
        //chatName.setText(chatId.toString());
        toolbar.setTitle(chatId.toString());
        getMessages();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void getMessages() {
        retrofit = ApiClient.getAuthorizedClient(getString(R.string.chats_base_url), user_token);
        msgService = retrofit.create(MessageService.class);
        msgService.getMessages(chatId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    messageList = response.body();
                    setupMessageAdapter(messageList);
                }
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(MessagesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupMessageAdapter(List<Message> messageList) {
        messageAdapter = new MessageAdapter(messageList, currentUser.getId(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(false);
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            editTextMessage.setText("");
            NewMessageRequest request = new NewMessageRequest(
                    new Chatter(currentUser.getId(), currentUser.getUsername()),
                    messageText);
            retrofit = ApiClient.getAuthorizedClient(getString(R.string.chats_base_url), user_token);
            msgService.addNewMessage(chatId, request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 201) {
                        getMessages();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MessagesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}