package com.example.messanger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.messanger.model.UserDataResponse;
import com.example.messanger.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatActivity extends AppCompatActivity {
    private String user_token;
    private SharedPreferences preferences;
    private UserService userService;
    private User currentUser;
    private Button logoutButton;
    private TextView helloText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        user_token = preferences.getString("jwt_token","NONE");
        logoutButton = findViewById(R.id.button_logout);
        if(user_token.isEmpty() || user_token.equals("NONE")) {
            Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Retrofit retrofit = ApiClient.getAuthorizedClient(getString(R.string.auth_base_url), user_token);
        userService = retrofit.create(UserService.class);

        userService.userDataRequest().enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body().getUser();
                    Toast.makeText(ChatActivity.this, "Здравствуйте, " + currentUser.getUsername()+"!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Невозможно получить информацию о пользователе", Toast.LENGTH_SHORT).show();
                    returnToLoginPage();
                }
            }
            private void returnToLoginPage() {
                Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        String hello = "Здравствуйте, " + currentUser.getUsername();
//        helloText.setText(hello);

        logoutButton.setOnClickListener(v -> logout());
    }

    private void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwt_token");
        editor.apply();
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}