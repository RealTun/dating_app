package com.dating.flirtify.Fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.dating.flirtify.R;

public class AccountFragment extends Fragment {
    CardView btnAccount, btnSecurity, btnNotification, btnSetting;
    FrameLayout avatarWrapper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        eventHandler();

        return view;
    }

    private void initView(View view) {
        btnAccount = view.findViewById(R.id.btn_account);
        btnSetting = view.findViewById(R.id.btn_setting);
        btnSecurity = view.findViewById(R.id.btn_security);
        avatarWrapper = view.findViewById(R.id.fl_avatar_wrapper);
        btnNotification = view.findViewById(R.id.btn_notification);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setSize(130, 130);
        gradientDrawable.setColors(new int[]{getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom)});

        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            avatarWrapper.setBackground(gradientDrawable);
        } else {
            avatarWrapper.setBackgroundDrawable(gradientDrawable);
        }
    }

    private void eventHandler() {
        btnAccount.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            SettingProfileFragment settingFragment = new SettingProfileFragment();
            HeaderFragment headerFragment = (HeaderFragment) fragmentManager.findFragmentById(R.id.fragment_header);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingFragment).addToBackStack(null).commit();
            headerFragment.setHeaderType(4);
        });
        btnSetting.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            SettingFragment settingFragment = new SettingFragment();
            HeaderFragment headerFragment = (HeaderFragment) fragmentManager.findFragmentById(R.id.fragment_header);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingFragment).addToBackStack(null).commit();
            headerFragment.setHeaderType(4);
        });
    }
}