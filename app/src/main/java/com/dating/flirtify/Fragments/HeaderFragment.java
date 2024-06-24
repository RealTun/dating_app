package com.dating.flirtify.Fragments;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.flirtify.Activities.PreviewActivity;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeaderFragment extends Fragment {
    private FragmentManager fragmentManager;
    private ImageView ivLogo;
    private TextView tvAppName, tvFullname, tvAge, rangeAge;
    private ImageButton ibFilter, ibBack, ibArrowDown;
    private RadioButton rbMale, rbFemale, rbOther;
    private BottomSheetDialog bottomSheetDialog;
    private View bottomSheetView;
    private RangeSlider rangeSlider;
    private PreviewFragment previewFragment;
    private UserResponse user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        initViews(view);
        eventHandler();
        return view;
    }

    public void eventHandler() {
        ibFilter.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });
        rangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            rangeAge.setText(String.format("%s - %s", values.get(0).intValue(), values.get(1).intValue()));
        });

        ibBack.setOnClickListener(v -> {
            AccountFragment accountFragment = new AccountFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, accountFragment).commit();
            setHeaderType(3);
        });
        bottomSheetDialog.setOnDismissListener(dialog -> {
            Toast.makeText(requireActivity(), "Dismissed", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews(@NonNull View view) {
        ivLogo = view.findViewById(R.id.iv_logo);
        tvAppName = view.findViewById(R.id.tv_app_name);
        tvFullname = view.findViewById(R.id.tv_fullname);
        tvAge = view.findViewById(R.id.tv_age);
        ibFilter = view.findViewById(R.id.ib_filter);
        ibBack = view.findViewById(R.id.ib_back);
        ibArrowDown = view.findViewById(R.id.ib_arrow_down);

        previewFragment = new PreviewFragment();
        fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, previewFragment).commit();

        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom));

        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        getCurrentUser();

        rangeSlider = bottomSheetDialog.findViewById(R.id.rangeSlider);
        rangeAge = bottomSheetDialog.findViewById(R.id.tv_range_age);

        rangeAge.setText("18 - 30");
        rangeSlider.setValues(18f, 30f);
    }

    public void setHeaderType(int type) {
        switch (type) {
            case 1:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.VISIBLE);
                tvFullname.setVisibility(View.GONE);
                tvAge.setVisibility(View.GONE);
                ibFilter.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
                ibArrowDown.setVisibility(View.GONE);
                break;
            case 2:
                ivLogo.setVisibility(View.GONE);
                tvAppName.setVisibility(View.GONE);
                tvFullname.setVisibility(View.VISIBLE);
                tvAge.setVisibility(View.VISIBLE);
                ibFilter.setVisibility(View.GONE);
                ibBack.setVisibility(View.GONE);
                ibArrowDown.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.VISIBLE);
                tvFullname.setVisibility(View.GONE);
                tvAge.setVisibility(View.GONE);
                ibFilter.setVisibility(View.GONE);
                ibBack.setVisibility(View.GONE);
                ibArrowDown.setVisibility(View.GONE);
                break;
            case 4:
                ivLogo.setVisibility(View.VISIBLE);
                tvAppName.setVisibility(View.VISIBLE);
                tvFullname.setVisibility(View.GONE);
                tvAge.setVisibility(View.GONE);
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

    public void setCurrentUser(UserResponse user) {
        tvFullname.setText(user.getFullname());
        tvAge.setText(String.valueOf(user.getAge()));
    }

    public void getCurrentUser() {
        ApiService apiService = ApiClient.getClient();
        String accessToken = SessionManager.getToken();
        Call<UserResponse> call = apiService.getUser(accessToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    int gender = user.getLooking_for();
                    rbMale = bottomSheetDialog.findViewById(R.id.rb_male);
                    rbFemale = bottomSheetDialog.findViewById(R.id.rb_female);
                    rbOther = bottomSheetDialog.findViewById(R.id.rb_other);
                    if (gender == 0) {
                        rbMale.setChecked(true);
                        rbFemale.setChecked(false);
                        rbOther.setChecked(false);
                    } else if (gender == 1) {
                        rbMale.setChecked(false);
                        rbFemale.setChecked(true);
                        rbOther.setChecked(false);
                    } else {
                        rbMale.setChecked(false);
                        rbFemale.setChecked(false);
                        rbOther.setChecked(true);
                    }
                } else {
                    Log.e("getUser", response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
}
