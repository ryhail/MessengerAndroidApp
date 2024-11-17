package com.example.messanger;

import static java.util.Locale.filter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

import com.example.messanger.adapters.ProfileAdapter;
import com.example.messanger.model.Message;
import com.example.messanger.model.Profile;
import com.example.messanger.service.ProfileService;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private EditText searchEditText;
    private Retrofit retrofit;
    private ProfileService service;
    private TextView emptyText;

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchEditText = findViewById(R.id.searchEditText);
        retrofit = ApiClient.getClient(getString(R.string.auth_base_url));
        service = retrofit.create(ProfileService.class);
        emptyText = findViewById(R.id.emptyView);

        toolbar = findViewById(R.id.search_toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);

        toolbar.setOnClickListener(v -> {
            finish();
        });

        textListenerSetup();
    }


    private void textListenerSetup() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSearchResult(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getSearchResult(String arg) {
        service.searchProfiles(arg).enqueue(new Callback<List<Profile>>() {

            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if (response.code() == 404)
                    toggleEmptyViewOff();
                if (response.isSuccessful() && response.body() != null) {
                    toggleEmptyViewOn();
                    List<Profile> profileList = response.body();
                    updateAdapter(profileList);
                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                toggleEmptyViewOff();
            }
        });
    }

    private void toggleEmptyViewOn() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);
    }
    private void toggleEmptyViewOff() {
        recyclerView.setVisibility(View.GONE);
        emptyText.setVisibility(View.VISIBLE);
    }

    private void updateAdapter(List<Profile> profileList) {
        profileAdapter = new ProfileAdapter(this, profileList);
        recyclerView.setAdapter(profileAdapter);
    }
}