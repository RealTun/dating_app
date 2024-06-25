package com.dating.flirtify.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dating.flirtify.R;
import com.dating.flirtify.Services.NetworkChangeReceiver;
import com.dating.flirtify.Services.SessionManager;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextView tvPhoneLogin, tvPrivacyPolicy;
    private ImageView ivLoginFB, ivLoginGG, ivLoginApple;
    private NetworkChangeReceiver networkChangeReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdgeMode();
        setContentView(R.layout.activity_main);

        // Đăng ký BroadcastReceiver
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

        setupWindowInsets();
        checkUserSession();

        setupAppNameTextView();
        setupRegisterButton();
        setupPhoneLoginTextView();
        setupFacebookLoginImageView();
        setupGoogleLoginImageView();
        setupAppleLoginImageView();
        setupPrivacyPolicyTextView();
    }

    private void setupEdgeToEdgeMode() {
        EdgeToEdge.enable(this);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkUserSession() {
        SessionManager sessionManager = new SessionManager(this);
        String accessToken = sessionManager.getAccessToken();

        // FB
        AccessToken accessTokenFB = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessTokenFB != null && !accessTokenFB.isExpired();

        if (accessToken != null && !accessToken.isEmpty()) {
            navigateToPreviewActivity();
        } else if (isLoggedIn) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            navigateToPreviewActivity();
        }
    }

    private void navigateToPreviewActivity() {
        Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupAppNameTextView() {
        TextView tvAppName = findViewById(R.id.tvAppName);
        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top),
                getResources().getColor(R.color.gradient_center),
                getResources().getColor(R.color.gradient_bottom));
    }

    private void setupRegisterButton() {
        btnRegister = findViewById(R.id.btnDeleteAccount);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void setupPhoneLoginTextView() {
        tvPhoneLogin = findViewById(R.id.tvPhoneLogin);
        tvPhoneLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void setupFacebookLoginImageView() {
        ivLoginFB = findViewById(R.id.ivLoginFB);
        ivLoginFB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FacebookLoginActivity.class);
            startActivity(intent);
        });
    }

    private void setupGoogleLoginImageView() {
        ivLoginGG = findViewById(R.id.ivLoginGG);
        ivLoginGG.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupAppleLoginImageView() {
        ivLoginApple = findViewById(R.id.ivLoginApple);
        ivLoginApple.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }


    private void setupPrivacyPolicyTextView() {
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        tvPrivacyPolicy.setOnClickListener(v -> {
            String url = "https://docs.google.com/document/d/1lDsk9xkbLPzvghpRmzS7KdBDmFtBfCFryBjz27gKcr4/edit?usp=sharing";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    private void setColorGradient(TextView tv, int... color) {
        TextPaint textPaint = tv.getPaint();
        float width = textPaint.measureText(tv.getText().toString());
        float height = tv.getTextSize();

        Shader textShader = new LinearGradient(
                0, 0, 0, height,
                color, null, Shader.TileMode.CLAMP
        );

        tv.getPaint().setShader(textShader);
        tv.setTextColor(color[0]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver khi không cần thiết
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}