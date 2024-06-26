package com.dating.flirtify.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.Activities.RegisterActivity;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.CountdownTimerHelper;
import com.dating.flirtify.Services.GmailSender;
import com.dating.flirtify.Services.OTPGenerators;
import com.dating.flirtify.Services.ShowMessage;

public class RegisterStep2Fragment extends Fragment {
    private EditText etNum1, etNum2, etNum3, etNum4, etNum5, etNum6;
    private TextView tvEmail, tvCountdownTime, tvResendOTP;
    private boolean allowResendOTP = false;
    private String generatedOTP;
    private static final String TAG = RegisterStep2Fragment.class.getSimpleName();
    private CountdownTimerHelper countdownTimer;
    private String USER_EMAIL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generatedOTP = OTPGenerators.generateOTP();
        RegisterActivity context = (RegisterActivity) getActivity();
        USER_EMAIL = context.getEmail();

        sendOTPEmail(USER_EMAIL, generatedOTP);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step2, container, false);
        initializeViews(view);
        setupOTPInputListeners();
        startCountdown();

        tvResendOTP.setOnClickListener(v -> {
            if (allowResendOTP) {
                resendOTP();
            } else {
                Toast.makeText(getContext(), "Vui lòng đợi thời gian kết thúc!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    public void initializeViews(View view) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        etNum1 = view.findViewById(R.id.etNum1);
        etNum2 = view.findViewById(R.id.etNum2);
        etNum3 = view.findViewById(R.id.etNum3);
        etNum4 = view.findViewById(R.id.etNum4);
        etNum5 = view.findViewById(R.id.etNum5);
        etNum6 = view.findViewById(R.id.etNum6);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvCountdownTime = view.findViewById(R.id.tvCountdownTime);
        tvResendOTP = view.findViewById(R.id.tvResendOTP);
        tvEmail.setText(USER_EMAIL);

    }

    private void setupOTPInputListeners() {
        addTextWatcher(null, etNum1, etNum2);
        addTextWatcher(etNum1, etNum2, etNum3);
        addTextWatcher(etNum2, etNum3, etNum4);
        addTextWatcher(etNum3, etNum4, etNum5);
        addTextWatcher(etNum4, etNum5, etNum6);
        addTextWatcher(etNum5, etNum6, null);
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
                    if (nextEditText != null) {
                        nextEditText.requestFocus();
                    }
                } else {
                    currentEditText.setText(value.substring(0, 1));
                    currentEditText.setSelection(1);
                    if (nextEditText != null) {
                        nextEditText.requestFocus();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No implementation needed
            }
        });
    }

    private void startCountdown() {
        countdownTimer = new CountdownTimerHelper(90000, new CountdownTimerHelper.CountdownListener() {
            @Override
            public void onCountdownTick(String formattedTime) {
                tvCountdownTime.setText(formattedTime);
            }

            @Override
            public void onCountdownFinish() {
                tvCountdownTime.setText("00:00");
                allowResendOTP = true;
                updateResendOTPStatus();
            }
        });
        countdownTimer.start();
    }

    private void updateResendOTPStatus() {
        int color = ContextCompat.getColor(requireContext(), allowResendOTP ? R.color.gradient_top : R.color.black);
        int typeface = allowResendOTP ? Typeface.BOLD : Typeface.NORMAL;
        tvResendOTP.setTextColor(color);
        tvResendOTP.setTypeface(null, typeface);
    }

    public boolean isOTPValid() {
        String enteredOTP = etNum1.getText().toString() + etNum2.getText().toString() + etNum3.getText().toString() + etNum4.getText().toString() + etNum5.getText().toString() + etNum6.getText().toString();
        if (countdownTimer.isCountdownFinished()) {
            Toast.makeText(getContext(), "Mã OTP đã hết hạn!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (enteredOTP.equals(generatedOTP) == false) {
            Toast.makeText(getActivity(), "OTP không đúng!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resendOTP() {
        generatedOTP = OTPGenerators.generateOTP();
        sendOTPEmail(USER_EMAIL, generatedOTP);
        startCountdown();
        allowResendOTP = false;
        updateResendOTPStatus();
        clearOTPFields();
    }

    private void sendOTPEmail(String email, String otp) {
        String subject = "Flirtify";
        String message = otp;

        new Thread(() -> {
            boolean emailSent = GmailSender.sendEmail(email, subject, message);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (emailSent) {
                    ShowMessage.showToast(getContext(), "Chúng tôi đã gửi mã OTP đến email của bạn!");
                } else {
                    ShowMessage.showToast(getContext(), "Gửi mã OTP thất bại!");
                }
            });
        }).start();
    }

    private void clearOTPFields() {
        etNum1.setText("");
        etNum2.setText("");
        etNum3.setText("");
        etNum4.setText("");
        etNum5.setText("");
        etNum6.setText("");
        etNum1.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}