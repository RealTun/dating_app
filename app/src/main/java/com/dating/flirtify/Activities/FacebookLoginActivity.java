package com.dating.flirtify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dating.flirtify.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.FacebookSdk;

import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {

    private static final String TAG = "FacebookLoginActivity";
    private CallbackManager callbackManager;
    private static final String FIELDS = "id,name,email,birthday,friends,picture.type(large),location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEdgeToEdgeMode();
        setContentView(R.layout.activity_facebook_login);
        setupWindowInsets();

        // Initialize Facebook SDK
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // Đăng nhập với các quyền cần thiết
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends", "user_photos", "user_location"));

        // Callback registration
        registerFacebookCallback();
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

    private void registerFacebookCallback() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook Login Success");
                GraphRequest request = new GraphRequest(
                        loginResult.getAccessToken(),
                        "/me?fields=" + FIELDS,
                        null,
                        HttpMethod.GET,
                        response -> {
                            if (response.getError() == null) {
                                JSONObject userInfo = response.getJSONObject();
                                String userName = userInfo.optString("name");
                                String userEmail = userInfo.optString("email");
                                String userBirthday = userInfo.optString("birthday");
                                JSONObject friends = userInfo.optJSONObject("friends");
                                String userLocation = userInfo.optString("location");
                                String profilePictureUrl = userInfo.optJSONObject("picture").optJSONObject("data").optString("url");

                                // Tiến hành xử lý các thông tin thu thập được
                                Log.d(TAG, "User Name: " + userName);
                                Log.d(TAG, "User Email: " + userEmail);
                                Log.d(TAG, "User Birthday: " + userBirthday);
                                Log.d(TAG, "User Location: " + userLocation);
                                Log.d(TAG, "Profile Picture URL: " + profilePictureUrl);

                                // Xử lý dữ liệu bạn muốn, ví dụ chuyển sang Activity khác
                                Intent intent = new Intent(FacebookLoginActivity.this, PreviewActivity.class);
                                intent.putExtra("userName", userName);
                                intent.putExtra("userEmail", userEmail);
                                intent.putExtra("userBirthday", userBirthday);
                                if (friends != null) {
                                    intent.putExtra("userFriends", friends.toString());
                                }
                                intent.putExtra("userLocation", userLocation);
                                intent.putExtra("profilePictureUrl", profilePictureUrl);
                                startActivity(intent);
                            } else {
                                Log.e(TAG, "Graph Request Error: " + response.getError().getErrorMessage());
                                // Xử lý lỗi khi tải dữ liệu
                                runOnUiThread(() -> Toast.makeText(FacebookLoginActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show());
                            }
                        }
                );

                Bundle parameters = new Bundle();
                parameters.putString("fields", FIELDS);
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook Login Canceled");
                runOnUiThread(() -> Toast.makeText(FacebookLoginActivity.this, "Login Canceled", Toast.LENGTH_SHORT).show());
                Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "Facebook Login Error: " + exception.getMessage());
                runOnUiThread(() -> Toast.makeText(FacebookLoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show());
                Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
