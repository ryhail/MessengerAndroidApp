package com.example.messanger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messanger.DTO.LoginRequest;
import com.example.messanger.DTO.LoginResponse;
import com.example.messanger.service.AuthService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private AuthService authService;
    AtomicReference<String> token = new AtomicReference<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        TextView registerText = findViewById(R.id.register_text);

        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        usernameInput = findViewById(R.id.username_input_log);
        passwordInput = findViewById(R.id.password_input_log);
        loginButton = findViewById(R.id.login_button);

        Retrofit retrofit = ApiClient.getClient(getString(R.string.auth_base_url));
        authService = retrofit.create(AuthService.class);

        loginButton.setOnClickListener(v -> getTokenAndLogin());
    }

    private void getTokenAndLogin() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Ошибка FCM токена", Toast.LENGTH_LONG).show();
                        return;
                    }
                    token.set(task.getResult());
                    runOnUiThread(this::loginUser);
                });
    }
    private void loginUser() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Введите имя пользователя и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(username, password, token.get());

        authService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveToken(token);
                    Toast.makeText(LoginActivity.this, "Вход успешен", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    Toast.makeText(LoginActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                }
            }

            private void saveToken(String token) {
                SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("jwt_token", token);
                editor.apply();
            }

            private void updateUI() {
                Intent intent = new Intent(LoginActivity.this, ChatsActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}