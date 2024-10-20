package com.example.messanger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messanger.adapters.ChatAdapter;
import com.example.messanger.model.Chat;
import com.example.messanger.model.User;
import com.example.messanger.DTO.UserDataResponse;
import com.example.messanger.service.ChatService;
import com.example.messanger.service.UserService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatsActivity extends AppCompatActivity implements ChatAdapter.OnChatClickListener {
    private String user_token;
    private SharedPreferences preferences;
    private UserService userService;
    private ChatService chatService;
    private User currentUser;
    private List<Chat> chatList;
    private RecyclerView recyclerViewChats;
    private ChatAdapter chatAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        user_token = preferences.getString("jwt_token", "NONE");
        if (user_token.isEmpty() || user_token.equals("NONE")) {
            Intent intent = new Intent(ChatsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setupDrawerContent(navigationView);
        checkUser();

    }
    private void checkUser() {
        Retrofit retrofit = ApiClient.getAuthorizedClient(getString(R.string.auth_base_url), user_token);
        userService = retrofit.create(UserService.class);
        userService.userDataRequest().enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body().getUser();
                    setupCurrentUser();
                    getChats();
                } else {
                    Toast.makeText(ChatsActivity.this, "Невозможно получить информацию о пользователе", Toast.LENGTH_SHORT).show();
                    returnToLoginPage();
                }
            }

            private void returnToLoginPage() {
                Intent intent = new Intent(ChatsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                Toast.makeText(ChatsActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCurrentUser() {
        TextView view = findViewById(R.id.nav_header_title);
        view.setText(currentUser.getUsername());
        view = findViewById(R.id.nav_header_subtitle);
        view.setText(currentUser.getEmail());
    }

    private void getChats() {
        Retrofit retrofit = ApiClient.getAuthorizedClient(getString(R.string.chats_base_url), user_token);
        chatService = retrofit.create(ChatService.class);
        chatService.getChats(currentUser.getId()).enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatList = response.body();
                    if (!chatList.isEmpty())
                        setupChatAdapter(chatList);
                }
            }
            private void returnToLoginPage() {
                Intent intent = new Intent(ChatsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(ChatsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                returnToLoginPage();
            }
        });
    }

    private void setupChatAdapter(List<Chat> chatList) {
        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(chatList, this);
        recyclerViewChats.setAdapter(chatAdapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_chat) {
                    Toast.makeText(ChatsActivity.this, "Чаты выбраны", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_profile) {
                    Toast.makeText(ChatsActivity.this, "Профиль выбран", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_logout) {
                    Toast.makeText(ChatsActivity.this, "Выход выбран", Toast.LENGTH_SHORT).show();
                    logout();

                }
                drawerLayout.closeDrawer(GravityCompat.START);  // Закрываем боковое меню после выбора
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onChatClick(Chat chat) {
        Intent intent = new Intent(ChatsActivity.this, MessagesActivity.class);
        intent.putExtra("CHAT_ID", chat.getId());
        intent.putExtra("CURRENT_USER_ID", currentUser.getId());
        intent.putExtra("CURRENT_USER_NAME", currentUser.getUsername());
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwt_token");
        editor.apply();
        Intent intent = new Intent(ChatsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}