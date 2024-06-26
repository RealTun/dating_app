package com.dating.flirtify.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Fragments.ProcessingFragment;
import com.dating.flirtify.Fragments.RegisterSearchOptionsFragment;
import com.dating.flirtify.Fragments.RegisterStep1Fragment;
import com.dating.flirtify.Fragments.RegisterStep2Fragment;
import com.dating.flirtify.Fragments.RegisterStep3Fragment;
import com.dating.flirtify.Fragments.RegisterStep4Fragment;
import com.dating.flirtify.Fragments.RegisterStep5Fragment;
import com.dating.flirtify.Fragments.RegisterWantToSeeFragment;
import com.dating.flirtify.Models.Requests.RegisterRequest;
import com.dating.flirtify.Models.Responses.LoginResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.LocationHelper;
import com.dating.flirtify.Services.NetworkChangeReceiver;
import com.dating.flirtify.Services.SessionManager;
import com.dating.flirtify.Services.ShowMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements LocationHelper.LocationResultListener {
    private Button nextButton;
    private ImageView ivStep;

    RegisterRequest registerRequest;
    FrameLayout frLayout;
    private Fragment currentFragment;
    private int currentStep = 0;
    RegisterStep1Fragment step1Fragment;
    RegisterStep2Fragment step2Fragment;
    RegisterStep3Fragment step3Fragment;
    RegisterStep4Fragment step4Fragment;
    RegisterStep5Fragment step5Fragment;
    RegisterWantToSeeFragment wantToSeeFragment;
    RegisterSearchOptionsFragment registerSearchOptionsFragment;
    ProcessingFragment processingFragment;
    LocationHelper locationHelper;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Đăng ký BroadcastReceiver
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);


        // Khởi tạo LocationHelper và thiết lập listener
        locationHelper = new LocationHelper(this);
        locationHelper.setLocationResultListener(this);

        // Yêu cầu quyền truy cập vị trí
        locationHelper.requestLocationPermission();


        initializeView();

        showFragment(step1Fragment);

        eventHandler();
    }

    private void initializeView() {
        TextView tvAppName = findViewById(R.id.tvAppName);
        setColorGradient(tvAppName, ResourcesCompat.getColor(getResources(), R.color.gradient_top, null), ResourcesCompat.getColor(getResources(), R.color.gradient_center, null), ResourcesCompat.getColor(getResources(), R.color.gradient_bottom, null));

        nextButton = findViewById(R.id.btnReset);
        ivStep = findViewById(R.id.ivStep);
        frLayout = findViewById(R.id.frLayout);

        registerRequest = new RegisterRequest();
        step1Fragment = new RegisterStep1Fragment();
        step2Fragment = new RegisterStep2Fragment();
        step3Fragment = new RegisterStep3Fragment();
        step4Fragment = new RegisterStep4Fragment();
        step5Fragment = new RegisterStep5Fragment(getApplicationContext());
        wantToSeeFragment = new RegisterWantToSeeFragment();
        registerSearchOptionsFragment = new RegisterSearchOptionsFragment();
        processingFragment = new ProcessingFragment();
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
                    nextButton.setText("Xác nhận");
                    currentStep++;
                case 1:
                    nextButton.setText("Tiếp tục");
                    if (step1Fragment != null) {
                        String email = step1Fragment.getEmail();
                        if (step1Fragment.isValidEmail()) {
                            registerRequest.setEmail(email);
                            checkDuplicateEmail(email);
                        }
                    }
                    break;
                case 2:
                    if (step2Fragment != null) {
                        if (step2Fragment.isOTPValid()) {
                            currentStep++;
                            showFragment(step3Fragment);
                            ivStep.setImageResource(R.drawable.register_step_3);
                        }
                    }
                    break;
                case 3:
                    if (step3Fragment != null) {
                        if (step3Fragment.areFieldsValid()) {
                            registerRequest.setPw(step3Fragment.getPassword());
                            currentStep++;
                            showFragment(step4Fragment);
                            ivStep.setImageResource(R.drawable.register_step_4);
                        }
                    }
                    break;
                case 4:
                    if (step4Fragment != null) {
                        if (step4Fragment.validateFields()) {
                            registerRequest.setFullname(step4Fragment.getName());
                            registerRequest.setAge(step4Fragment.getDateOfBirth());
                            registerRequest.setGender(step4Fragment.getGender());
                            showFragment(wantToSeeFragment);
                            currentStep++;
                            ivStep.setImageResource(R.drawable.register_step_5);
                        }
                    }
                    break;
                case 5:
                    if (wantToSeeFragment.getLookingFor() != -1) {
                        registerRequest.setLooking_for(wantToSeeFragment.getLookingFor());
                        currentStep++;
                        showFragment(registerSearchOptionsFragment);
                        ivStep.setImageResource(R.drawable.register_step_6);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Vui lòng chọn đối tượng muốn hiển thị!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if (registerSearchOptionsFragment.getRelationshipType() != 0) {
                        registerRequest.setRelationship_type(registerSearchOptionsFragment.getRelationshipType());
                        currentStep++;
                        showFragment(step5Fragment);
                        ivStep.setImageResource(R.drawable.register_step_7);
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
                        String accessToken = loginResponse.getAccessToken();
                        String tokenType = loginResponse.getTokenType();

                        SessionManager sessionManager = new SessionManager(RegisterActivity.this);
                        sessionManager.saveLoginSession(accessToken, tokenType);

                        if (step5Fragment.Upload()) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, PreviewActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
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

    public void checkDuplicateEmail(String email) {
        // Lấy instance của ApiService thông qua ApiClient
        ApiService apiService = ApiClient.getClient();

        // Gửi yêu cầu đăng ký
        Call<Void> call = apiService.checkDuplicateEmail(email);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi yêu cầu thành công
                    Log.d("checkDuplicateEmail", "Email is unique!");
                    showFragment(step2Fragment);
                    ivStep.setImageResource(R.drawable.register_step_2);
                    currentStep++;
                } else {
                    // Xử lý khi yêu cầu không thành công
                    ShowMessage.showCustomDialog(RegisterActivity.this, "Thông báo", "Email đã tồn tại");
                    Log.e("checkDuplicateEmail", "Duplicate email found!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frLayout, fragment)
                .commitNow();
        currentFragment = fragment;
    }

    @Override
    public void onLocationReceived(Location location, String address) {
        if (location != null) {
            registerRequest.setLocation(address);
            // Hiển thị thông tin vị trí và quận/huyện
//            Toast.makeText(this, "Lat: " + location.getLatitude() + ", Lon:" + location.getLongitude(), Toast.LENGTH_LONG).show();
//            Toast.makeText(this, "Vị trí hiện tại: " + address, Toast.LENGTH_LONG).show();
//            double distance = DistanceCalculator.calculateDistanceForAddress(this, address, "175 Tây Sơn, Đống Đa, Hà Nội, Việt Nam");
//            Toast.makeText(this, "Khoảng cách là: " + Double.toString(distance), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không thể lấy được vị trí hiện tại", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức này được gọi khi người dùng cấp quyền hoặc không
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Xử lý kết quả yêu cầu quyền vị trí
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, lấy vị trí hiện tại
                locationHelper.getCurrentLocation();
            } else {
                // Quyền không được cấp, thông báo cho người dùng
                Toast.makeText(this, "Ứng dụng cần quyền truy cập vị trí để hoạt động", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên khi hoạt động bị hủy
        if (locationHelper != null) {
            locationHelper = null;
        }
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}

