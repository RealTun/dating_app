package com.dating.flirtify.Register;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dating.flirtify.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterStep1Fragment extends Fragment {
    EditText etNumberPhone;
    private static final String PHONE_NUMBER_PATTERN = "^(?:\\+?\\d{1,3}[-.\\s]?)?(?:\\(\\d{3}\\)|\\d{3})[-.\\s]?\\d{3}[-.\\s]?\\d{4}$";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step1, container, false);

        etNumberPhone = view.findViewById(R.id.etNumberPhone);

        return view;
    }

    public String getPhoneNumber() {
        return etNumberPhone.getText().toString();
    }

    public void showInvalidPhoneNumberAlert() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Lỗi")
                .setMessage("Số điện thoại không hợp lệ!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng hộp thoại
                    }
                })
                .show();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
