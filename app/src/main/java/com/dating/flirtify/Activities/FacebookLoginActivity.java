package com.dating.flirtify.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dating.flirtify.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {

    private static final String TAG = "FacebookLoginActivity";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_facebook_login);

        // Khởi tạo Facebook SDK
        try {
            FacebookSdk.sdkInitialize(getApplicationContext());
        } catch (Exception e) {
            showErrorDialog("Error", e.getMessage());
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        callbackManager = CallbackManager.Factory.create();

        // Yêu cầu quyền đọc từ Facebook
        try{
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        } catch (Exception e) {
            showErrorDialog("Error", e.getMessage());
        }

        // Đăng ký callback cho quá trình đăng nhập Facebook
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Xử lý thành công đăng nhập
//                AccessToken accessToken = loginResult.getAccessToken();
//                // Gọi phương thức để xử lý access token hoặc chuyển đến màn hình tiếp theo
//                handleFacebookAccessToken(accessToken);

                Toast.makeText(FacebookLoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                AccessToken accessToken = loginResult.getAccessToken();
                getUserProfileAndShowDialog(accessToken);

            }

            @Override
            public void onCancel() {
                // Xử lý khi người dùng huỷ đăng nhập
                Toast.makeText(FacebookLoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // Xử lý khi có lỗi xảy ra trong quá trình đăng nhập
                Toast.makeText(FacebookLoginActivity.this, "Bug", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Phương thức onActivityResult để nhận kết quả từ quá trình đăng nhập Facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Phương thức xử lý access token hoặc chuyển đến màn hình tiếp theo
    private void handleFacebookAccessToken(AccessToken accessToken) {
        // Xử lý access token, ví dụ: gửi access token cho server để xác thực
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);

        // Chuyển đến màn hình chính của ứng dụng
        Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Kết thúc activity hiện tại
    }

    private void getUserProfileAndShowDialog(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Xử lý kết quả JSON chứa thông tin người dùng
                        try {
                            String userId = object.getString("id");
                            String userName = object.getString("name");
                            // Hiển thị thông tin người dùng trong dialog
                            showDialogWithUserInfo(userId, userName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name"); // Thêm các trường thông tin cần lấy
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void showDialogWithUserInfo(String userId, String userName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Info");
        builder.setMessage("User ID: " + userId + "\nUser Name: " + userName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
