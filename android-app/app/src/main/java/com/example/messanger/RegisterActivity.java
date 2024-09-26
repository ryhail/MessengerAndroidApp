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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.messanger.model.LoginResponse;
import com.example.messanger.model.RegisterRequest;
import com.example.messanger.service.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput, emailInput;
    private Button registerButton;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        TextView registerText = findViewById(R.id.login_text);

        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        registerButton = findViewById(R.id.register_button);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        emailInput = findViewById(R.id.email_input);

        Retrofit retrofit = ApiClient.getClient(getString(R.string.auth_base_url));
        authService = retrofit.create(AuthService.class);

        registerButton.setOnClickListener(v -> registerUser());

    }

    private void registerUser() {
        String username = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Введите имя пользователя и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest registerRequest = new RegisterRequest(username,email, password);
        authService.register(registerRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveToken(token);
                    Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    Toast.makeText(RegisterActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                }
            }

            private void saveToken(String token) {
                SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("jwt_token", token);
                editor.apply();
            }

            private void updateUI() {
                Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}