package com.example.messanger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messanger.adapters.ChatAdapter;
import com.example.messanger.model.Chat;
import com.example.messanger.model.Profile;
import com.example.messanger.model.User;
import com.example.messanger.DTO.UserDataResponse;
import com.example.messanger.service.ChatService;
import com.example.messanger.service.ProfileService;
import com.example.messanger.service.UserService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.example.messanger.model.ChatSorter;


import java.util.ArrayList;
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
    private TextView viewUsername;
    private TextView viewEmail;
    private ImageView imageView;

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
        viewUsername = navigationView.getHeaderView(0).findViewById(R.id.nav_header_title);
        viewEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_header_subtitle);
        imageView = navigationView.getHeaderView(0).findViewById(R.id.nav_header_imageview);
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
        Retrofit retrofit = ApiClient.getAuthorizedClient(getString(R.string.auth_base_url),user_token);
        ProfileService profileService = retrofit.create(ProfileService.class);
        profileService.getProfile(currentUser.getId()).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().getProfilePicture() != null)
                        runOnUiThread(() -> {Glide.with(ChatsActivity.this)
                                .load(getString(R.string.auth_base_url) + response.body().getProfilePicture())
                                .circleCrop()
                                .placeholder(R.drawable.load)
                                .error(R.drawable.no_image)
                                .into(imageView);
                    viewUsername.setText(response.body().getNickname());
                    viewEmail.setText(currentUser.getEmail());});
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(ChatsActivity.this, "Не удалось получить профиль пользователя" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getChats() {
        Retrofit retrofit = ApiClient.getAuthorizedClient(getString(R.string.chats_base_url), user_token);
        chatService = retrofit.create(ChatService.class);
        chatService.getChats(currentUser.getId()).enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatList = response.body();
                    ChatSorter chatSorter = new ChatSorter();
                    List<Chat> sortedChats;
                    if (!chatList.isEmpty()) {
                        sortedChats = chatSorter.sortChats(chatList);
                        if (sortedChats != null && !sortedChats.isEmpty())
                            setupChatAdapter(sortedChats);
                    }
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
                //returnToLoginPage();
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
                if (id == R.id.nav_profile) {
                    Toast.makeText(ChatsActivity.this, "Профиль выбран", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChatsActivity.this, ProfileActivity.class);
                    if(currentUser != null) {
                        intent.putExtra("USER_ID", currentUser.getId());
                        intent.putExtra("USER_NAME", currentUser.getUsername());
                    }
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_logout) {
                    Toast.makeText(ChatsActivity.this, "Выход выбран", Toast.LENGTH_SHORT).show();
                    logout();
                } else if (id == R.id.nav_search) {
                    if(currentUser != null) {
                        Intent intent = new Intent(ChatsActivity.this, SearchActivity.class);
                        intent.putExtra("USER_ID", currentUser.getId());
                        intent.putExtra("USER_NAME", currentUser.getUsername());
                        startActivityForResult(intent, 1234);
                    } else {
                        Toast.makeText(ChatsActivity.this, "Нет подключения", Toast.LENGTH_SHORT).show();
                    }

                }

                navigationView.setCheckedItem(0);
                drawerLayout.closeDrawer(GravityCompat.START);  // Закрываем боковое меню после выбора
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234 && resultCode == 4321) {
            getChats();
        }
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
        intent.putExtra("CHAT_NAME", chat.getName());
        intent.putExtra("USER_ID", currentUser.getId());
        intent.putExtra("USER_NAME", currentUser.getUsername());
        startActivity(intent);
        finish();
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