package com.dating.flirtify.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.dating.flirtify.R;
import com.dating.flirtify.Services.ShowMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterStep3Fragment extends Fragment {
    private static final String TAG = "RegisterStep3Fragment";
    private EditText tvPassword, tvRePassword;

    // Regex for password
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step3, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tvPassword = view.findViewById(R.id.tvPassword);
        tvRePassword = view.findViewById(R.id.tvRePassword);

        return view;
    }

    private boolean validate(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean areFieldsValid() {
        String password = tvPassword.getText().toString();
        String rePassword = tvRePassword.getText().toString();

        if (!validate(password)) {
            ShowMessage.showCustomDialog(getContext(),"Thông báo", "Mật khẩu phải chứa ít nhất một chữ hoa, một chữ thường, một số, một ký tự đặc biệt và dài tối thiểu 8 ký tự.");
            return false;
        }
        if (!password.equals(rePassword)) {
            ShowMessage.showCustomDialog(getContext(),"Thông báo", "Mật khẩu xác nhận không khớp!");
            return false;
        }
        return true;
    }

    public String getPassword() {
        return tvPassword.getText().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}