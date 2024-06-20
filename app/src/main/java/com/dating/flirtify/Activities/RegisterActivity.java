package com.dating.flirtify.Activities;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

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
import com.dating.flirtify.Adapters.ViewPagerAdapter;
import com.dating.flirtify.Services.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Button nextButton;
    private ViewPagerAdapter adapter;
    private ImageView ivStep;

    RegisterRequest registerRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeView();
        eventHandler();
    }

    private void initializeView() {
        TextView tvAppName = findViewById(R.id.tvAppName);
        setColorGradient(tvAppName, ResourcesCompat.getColor(getResources(), R.color.gradient_top, null), ResourcesCompat.getColor(getResources(), R.color.gradient_center, null), ResourcesCompat.getColor(getResources(), R.color.gradient_bottom, null));

        viewPager = findViewById(R.id.viewPager2);
        nextButton = findViewById(R.id.btnLogin);
        ivStep = findViewById(R.id.ivStep);

        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);
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
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        ivStep.setImageResource(R.drawable.register_step_2);
                        nextButton.setText("Xác nhận");
                        RegisterStep1Fragment step1Fragment = (RegisterStep1Fragment) adapter.getFragment(currentItem);
//                        if (step1Fragment != null) {
//                            String email = step1Fragment.getEmail();
//                            if (step1Fragment.isValidEmail()) {
//                                registerRequest.setEmail(email);
//                                viewPager.setCurrentItem(currentItem + 1);
//                            }
//                        }
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 1:
                        ivStep.setImageResource(R.drawable.register_step_3);
                        nextButton.setText("Tiếp tục");
                        RegisterStep2Fragment step2Fragment = (RegisterStep2Fragment) adapter.getFragment(currentItem);
//                        if (step2Fragment != null) {
//                            if (step2Fragment.isOTPValid()) {
//                                viewPager.setCurrentItem(currentItem + 1);
//                            }
//                        }
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 2:
                        ivStep.setImageResource(R.drawable.register_step_4);
                        RegisterStep3Fragment step3Fragment = (RegisterStep3Fragment) adapter.getFragment(currentItem);
//                        if (step3Fragment != null) {
//                            if (step3Fragment.areFieldsValid()) {
//                                registerRequest.setPassword(step3Fragment.getPassword());
//                                viewPager.setCurrentItem(currentItem + 1);
//                            }
//                        }
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 3:
                        ivStep.setImageResource(R.drawable.register_step_5);
                        RegisterStep4Fragment step4Fragment = (RegisterStep4Fragment) adapter.getFragment(currentItem);
//                        if (step4Fragment != null) {
//                            if (step4Fragment.validateFields()) {
//                                registerRequest.setFullname(step4Fragment.getName());
////                                registerRequest.setAge(step4Fragment.getDateOfBirth());
//                                registerRequest.setGender(step4Fragment.getGender());
//                                viewPager.setCurrentItem(currentItem + 1);
//                            }
//                        }
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 4:
                        ivStep.setImageResource(R.drawable.register_step_6);
                        RegisterWantToSeeFragment wantToSeeFragment = (RegisterWantToSeeFragment) adapter.getFragment(currentItem);
//                        if (wantToSeeFragment.getLookingFor() != -1) {
//                            registerRequest.setLooking_for(wantToSeeFragment.getLookingFor());
//                            viewPager.setCurrentItem(currentItem + 1);
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "Vui lòng chọn đối tượng muốn hiển thị!", Toast.LENGTH_SHORT).show();
//                        }
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 5:
                        ivStep.setImageResource(R.drawable.register_step_7);
                        RegisterSearchOptionsFragment registerSearchOptionsFragment = (RegisterSearchOptionsFragment) adapter.getFragment(currentItem);
                        if (registerSearchOptionsFragment.getRelationshipType() != 0) {
                            registerRequest.setRelationship_type(registerSearchOptionsFragment.getRelationshipType());
                            viewPager.setCurrentItem(currentItem + 1);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Vui lòng chọn đối tượng muốn hiển thị!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 6:
                        ivStep.setVisibility(View.GONE);
                        RegisterStep5Fragment step5Fragment = (RegisterStep5Fragment) adapter.getFragment(currentItem);
                        nextButton.setText("Tiếp theo");
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 7:
                        ProcessingFragment processingFragment = (ProcessingFragment) adapter.getFragment(currentItem);
                        RegisterProcess();
                        break;
                }
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

                        Toast.makeText(RegisterActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn hình chính
                        Intent intent = new Intent(RegisterActivity.this, PreviewActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Log.e("API Error", "Lỗi: " + response.code());
                    Toast.makeText(RegisterActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage(), t);
            }
        });
    }


}
