package com.dating.flirtify.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Requests.CheckEmailRequest;
import com.dating.flirtify.Models.Requests.RegisterRequest;
import com.dating.flirtify.Models.Responses.LoginResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.ShowMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStep1Fragment extends Fragment {
    private EditText etEmail;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private boolean isDuplicateEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step1, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etEmail = view.findViewById(R.id.tvEmail);
        return view;
    }

    public String getEmail() {
        return etEmail.getText().toString();
    }

    public boolean isValidEmail() {
        if (etEmail == null || etEmail.getText().toString().isEmpty()) {
            ShowMessage.showCustomDialog(getContext(), "Thông báo", "Email không được bỏ trống!");
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(etEmail.getText().toString());
        if (!matcher.matches()) {
            ShowMessage.showCustomDialog(getContext(), "Thông báo", "Email nhập vào không hợp lệ!");
            return false;
        }

        return true;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}