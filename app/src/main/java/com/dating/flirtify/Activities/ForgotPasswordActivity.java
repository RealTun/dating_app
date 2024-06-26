package com.dating.flirtify.Activities;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Requests.ChangePasswordRequest;
import com.dating.flirtify.Models.Requests.CheckEmailRequest;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.GmailSender;
import com.dating.flirtify.Services.OTPGenerators;
import com.dating.flirtify.Services.ShowMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView tvEmail, tvOTP, tvPW, tvAppName;
    private EditText etEmail, etOTP, etPW, etRePW;
    private Button btnSend, btnVerify, btnReset;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    private String generatedOTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        generatedOTP = OTPGenerators.generateOTP();
        setColorGradient(tvAppName, ResourcesCompat.getColor(getResources(), R.color.gradient_top, null), ResourcesCompat.getColor(getResources(), R.color.gradient_center, null), ResourcesCompat.getColor(getResources(), R.color.gradient_bottom, null));
        EventHandler();
    }
    private void setColorGradient(TextView tv, int... color) {
        TextPaint textPaint = tv.getPaint();
        float width = textPaint.measureText(tv.getText().toString());
        float height = tv.getTextSize();
        Shader textShader = new LinearGradient(0, 0, 0, height, color, null, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
        tv.setTextColor(color[0]);
    }
    private void initView() {
        tvAppName = findViewById(R.id.tvAppName);
        tvEmail = findViewById(R.id.tvEmail);
        tvOTP = findViewById(R.id.tvOTP);
        tvPW = findViewById(R.id.tvPW);
        etEmail = findViewById(R.id.etEmail);
        etOTP = findViewById(R.id.etOTP);
        etPW = findViewById(R.id.etPW);
        etRePW = findViewById(R.id.etRePW);
        btnSend = findViewById(R.id.btnSend);
        btnVerify = findViewById(R.id.btnVerify);
        btnReset = findViewById(R.id.btnReset);

        tvOTP.setVisibility(View.GONE);
        etOTP.setVisibility(View.GONE);
        btnVerify.setVisibility(View.GONE);
        tvPW.setVisibility(View.GONE);
        etPW.setVisibility(View.GONE);
        etRePW.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
    }

    private void EventHandler() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()) {
                    ShowMessage.showCustomDialog(ForgotPasswordActivity.this, "Thông báo", "Email không được bỏ trống!");
                    return;
                }
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(etEmail.getText().toString());
                if (!matcher.matches()) {
                    ShowMessage.showCustomDialog(ForgotPasswordActivity.this, "Thông báo", "Email nhập vào không hợp lệ!");
                    return;
                }
                // Gọi phương thức để kiểm tra email trùng lặp
                checkDuplicateEmail(etEmail.getText().toString());

            }
        });
        btnVerify.setOnClickListener(v -> {
            if (etOTP.getText().toString().equals(generatedOTP)) {
                tvPW.setVisibility(View.VISIBLE);
                etPW.setVisibility(View.VISIBLE);
                etRePW.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);

                etOTP.setVisibility(View.GONE);
                btnVerify.setVisibility(View.GONE);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "OTP không đúng!", Toast.LENGTH_SHORT).show();
                etOTP.setText("");
            }
        });
        btnReset.setOnClickListener(v -> {
            String password = etPW.getText().toString();
            String rePassword = etRePW.getText().toString();

            if (!validate(password)) {
                ShowMessage.showCustomDialog(ForgotPasswordActivity.this, "Thông báo", "Mật khẩu phải chứa ít nhất một chữ hoa, một chữ thường, một số, một ký tự đặc biệt và dài tối thiểu 8 ký tự.");
                return;
            }
            if (!password.equals(rePassword)) {
                ShowMessage.showCustomDialog(ForgotPasswordActivity.this, "Thông báo", "Mật khẩu xác nhận không khớp!");
                return;
            }
            updatePassword(etRePW.getText().toString().trim());
        });
    }

    public void checkDuplicateEmail(String email) {
        // Lấy instance của ApiService thông qua ApiClient
        ApiService apiService = ApiClient.getClient();
        CheckEmailRequest checkEmailRequest = new CheckEmailRequest(email);
        // Gửi yêu cầu đăng ký
        Call<Void> call = apiService.checkDuplicateEmail(checkEmailRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ShowMessage.showCustomDialog(ForgotPasswordActivity.this, "Thông báo", "Email không tồn tại trong hệ thống!");
                    Log.e("checkDuplicateEmail", "Email not found!");
                }
                else{
                    // Xử lý khi yêu cầu thành công
                    Log.d("checkDuplicateEmail", "Tồn tại mail user trong hệ thống");

                    sendOTPEmail(etEmail.getText().toString().trim(), generatedOTP);
                    tvOTP.setVisibility(View.VISIBLE);
                    etOTP.setVisibility(View.VISIBLE);
                    btnVerify.setVisibility(View.VISIBLE);

                    etEmail.setVisibility(View.GONE);
                    btnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void sendOTPEmail(String email, String otp) {
        String subject = "Flirtify";
        String message = otp;

        new Thread(() -> {
            boolean emailSent = GmailSender.sendEmail(email, subject, message);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (emailSent) {
                    ShowMessage.showToast(ForgotPasswordActivity.this, "Chúng tôi đã gửi mã OTP đến email của bạn!");
                } else {
                    ShowMessage.showToast(ForgotPasswordActivity.this, "Gửi mã OTP thất bại!");
                }
            });
        }).start();
    }

    private boolean validate(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void updatePassword(String password) {
        ApiService apiService = ApiClient.getClient();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(etEmail.getText().toString(), password);
        Call<Void> call = apiService.updatePassword(changePasswordRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ShowMessage.showToast(ForgotPasswordActivity.this, "Cập nhật mật khẩu thành công!");
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    ShowMessage.showToast(ForgotPasswordActivity.this, "Cập nhật mật khẩu thất bại!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ShowMessage.showToast(ForgotPasswordActivity.this, "Cập nhật mật khẩu thất bại!");
            }
        });
    }

}