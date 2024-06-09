package com.dating.flirtify.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dating.flirtify.R;

public class AccountFragment extends Fragment {
    CardView btnAccount, btnSecurity, btnNotification, btnSetting;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        eventHandler();

        return view;
    }

    private void initView(View view) {
        btnAccount = view.findViewById(R.id.btn_account);
        btnSecurity = view.findViewById(R.id.btn_security);
        btnNotification = view.findViewById(R.id.btn_notification);
        btnSetting = view.findViewById(R.id.btn_setting);
    }

    private void eventHandler() {
        btnSetting.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            SettingFragment settingFragment = new SettingFragment();
            HeaderFragment headerFragment = (HeaderFragment) fragmentManager.findFragmentById(R.id.fragment_header);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, settingFragment).addToBackStack(null).commit();
            headerFragment.setHeaderType(4);
        });
    }
}