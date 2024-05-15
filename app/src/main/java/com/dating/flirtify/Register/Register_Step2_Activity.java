package com.dating.flirtify.Register;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dating.flirtify.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_Step2_Activity extends AppCompatActivity {
    EditText etNum1, etNum2, etNum3, etNum4;
    TextView tvPhoneNumber, tvCountdownTime, tvResendOTP;

    Button btnContinues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_step2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        TextView tvAppName = findViewById(R.id.tvAppName);
        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom));

        etNum1 = findViewById(R.id.etNum1);
        etNum2 = findViewById(R.id.etNum2);
        etNum3 = findViewById(R.id.etNum3);
        etNum4 = findViewById(R.id.etNum4);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvCountdownTime = findViewById(R.id.tvCountdownTime);
        tvResendOTP = findViewById(R.id.tvResendOTP);

        addTextWatcher(etNum1, etNum2);
        addTextWatcher(etNum2, etNum3);
        addTextWatcher(etNum3, etNum4);

        Intent intent = getIntent();
        String NumberPhone = intent.getStringExtra("PhoneNumber");
        Log.d("passvalue", NumberPhone);
        tvPhoneNumber.setText(NumberPhone);


        startCountdown(tvCountdownTime);



    }

    private void setColorGradient(TextView tv, int... color){
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

    public void addTextWatcher(final EditText _etNum1, final EditText _etNum2) {
        _etNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = _etNum1.getText().toString(); // Lấy nội dung mới của _etNum1
                if (value.length() == 0) {
                    _etNum1.requestFocus();
                } else if (value.length() == 1) {
                    _etNum2.requestFocus();
                } else {
                    _etNum1.setText(value.substring(0, 1)); // Chỉ lấy ký tự đầu tiên
                    _etNum1.setSelection(1); // Đặt con trỏ về vị trí cuối cùng
                    _etNum1.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }


    public void startCountdown(TextView countdownTextView) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 10000; // 90000 mili giây = 1 phút 30 giây
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long millisUntilFinished = endTime - currentTime;
                if (millisUntilFinished > 0) {
                    long minutes = millisUntilFinished / 1000 / 60;
                    long seconds = millisUntilFinished / 1000 % 60;

                    String formattedTime = String.format("%02d:%02d", minutes, seconds);
                    countdownTextView.setText(formattedTime);

                    new Handler().postDelayed(this, 1000);
                } else {
                    // Đếm ngược kết thúc
                    countdownTextView.setText("00:00");
                    // Get the color resource reference from your resources
                    int blackColor = getResources().getColor(R.color.gradient_top);
                    tvResendOTP.setTextColor(blackColor);
                    tvResendOTP.setTypeface(tvResendOTP.getTypeface(), Typeface.BOLD);
                }
            }
        });
    }



}