package com.dating.flirtify.Fragments;

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
import androidx.fragment.app.Fragment;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDragHandleView;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

public class HeaderFragment extends Fragment {
    private ImageView ivLogo;
    private TextView tvAppName, rangeAge;
    private LinearLayout lnWrapper;
    private ImageButton btnFilter, btnBack, btnArrowDown;
    private BottomSheetDialog bottomSheetDialog;
    private RangeSlider rangeSlider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        initViews(view);

        btnFilter.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });

        rangeAge.setText("18 - 30");
        rangeSlider.setValues(18f, 30f);
        rangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            rangeAge.setText(String.format("%s - %s", values.get(0).intValue(), values.get(1).intValue()));
        });

        return view;
    }

    private void initViews(View view) {
        ivLogo = view.findViewById(R.id.iv_logo);
        tvAppName = view.findViewById(R.id.tv_app_name);
        lnWrapper = view.findViewById(R.id.ln_wrapper);
        btnFilter = view.findViewById(R.id.ib_filter);
        btnBack = view.findViewById(R.id.ib_back);
        btnArrowDown = view.findViewById(R.id.ib_arrow_down);

        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        rangeSlider = bottomSheetDialog.findViewById(R.id.rangeSlider);
        rangeAge = bottomSheetDialog.findViewById(R.id.tv_range_age);
    }

    public void setHeaderType(int type) {
        switch (type) {
            case 1:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.VISIBLE);
                lnWrapper.setVisibility(View.GONE);
                btnFilter.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.GONE);
                btnArrowDown.setVisibility(View.GONE);
                break;
            case 2:
                ivLogo.setVisibility(View.GONE);
                tvAppName.setVisibility(View.GONE);
                lnWrapper.setVisibility(View.VISIBLE);
                btnFilter.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                btnArrowDown.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.GONE);
                lnWrapper.setVisibility(View.GONE);
                btnFilter.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                btnArrowDown.setVisibility(View.GONE);
                break;
            case 4:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.GONE);
                lnWrapper.setVisibility(View.GONE);
                btnFilter.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                btnArrowDown.setVisibility(View.VISIBLE);
                break;
        }
    }
}