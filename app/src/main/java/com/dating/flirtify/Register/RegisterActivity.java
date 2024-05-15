package com.dating.flirtify.Register;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.dating.flirtify.R;

public class RegisterActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Button nextButton;
    private ViewPagerAdapter adapter;
    String phoneNumber="";

    // Method tạo mới fragment với giá trị PhoneNumber truyền vào

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        TextView tvAppName = findViewById(R.id.tvAppName);
        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom));


        viewPager = findViewById(R.id.viewPager2);
        nextButton = findViewById(R.id.nextButton);

        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Disable user input for ViewPager2
        viewPager.setUserInputEnabled(false);



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        RegisterStep1Fragment step1Fragment = (RegisterStep1Fragment) adapter.getFragment(currentItem);
                        if (step1Fragment != null) {
                            phoneNumber = step1Fragment.getPhoneNumber();
                            if (RegisterStep1Fragment.isValidPhoneNumber(phoneNumber)) {
                                viewPager.setCurrentItem(currentItem + 1);
                            } else {
                                step1Fragment.showInvalidPhoneNumberAlert();
                            }
                        }
                        break;
                    case 1:
                        RegisterStep2Fragment step2Fragment = (RegisterStep2Fragment) adapter.getFragment(currentItem);
                        if (step2Fragment != null) {
                            if (step2Fragment.isOTPValid()) {
                                viewPager.setCurrentItem(currentItem + 1);
                            } else {
                                Toast.makeText(RegisterActivity.this, "OTP không đúng!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case 2:
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 3:
                        break;


                    // Add more cases for other steps if needed
                }
            }
        });
    }


    public String getPhoneNumber() {
        return phoneNumber;
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
}