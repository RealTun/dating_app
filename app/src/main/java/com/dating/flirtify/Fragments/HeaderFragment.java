package com.dating.flirtify.Fragments;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.flirtify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDragHandleView;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

public class HeaderFragment extends Fragment {
    private ImageView ivLogo;
    private TextView tvAppName, rangeAge;
    private LinearLayout lnWrapper;
    private ImageButton ibFilter, ibBack, ibArrowDown;
    private BottomSheetDialog bottomSheetDialog;
    private RangeSlider rangeSlider;
    private PreviewFragment previewFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        initViews(view);

        ibFilter.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });

        return view;
    }

    private void initViews(View view) {
        ivLogo = view.findViewById(R.id.iv_logo);
        tvAppName = view.findViewById(R.id.tv_app_name);
        lnWrapper = view.findViewById(R.id.ln_wrapper);
        ibFilter = view.findViewById(R.id.ib_filter);
        ibBack = view.findViewById(R.id.ib_back);
        ibArrowDown = view.findViewById(R.id.ib_arrow_down);

        previewFragment = new PreviewFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, previewFragment).commit();

        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom));

        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        rangeSlider = bottomSheetDialog.findViewById(R.id.rangeSlider);
        rangeAge = bottomSheetDialog.findViewById(R.id.tv_range_age);

        rangeAge.setText("18 - 30");
        rangeSlider.setValues(18f, 30f);
        rangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            rangeAge.setText(String.format("%s - %s", values.get(0).intValue(), values.get(1).intValue()));
        });

        ibBack.setOnClickListener(v -> {
            AccountFragment accountFragment = new AccountFragment();

            fragmentManager.beginTransaction().replace(R.id.fragment_container, accountFragment).commit();
            setHeaderType(3);
        });
    }

    public void setHeaderType(int type) {
        switch (type) {
            case 1:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.VISIBLE);
                lnWrapper.setVisibility(View.GONE);
                ibFilter.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
                ibArrowDown.setVisibility(View.GONE);
                break;
            case 2:
                ivLogo.setVisibility(View.GONE);
                tvAppName.setVisibility(View.GONE);
                lnWrapper.setVisibility(View.VISIBLE);
                ibFilter.setVisibility(View.GONE);
                ibBack.setVisibility(View.GONE);
                ibArrowDown.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.VISIBLE);
                lnWrapper.setVisibility(View.GONE);
                ibFilter.setVisibility(View.GONE);
                ibBack.setVisibility(View.GONE);
                ibArrowDown.setVisibility(View.GONE);
                break;
            case 4:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.VISIBLE);
                lnWrapper.setVisibility(View.GONE);
                ibFilter.setVisibility(View.GONE);
                ibBack.setVisibility(View.VISIBLE);
                ibArrowDown.setVisibility(View.GONE);
                break;
        }
    }


    private void setColorGradient(TextView tv, int... color) {
        float height = tv.getTextSize();

        Shader textShader = new LinearGradient(0, 0, 0, height, color, null, Shader.TileMode.CLAMP);

        tv.getPaint().setShader(textShader);
        tv.setTextColor(color[0]);
    }
}