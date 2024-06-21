package com.dating.flirtify.Activities;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Fragments.ProcessingFragment;
import com.dating.flirtify.Fragments.RegisterSearchOptionsFragment;
import com.dating.flirtify.Fragments.RegisterStep3Fragment;
import com.dating.flirtify.Fragments.RegisterStep4Fragment;
import com.dating.flirtify.Fragments.RegisterStep5Fragment;
import com.dating.flirtify.Fragments.RegisterWantToSeeFragment;
import com.dating.flirtify.Models.Requests.RegisterRequest;
import com.dating.flirtify.Models.Responses.LoginResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Fragments.RegisterStep1Fragment;
import com.dating.flirtify.Fragments.RegisterStep2Fragment;
import com.dating.flirtify.Services.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Button nextButton;
    private ImageView ivStep;

    RegisterRequest registerRequest;
    FrameLayout frLayout;
    private Fragment currentFragment;
    private int currentStep = 0;
    RegisterStep1Fragment step1Fragment = new RegisterStep1Fragment();
    RegisterStep2Fragment step2Fragment;
    RegisterStep3Fragment step3Fragment;
    RegisterStep4Fragment step4Fragment;
    RegisterStep5Fragment step5Fragment;
    RegisterWantToSeeFragment wantToSeeFragment;
    RegisterSearchOptionsFragment registerSearchOptionsFragment;
    ProcessingFragment processingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        step2Fragment = new RegisterStep2Fragment();
        step3Fragment = new RegisterStep3Fragment();
        step4Fragment = new RegisterStep4Fragment();
        step5Fragment = new RegisterStep5Fragment(getApplicationContext());
        wantToSeeFragment = new RegisterWantToSeeFragment();
        registerSearchOptionsFragment = new RegisterSearchOptionsFragment();
        processingFragment = new ProcessingFragment();

        initializeView();

        showFragment(step1Fragment);

        eventHandler();
    }

    private void initializeView() {
        TextView tvAppName = findViewById(R.id.tvAppName);
        setColorGradient(tvAppName, ResourcesCompat.getColor(getResources(), R.color.gradient_top, null), ResourcesCompat.getColor(getResources(), R.color.gradient_center, null), ResourcesCompat.getColor(getResources(), R.color.gradient_bottom, null));

        nextButton = findViewById(R.id.btnLogin);
        ivStep = findViewById(R.id.ivStep);
        frLayout = findViewById(R.id.frLayout);

        registerRequest = new RegisterRequest();
    }

    private void setColorGradient(TextView tv, int... color) {
        TextPaint textPaint = tv.getPaint();
        float width = textPaint.measureText(tv.getText().toString());
        float height = tv.getTextSize();
        Shader textShader = new LinearGradient(0, 0, 0, height, color, null, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
        tv.setTextColor(color[0]);
    }

    public String getEmail() {
        return registerRequest.getEmail();
    }

    private void eventHandler() {


        nextButton.setOnClickListener(v -> {
            switch (currentStep) {
                case 0:
                    ivStep.setImageResource(R.drawable.register_step_2);
                    nextButton.setText("Xác nhận");
                    currentStep++;
                case 1:
                    nextButton.setText("Tiếp tục");
                    if (step1Fragment != null) {
                        String email = step1Fragment.getEmail();
                        if (step1Fragment.isValidEmail()) {
                            registerRequest.setEmail(email);
                            currentStep++;
                            showFragment(step2Fragment);
                            ivStep.setImageResource(R.drawable.register_step_3);
                        }
                    }
                    break;
                case 2:
                    if (step2Fragment != null) {
                        if (step2Fragment.isOTPValid()) {
                            currentStep++;
                            showFragment(step3Fragment);
                            ivStep.setImageResource(R.drawable.register_step_4);
                        }
                    }
                    break;
                case 3:
                    ivStep.setImageResource(R.drawable.register_step_5);
                    if (step3Fragment != null) {
                        if (step3Fragment.areFieldsValid()) {
                            registerRequest.setPw(step3Fragment.getPassword());
                            currentStep++;
                            showFragment(step4Fragment);
                        }
                    }
                    break;
                case 4:
                    ivStep.setImageResource(R.drawable.register_step_6);
                    if (step4Fragment != null) {
                        if (step4Fragment.validateFields()) {
                            registerRequest.setFullname(step4Fragment.getName());
                            registerRequest.setAge(step4Fragment.getDateOfBirth());
                            registerRequest.setGender(step4Fragment.getGender());
                            showFragment(wantToSeeFragment);
                            currentStep++;
                        }
                    }
                    break;
                case 5:
                    ivStep.setImageResource(R.drawable.register_step_7);
                    if (wantToSeeFragment.getLookingFor() != -1) {
                        registerRequest.setLooking_for(wantToSeeFragment.getLookingFor());
                        currentStep++;
                        showFragment(registerSearchOptionsFragment);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Vui lòng chọn đối tượng muốn hiển thị!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if (registerSearchOptionsFragment.getRelationshipType() != 0) {
                        registerRequest.setRelationship_type(registerSearchOptionsFragment.getRelationshipType());
                        currentStep++;
                        showFragment(step5Fragment);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Vui lòng chọn đối tượng muốn hiển thị!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 7:
                    currentStep++;
                    showFragment(processingFragment);
                    RegisterProcess();

                    break;
            }
        });
    }

    private void RegisterProcess() {
        // Lấy instance của ApiService thông qua ApiClient
        ApiService apiService = ApiClient.getClient();

        // Gửi yêu cầu đăng ký
        Call<LoginResponse> call = apiService.register(registerRequest);

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
                        SessionManager sessionManager = new SessionManager(RegisterActivity.this);
                        sessionManager.saveLoginSession(accessToken, tokenType);

                        if (step5Fragment.Upload()) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, PreviewActivity.class);
                            startActivity(intent);
                        }

                    }
                } else {
                    Log.e("API Error", "Lỗi: " + response.code());
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage(), t);
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frLayout, fragment)
                .commitNow();
        currentFragment = fragment;
    }
}


