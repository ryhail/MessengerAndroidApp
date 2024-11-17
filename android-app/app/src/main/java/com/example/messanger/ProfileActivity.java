package com.example.messanger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.messanger.DTO.ImageRequest;
import com.example.messanger.model.Profile;
import com.example.messanger.service.ProfileService;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    private static final int RQ_IMAGE_PICK = 666;
    private ImageView profileImageView;
    private EditText displayNameEditText;
    private EditText userInfoEditText;
    private CheckBox privacyCheckBox;
    private TextView registrationDateTextView;
    private Button uploadPic;
    private Button update;
    private Long currentUserId;
    private Profile currentProfile;
    private ProfileService profileService;
    private String user_token;
    private SharedPreferences preferences;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        user_token = preferences.getString("jwt_token", "NONE");
        if (user_token.isEmpty() || user_token.equals("NONE")) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        toolbar = findViewById(R.id.profile_toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);

        toolbar.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChatsActivity.class);
            startActivity(intent);
            finish();
        });

        Intent intent = getIntent();
        currentUserId = intent.getLongExtra("USER_ID", -1);

        profileImageView = findViewById(R.id.profileImage);
        displayNameEditText = findViewById(R.id.displayNameEditText);
        userInfoEditText = findViewById(R.id.userInfoEditText);
        privacyCheckBox = findViewById(R.id.privacyCheckBox);
        registrationDateTextView = findViewById(R.id.registrationDateTextView);
        uploadPic = findViewById(R.id.upload_pic);
        update = findViewById(R.id.update_profile);
        loadUserProfile();
        buttonListener();

        update.setOnClickListener(v -> {
            currentProfile.setBio(userInfoEditText.getText().toString());
            currentProfile.setNickname(displayNameEditText.getText().toString());
            currentProfile.setPrivacyStatus(privacyCheckBox.isChecked());
            profileService.update(currentProfile).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        currentProfile = response.body();
                        setupProfile(currentProfile);
                    }
                }
                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Не удалось обновить профиль " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void buttonListener() {
        uploadPic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RQ_IMAGE_PICK);
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void uploadImage(Bitmap bitmap) {
        String base64Image = convertBitmapToBase64(bitmap);
        profileService.updatePfp(new ImageRequest(base64Image)).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(response.isSuccessful() && response.body() != null) {
                    currentProfile = response.body();
                    setupProfile(currentProfile);
                }
            }
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Не удалось загрузить изображение " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private void loadUserProfile() {
        Retrofit retrofit = ApiClient.getAuthorizedClient(getString(R.string.auth_base_url),user_token);
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
        if(profile == null)
            this.finish();
        Glide.with(this)
                .load(getString(R.string.auth_base_url) + profile.getProfilePicture())
                .placeholder(R.drawable.load)
                .error(R.drawable.no_image)
                .into(profileImageView);
        displayNameEditText.setText(profile.getNickname());
        userInfoEditText.setText(profile.getBio());
        privacyCheckBox.setChecked(profile.getPrivacyStatus());
        registrationDateTextView.setText(String.format("Зарегистрирован: %s", profile.getRegistrationDateString()));
    }

}

