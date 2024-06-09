package com.dating.flirtify.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.User;
import com.dating.flirtify.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    EditText etMessage;
    ImageButton btn_back;
    ApiService apiService;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etMessage = findViewById(R.id.etMessage);
        btn_back = findViewById(R.id.imgbtn_back);
        apiService = ApiClient.getClient();

        Intent intent = getIntent();

        etMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2; // Right drawable
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (etMessage.getCompoundDrawables()[DRAWABLE_END] != null) {
                        int drawableWidth = etMessage.getCompoundDrawables()[DRAWABLE_END].getBounds().width();
                        if (event.getRawX() >= (etMessage.getRight() - drawableWidth - etMessage.getPaddingEnd())) {
                            getDetailsUser();
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        // back to matcher display
        btn_back.setOnClickListener(v -> {
            finish();
        });
    }

    private void getDetailsUser(){
        String accessToken = "Bearer " + "2|xB7YKMGNrtVe8JqhpPQYc28ymFNav2kqWglXW61d55e976a5";

        Call<User> call = apiService.getUser(accessToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    User user = response.body();
                    Log.d("UserResponse", user.getFullname());
                }
                else if (response.code() == 401) {
                    Log.d("UserResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}