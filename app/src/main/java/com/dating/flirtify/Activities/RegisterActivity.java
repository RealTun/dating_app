package com.dating.flirtify.Activities;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.dating.flirtify.Fragments.RegisterStep3Fragment;
import com.dating.flirtify.Fragments.RegisterStep4Fragment;
import com.dating.flirtify.Fragments.RegisterStep5Fragment;
import com.dating.flirtify.Fragments.RegisterWantToSeeFragment;
import com.dating.flirtify.R;
import com.dating.flirtify.Fragments.RegisterSearchOptionsFragment;
import com.dating.flirtify.Fragments.RegisterStep1Fragment;
import com.dating.flirtify.Fragments.RegisterStep2Fragment;
import com.dating.flirtify.Adapters.ViewPagerAdapter;

public class RegisterActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Button nextButton;
    private ViewPagerAdapter adapter;

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
        viewPager.setUserInputEnabled(false);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        RegisterStep1Fragment step1Fragment = (RegisterStep1Fragment) adapter.getFragment(currentItem);
//                        if (step1Fragment != null) {
//                            String email = step1Fragment.getEmail();
//                            if (step1Fragment.isValidEmail()) {
//                                registerData.setEmailAddress(email);
//                                viewPager.setCurrentItem(currentItem + 1);
//                            }
//                        }
                        viewPager.setCurrentItem(currentItem + 1);
                        nextButton.setText("Xác nhận");
                        break;
                    case 1:
                        RegisterStep2Fragment step2Fragment = (RegisterStep2Fragment) adapter.getFragment(currentItem);
//                        if (step2Fragment != null) {
//                            if (step2Fragment.isOTPValid()) {
//                                viewPager.setCurrentItem(currentItem + 1);
//                            }
//                        }
                        viewPager.setCurrentItem(currentItem + 1);
                        nextButton.setText("Tiếp tục");
                        break;
                    case 2:
                        RegisterStep3Fragment step3Fragment = (RegisterStep3Fragment) adapter.getFragment(currentItem);
//                        if (step3Fragment != null) {
//                            if (step3Fragment.areFieldsValid()) {
//                                registerData.setPassword(step3Fragment.getPassword());
//                                viewPager.setCurrentItem(currentItem + 1);
//                            }
//                        }
                        viewPager.setCurrentItem(currentItem + 1);

                        break;
                    case 3:
                        RegisterStep4Fragment step4Fragment = (RegisterStep4Fragment) adapter.getFragment(currentItem);
//                        if (step4Fragment != null) {
//                            if (step4Fragment.validateFields()) {
//                                registerData.setName(step4Fragment.getName());
//                                registerData.setBirthdate(step4Fragment.getDateOfBirth());
//                                registerData.setGender(step4Fragment.getGender());
//                                viewPager.setCurrentItem(currentItem + 1);
//                            }
//                        }
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 4:
                        RegisterStep5Fragment step5Fragment = (RegisterStep5Fragment) adapter.getFragment(currentItem);
                        nextButton.setText("Tiếp theo");
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 5:
                        RegisterWantToSeeFragment wantToSeeFragment = (RegisterWantToSeeFragment) adapter.getFragment(currentItem);
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                    case 6:
//                        RegisterSearchOptionsFragment registerSearchOptionsFragment = (RegisterSearchOptionsFragment)
                                adapter.getFragment(currentItem);
                        viewPager.setCurrentItem(currentItem + 1);
                        break;
                }
            }
        });
    }

    private void setColorGradient(TextView tv, int... color) {
        TextPaint textPaint = tv.getPaint();
        float width = textPaint.measureText(tv.getText().toString());
        float height = tv.getTextSize();
        Shader textShader = new LinearGradient(0, 0, 0, height, color, null, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
        tv.setTextColor(color[0]);
    }
}
