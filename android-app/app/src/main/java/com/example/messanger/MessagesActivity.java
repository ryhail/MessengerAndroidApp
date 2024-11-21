package com.example.messanger;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import com.example.messanger.DTO.CreateChatRequest;
import com.example.messanger.DTO.ImageRequest;
import com.example.messanger.DTO.NewMessageRequest;
import com.example.messanger.adapters.ChatAdapter;
import com.example.messanger.adapters.MessageAdapter;
import com.example.messanger.model.Chat;
import com.example.messanger.model.Chatter;
import com.example.messanger.model.Message;
import com.example.messanger.model.MessageType;
import com.example.messanger.model.Profile;
import com.example.messanger.model.User;
import com.example.messanger.service.ChatService;
import com.example.messanger.service.MessageService;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessagesActivity extends AppCompatActivity {
    private static final int RQ_IMAGE_PICK = 666;
    private String user_token;
    private SharedPreferences preferences;
    private String chatName;
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
    private ChatService chatService;
    private Button addImage;
    private Timer t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getWindow().setNavigationBarColor(getColor(R.color.red));
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
        addImage = findViewById(R.id.buttonAddImage);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(MessagesActivity.this, ChatsActivity.class);
            startActivity(intent);
            t.cancel();
            finish();
        });

        Intent intent = getIntent();
        chatId = intent.getLongExtra("CHAT_ID", -1);
        currentUser = new User(intent.getLongExtra("USER_ID", -1),
                intent.getStringExtra("USER_NAME"),"");
        if(chatId != -1) {
            chatName = intent.getStringExtra("CHAT_NAME");
            toolbar.setTitle(chatName);
            getMessages();
        } else {
            getChatWithUserId(intent.getLongExtra("PROFILE_ID", -1),
                    intent.getStringExtra("PROFILE_NAME"));
        }

        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateMessageList();
            }
        },2000, 500);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQ_IMAGE_PICK);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                uploadImage(bitmap);
                Toast.makeText(MessagesActivity.this, "Отправка изображения", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private void uploadImage(Bitmap bitmap) {
        String base64Image = convertBitmapToBase64(bitmap);
        msgService.addNewMessage(chatId, new NewMessageRequest(
                new Chatter(currentUser.getId(), currentUser.getUsername()),
                base64Image,MessageType.image)).enqueue(new Callback<Void>() {
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

    private void getChatWithUserId(long profileId, String profileName) {
        Set<Chatter> chatters = new HashSet<>();
        chatters.add(new Chatter(currentUser.getId(),currentUser.getUsername()));
        chatters.add(new Chatter(profileId,profileName));
        CreateChatRequest request = new CreateChatRequest(chatters);
        retrofit = ApiClient.getAuthorizedClient(getString(R.string.chats_base_url), user_token);
        chatService = retrofit.create(ChatService.class);
        chatService.getOrCreate(request).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                chatId = response.body().getId();
                chatName = response.body().getName();
                toolbar.setTitle(profileName);
                getMessages();
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                finish();
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

    private void updateMessageList() {
        Long lastMessageTime = 0L;
        if(msgService != null) {
            if(messageList != null && !messageList.isEmpty())
                lastMessageTime = messageList.get(messageList.size()-1).getTimestamp().getTime();
            msgService.getNewMessages(chatId, lastMessageTime).enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        if(!response.body().isEmpty() && messageList != null) {
                            runOnUiThread(() -> {
                                if (messageAdapter != null) {
                                    appendAdapter(response.body());
                                }
                            });
                        }
                        if(messageList == null) {
                            messageList = response.body();
                            setupMessageAdapter(messageList);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {

                }
            });
        }
    }
    private void appendAdapter(List<Message> newItems) {
        messageAdapter.addItems(newItems);
        recyclerViewMessages.scrollToPosition(messageAdapter.getItemCount()-1);
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            editTextMessage.setText("");
            NewMessageRequest request = new NewMessageRequest(
                    new Chatter(currentUser.getId(), currentUser.getUsername()),
                    messageText, MessageType.text);
            retrofit = ApiClient.getAuthorizedClient(getString(R.string.chats_base_url), user_token);
            msgService.addNewMessage(chatId, request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MessagesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}