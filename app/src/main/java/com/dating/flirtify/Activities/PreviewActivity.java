package com.dating.flirtify.Activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.dating.flirtify.Fragments.HeaderFragment;
import com.dating.flirtify.Fragments.PreviewFragment;
import com.dating.flirtify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PreviewActivity extends AppCompatActivity {

    TextView tvAppName;
    ImageButton ibArrowUp, ibArrowDown;
    BottomNavigationView footerWrapper;
    LinearLayout bottomCardWrapper;
    ConstraintLayout headerWrapper, mConstraintLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvAppName = findViewById(R.id.tv_app_name);
        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom));

        ibArrowUp = findViewById(R.id.ib_arrow_up);
        ibArrowDown = findViewById(R.id.ib_arrow_down);
        footerWrapper = findViewById(R.id.footer_wrapper);
        bottomCardWrapper = findViewById(R.id.bottom_card_wrapper);
        headerWrapper = findViewById(R.id.header_wrapper);
        mConstraintLayout = findViewById(R.id.main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        FragmentManager fragmentManager = getSupportFragmentManager();
        HeaderFragment headerFragment = (HeaderFragment) fragmentManager.findFragmentById(R.id.fragment_header);
        headerFragment.setHeaderType(1);

        PreviewFragment fragment = (PreviewFragment) fragmentManager.findFragmentById(R.id.fragment_card_view);

        ibArrowUp.setOnClickListener(v -> {
            ibArrowUp.setVisibility(View.GONE);
            footerWrapper.setVisibility(View.GONE);

            ObjectAnimator animator = ObjectAnimator.ofFloat(bottomCardWrapper, "translationY", 0, 125f);
            animator.setDuration(300);
            animator.start();
            headerFragment.setHeaderType(2);

            fragment.SlideUp();

            ConstraintSet mConstraintSet = new ConstraintSet();
            mConstraintSet.clone(mConstraintLayout);
            mConstraintSet.constrainPercentHeight(R.id.content_wrapper, 0.9f);
            mConstraintSet.applyTo(mConstraintLayout);
        });

        ibArrowDown.setOnClickListener(v -> {
            footerWrapper.setVisibility(View.VISIBLE);
            headerWrapper.setVisibility(View.VISIBLE);
            ibArrowUp.setVisibility(View.VISIBLE);

            ObjectAnimator animator = ObjectAnimator.ofFloat(bottomCardWrapper, "translationY", 125f, 0f);
            animator.setDuration(300);
            animator.start();
            headerFragment.setHeaderType(1);

            fragment.SlideDown();
            ConstraintSet mConstraintSet = new ConstraintSet();
            mConstraintSet.clone(mConstraintLayout);
            mConstraintSet.constrainPercentHeight(R.id.content_wrapper, 0.75f);
            mConstraintSet.applyTo(mConstraintLayout);
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