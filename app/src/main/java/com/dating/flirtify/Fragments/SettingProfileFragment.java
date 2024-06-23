package com.dating.flirtify.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.Activities.MainActivity;
import com.dating.flirtify.Activities.PreviewActivity;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingProfileFragment extends Fragment {

    private ImageView ivEditName, ivEditAge, ivEditPhone, ivEditEmail;
    private EditText etName, etAge, etPhone, etEmail;
    Button btnDeleteAccount;

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
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);

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

        btnDeleteAccount.setOnClickListener(v -> {
            showDeleteConfirmationDialog();
        });
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

    // Hiển thị dialog xác nhận xóa tài khoản
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm_delete, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                // Gọi phương thức xóa tài khoản ở đây
                deleteUserAccount();
            }
        });

        alertDialog.show();
    }

    // Phương thức xóa tài khoản
    private void deleteUserAccount() {
        ApiService apiService = ApiClient.getClient();

        // Chuẩn bị dữ liệu cần thiết
        String accessToken = SessionManager.getToken();  // Thay thế bằng access token của bạn
        int userIdToDelete = 123;  // Thay thế bằng ID của người dùng cần xóa

        // Gọi phương thức API để xóa người dùng
        Call<Void> call = apiService.deleteUserInterest(accessToken, userIdToDelete);

        // Thực hiện yêu cầu bất đồng bộ
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công khi xóa người dùng
                    Log.d("DeleteUser", "Deleted successfully");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    // Xử lý lỗi khi không xóa được người dùng (có thể do lỗi phân quyền hoặc lỗi server)
                    Log.e("DeleteUser", "Failed to delete user. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi kết nối hoặc lỗi trong quá trình gửi yêu cầu
                Log.e("DeleteUser", "Error: " + t.getMessage());
            }
        });

    }
}
