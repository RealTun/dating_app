package com.dating.flirtify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Requests.LoginRequest;
import com.dating.flirtify.Models.Responses.LoginResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeView();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void initializeView() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void performLogin() {
        // Định nghĩa thông tin đăng nhập
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Lấy instance của ApiService thông qua ApiClient
        ApiService apiService = ApiClient.getClient();

        // Gửi yêu cầu đăng nhập
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        // Xử lý dữ liệu phản hồi
                        String accessToken = loginResponse.getAccessToken();
                        String tokenType = loginResponse.getTokenType();

                        // Lưu phiên đăng nhập vào SharedPreferences
                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.saveLoginSession(accessToken, tokenType);
                        sessionManager.saveLocationUser("");

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn hình chính
                        Intent intent = new Intent(LoginActivity.this, PreviewActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Log.e("API Error", "Lỗi: " + response.code());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage(), t);
            }
        });
    }
}