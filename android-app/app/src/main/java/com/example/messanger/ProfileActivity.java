package com.example.messanger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messanger.model.Profile;
import com.example.messanger.service.ProfileService;
import com.example.messanger.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText displayNameEditText;
    private EditText userInfoEditText;
    private CheckBox privacyCheckBox;
    private TextView registrationDateTextView;
    private Long currentUserId;
    private Profile currentProfile;
    private ProfileService profileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        currentUserId = intent.getLongExtra("USER_ID", -1);

        profileImageView = findViewById(R.id.profileImage);
        displayNameEditText = findViewById(R.id.displayNameEditText);
        userInfoEditText = findViewById(R.id.userInfoEditText);
        privacyCheckBox = findViewById(R.id.privacyCheckBox);
        registrationDateTextView = findViewById(R.id.registrationDateTextView);
        loadUserProfile();

    }

    private void loadUserProfile() {
        Retrofit retrofit = ApiClient.getClient(getString(R.string.auth_base_url));
        profileService = retrofit.create(ProfileService.class);
        profileService.getProfile(currentUserId).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(response.isSuccessful() && response.body() != null) {
                    currentProfile = response.body();
                    setupProfile(currentProfile);
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Не удалось получить профиль пользователя" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupProfile(Profile profile) {
        displayNameEditText.setText(profile.getNickname());
        userInfoEditText.setText(profile.getBio());
        privacyCheckBox.setChecked(profile.getPrivacyStatus());
        registrationDateTextView.setText(String.format("Зарегистрирован: %s", profile.getRegistrationDate()));
    }

}

