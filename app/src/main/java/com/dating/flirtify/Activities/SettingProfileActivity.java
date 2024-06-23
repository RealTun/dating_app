package com.dating.flirtify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;

import java.util.List;

import retrofit2.Call;

public class SettingProfileActivity extends AppCompatActivity {
    private ApiService apiService;
    private String accessToken;
    private List<String> listInterest;
    private TextView tvInterests;
    private EditText edtFullname, edtBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        handlerEvent();
    }

    private void initView() {
        edtFullname = findViewById(R.id.edt_fullname);
        edtBio = findViewById(R.id.edt_bio);
        tvInterests = findViewById(R.id.edt_interests);

        apiService = ApiClient.getClient();
        accessToken = SessionManager.getToken();
        getCurrentUser();
    }

    private void getCurrentUser() {
        Call<UserResponse> call = apiService.getUser(accessToken);
        call.enqueue(new retrofit2.Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    listInterest = userResponse.getInterests();
                    String listInterestString = listInterest.toString();
                    listInterestString = listInterestString.replace("[", "").replace("]", "");
                    tvInterests.setText(listInterestString);

                    edtFullname.setText(userResponse.getFullname());
                    edtBio.setText(userResponse.getBio());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void handlerEvent() {
        tvInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingProfileActivity.this, ListInterestActivity.class);
                startActivityForResult(intent, 9);
            }
        });
    }

}