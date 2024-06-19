package com.dating.flirtify.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.GmailSender;
import com.dating.flirtify.Services.OTPGenerators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterStep1Fragment extends Fragment {
    private EditText etEmail;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step1, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etEmail = view.findViewById(R.id.tvEmail);  // Ensure R.id.tvEmail is an EditText in your XML
        return view;
    }

    public String getEmail() {
        return etEmail.getText().toString();
    }


    private void showInvalidEmailAlert() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Lỗi")
                .setMessage("Email không hợp lệ!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog
                    }
                })
                .show();
    }

    public boolean isValidEmail() {
        if (etEmail == null || etEmail.getText().toString().isEmpty()) {
            showInvalidEmailAlert();
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(etEmail.getText().toString());
        if (!matcher.matches()) {
            showInvalidEmailAlert();
            return false;
        }
        return true;
    }
}