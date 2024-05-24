package com.dating.flirtify.UI.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dating.flirtify.R;
import com.dating.flirtify.UI.Activity.RegisterActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterStep3Fragment extends Fragment {
    private static final String TAG = "RegisterStep3Fragment";
    private EditText tvPassword, tvRePassword;
    private RegisterActivity _registerActivity;

    // Regex for password
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _registerActivity = (RegisterActivity) getActivity(); // Store reference to the activity containing this fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_step3, container, false);

        tvPassword = view.findViewById(R.id.tvPassword);
        tvRePassword = view.findViewById(R.id.tvRePassword);

        return view;
    }

    private boolean validate(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void showMessage(String message) {
//        Toast.makeText(_registerActivity, message, Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(_registerActivity)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Public method to check if fields are valid
    public boolean areFieldsValid() {
        String password = tvPassword.getText().toString();
        String rePassword = tvRePassword.getText().toString();

        if (!validate(password)) {
            showMessage("Mật khẩu phải chứa ít nhất một chữ hoa, một chữ thường, một số, một ký tự đặc biệt và dài tối thiểu 8 ký tự.");
            return false;
        }

        if (!password.equals(rePassword)) {
//            Toast.makeText(_registerActivity, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            showMessage("Mật khẩu xác nhận không khớp.");
            return false;
        }

        return true;
    }

    public String getPassword() {
        return tvPassword.getText().toString();
    }
}