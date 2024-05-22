package com.dating.flirtify.Fragments;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dating.flirtify.R;

public class HeaderFragment extends Fragment {
    private ImageView ivLogo;
    private TextView tvAppName;
    private LinearLayout lnWrapper;
    private ImageButton btnFilter, btnBack, btnArrowDown;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
        tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
        lnWrapper = (LinearLayout) view.findViewById(R.id.ln_wrapper);
        btnFilter = (ImageButton) view.findViewById(R.id.ib_filter);
        btnBack = (ImageButton) view.findViewById(R.id.ib_back);
        btnArrowDown = (ImageButton) view.findViewById(R.id.ib_arrow_down);
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