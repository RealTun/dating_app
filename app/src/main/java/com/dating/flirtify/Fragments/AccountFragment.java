package com.dating.flirtify.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dating.flirtify.Activities.SettingProfileActivity;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;

import retrofit2.Call;

public class AccountFragment extends Fragment {
    private ApiService apiService;
    private CardView btnAccount, btnSecurity, btnNotification, btnSetting;
    private FrameLayout avatarWrapper;
    private TextView tvFullname, tvAge;
    private ImageView ivProfile;
    private ImageButton ibEditProfile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        eventHandler();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra mã yêu cầu và mã kết quả
        if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
            getCurrentUser();
        }
    }

    private void initView(View view) {
        btnAccount = view.findViewById(R.id.btn_account);
        btnSetting = view.findViewById(R.id.btn_setting);
        btnSecurity = view.findViewById(R.id.btn_security);
        avatarWrapper = view.findViewById(R.id.fl_avatar_wrapper);
        btnNotification = view.findViewById(R.id.btn_notification);
        tvFullname = view.findViewById(R.id.tv_fullname);
        tvAge = view.findViewById(R.id.tv_age);
        ivProfile = view.findViewById(R.id.profile_image);
        ibEditProfile = view.findViewById(R.id.ib_edit_profile);
        apiService = ApiClient.getClient();

        getCurrentUser();

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
        ibEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingProfileActivity.class);
            startActivityForResult(intent, 9); // Sử dụng một mã yêu cầu tùy ý, ví dụ 1
        });
    }

    private void getCurrentUser() {
        String accessToken = SessionManager.getToken();
        Call<UserResponse> call = apiService.getUser(accessToken);
        call.enqueue(new retrofit2.Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    tvFullname.setText(userResponse.getFullname());
                    tvAge.setText(String.valueOf(userResponse.getAge()));
                    Glide.with(getActivity()).load(userResponse.getAvatar()).circleCrop().into(ivProfile);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}