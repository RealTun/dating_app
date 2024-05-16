package com.dating.flirtify.Register;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.R;
public class RegisterStep2Fragment extends Fragment {
    EditText etNum1, etNum2, etNum3, etNum4;
    TextView tvPhoneNumber, tvCountdownTime, tvResendOTP;
    boolean allowResendOTP = false;
    String phoneNumber;

    private RegisterActivity _registerActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _registerActivity = (RegisterActivity) getActivity();
        phoneNumber = _registerActivity.getPhoneNumber();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step2, container, false);

        etNum1 = view.findViewById(R.id.etNum1);
        etNum2 = view.findViewById(R.id.etNum2);
        etNum3 = view.findViewById(R.id.etNum3);
        etNum4 = view.findViewById(R.id.etNum4);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvCountdownTime = view.findViewById(R.id.tvCountdownTime);
        tvResendOTP = view.findViewById(R.id.tvResendOTP);

        tvPhoneNumber.setText(phoneNumber);

        addTextWatcher(null, etNum1, etNum2); // Không có previousEditText cho etNum1
        addTextWatcher(etNum1, etNum2, etNum3);
        addTextWatcher(etNum2, etNum3, etNum4);
        addTextWatcher(etNum3, etNum4, null); // Không có nextEditText cho etNum4

        startCountdown(tvCountdownTime);

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowResendOTP) {
                    // Implement resend OTP logic here
                } else {
                    Toast.makeText(getContext(), "Please wait for the countdown to finish", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowResendOTP) {
                    startCountdown(tvCountdownTime);
                    allowResendOTP = false;
                    isAllowResendOTP(allowResendOTP);
                    etNum1.setText("");
                    etNum2.setText("");
                    etNum3.setText("");
                    etNum4.setText("");
                    etNum1.requestFocus();
                }
            }
        });

        return view;
    }

    public void addTextWatcher(final EditText previousEditText, final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = currentEditText.getText().toString();
                if (value.length() == 0) {
                    if (previousEditText != null) {
                        previousEditText.requestFocus();
                    }
                } else if (value.length() == 1) {
                    if (nextEditText != null){
                        nextEditText.requestFocus();
                    }
                } else {
                    currentEditText.setText(value.substring(0, 1));
                    currentEditText.setSelection(1);
                    currentEditText.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public void startCountdown(TextView countdownTextView) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 5000; // 5 seconds
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long millisUntilFinished = endTime - currentTime;
                if (millisUntilFinished > 0) {
                    long minutes = millisUntilFinished / 60000;
                    long seconds = (millisUntilFinished % 60000) / 1000;
                    String formattedTime = String.format("%02d:%02d", minutes, seconds);
                    countdownTextView.setText(formattedTime);

                    new Handler().postDelayed(this, 1000);
                } else {
                    // Countdown has finished
                    countdownTextView.setText("00:00");
                    allowResendOTP = true;
                    isAllowResendOTP(allowResendOTP);
                }
            }
        });
    }


    private void isAllowResendOTP(boolean allow) {
        if (allow) {
            int color = ContextCompat.getColor(requireContext(), R.color.gradient_top);
            tvResendOTP.setTextColor(color);
            tvResendOTP.setTypeface(null, Typeface.BOLD);
        } else {
            int color = ContextCompat.getColor(requireContext(), R.color.black);
            tvResendOTP.setTextColor(color);
            tvResendOTP.setTypeface(null, Typeface.NORMAL);
        }
    }

    public boolean isOTPValid() {
        String OTP = "1234";
        String userOTP = etNum1.getText().toString() + etNum2.getText().toString() + etNum3.getText().toString() + etNum4.getText().toString();
        return OTP.equals(userOTP);
    }
}
