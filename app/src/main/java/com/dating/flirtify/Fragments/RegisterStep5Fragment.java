package com.dating.flirtify.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.R;

import java.util.ArrayList;
import java.util.List;

public class RegisterStep5Fragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView selectedImageView;

    private static final int PERMISSION_REQUEST_READ_FOLDERS = 101;

    private List<Uri> imagesURIs = new ArrayList<Uri>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step5, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Khởi tạo các ImageView từ layout.
        ImageView imageView1 = view.findViewById(R.id.imageView1);
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        ImageView imageView3 = view.findViewById(R.id.imageView3);
        ImageView imageView4 = view.findViewById(R.id.imageView4);
        ImageView imageView5 = view.findViewById(R.id.imageView5);
        ImageView imageView6 = view.findViewById(R.id.imageView6);

        // Đặt sự kiện click cho các ImageView để chọn hình ảnh.
        imageView1.setOnClickListener(v -> selectImage(imageView1));
        imageView2.setOnClickListener(v -> selectImage(imageView2));
        imageView3.setOnClickListener(v -> selectImage(imageView3));
        imageView4.setOnClickListener(v -> selectImage(imageView4));
        imageView5.setOnClickListener(v -> selectImage(imageView5));
        imageView6.setOnClickListener(v -> selectImage(imageView6));

        return view;
    }

    private void selectImage(ImageView imageView) {
        selectedImageView = imageView;

        // Tạo intent chọn hình từ bộ nhớ ngoài.
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // Kiểm tra quyền đã được cấp chưa.
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

            // Yêu cầu quyền nếu chưa được cấp.
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_READ_FOLDERS);
        } else {
            // Quyền đã được cấp, khởi chạy intent chọn hình ảnh.
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                selectedImageView.setImageURI(selectedImageUri);
                imagesURIs.add(selectedImageUri); // Lưu URI của hình ảnh vào danh sách
            } else {
                Toast.makeText(getContext(), "Không thể chọn hình ảnh này", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_FOLDERS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, khởi chạy lại intent chọn hình ảnh.
                selectImage(selectedImageView);
            } else {
                Toast.makeText(getContext(), "Yêu cầu quyền đọc bộ nhớ để chọn hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

}