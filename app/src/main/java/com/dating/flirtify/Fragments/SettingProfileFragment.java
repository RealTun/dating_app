package com.dating.flirtify.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.R;

public class SettingProfileFragment extends Fragment {

    private ImageView ivEditName, ivEditAge, ivEditPhone, ivEditEmail;
    private EditText etName, etAge, etPhone, etEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tìm view theo id từ layout
        ivEditName = view.findViewById(R.id.ivEditName);
        ivEditAge = view.findViewById(R.id.ivEditAge);
        ivEditPhone = view.findViewById(R.id.ivEditPhone);
        ivEditEmail = view.findViewById(R.id.ivEditEmail);

        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);

        etName.setEnabled(false);
        etAge.setEnabled(false);
        etPhone.setEnabled(false);
        etEmail.setEnabled(false);

        // Đăng ký sự kiện cho các ImageView
        ivEditName.setOnClickListener(v -> toggleEditText(etName, ivEditName, R.drawable.ic_done_green, R.drawable.icon_pencil));
        ivEditAge.setOnClickListener(v -> toggleEditText(etAge, ivEditAge, R.drawable.ic_done_green, R.drawable.icon_pencil));
        ivEditPhone.setOnClickListener(v -> toggleEditText(etPhone, ivEditPhone, R.drawable.ic_done_green, R.drawable.icon_pencil));
        ivEditEmail.setOnClickListener(v -> toggleEditText(etEmail, ivEditEmail, R.drawable.ic_done_green, R.drawable.icon_pencil));
    }

    private void toggleEditText(EditText editText, ImageView imageView, int doneIcon, int pencilIcon) {
        if (!editText.isEnabled()) {
            // Chuyển đổi thành chế độ chỉnh sửa
            editText.setEnabled(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) ContextCompat.getSystemService(requireContext(), InputMethodManager.class);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
            imageView.setImageResource(doneIcon);
        } else {
            // Chuyển đổi trở lại chế độ xem bình thường
            editText.setEnabled(false);
            imageView.setImageResource(pencilIcon);
        }
    }
}
