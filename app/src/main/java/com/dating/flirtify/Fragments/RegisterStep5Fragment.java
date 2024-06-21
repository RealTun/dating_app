package com.dating.flirtify.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.RealPathUtil;
import com.dating.flirtify.Services.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStep5Fragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_READ_FOLDERS = 101;

    private Map<Integer, Uri> imagesURIs = new HashMap<>();
    private int imageCount = 0; // Track number of images uploaded
    private int selectedImageIndex; // Vị trí của ImageView đang được chọn
    private ImageView[] imageViews; // Mảng chứa các ImageView
    private ApiService apiService;
    Context context;

    public RegisterStep5Fragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step5, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Khởi tạo các ImageView từ layout.
        imageViews = new ImageView[6];
        for (int i = 0; i < 6; i++) {
            imageViews[i] = view.findViewById(getResources().getIdentifier("imageView" + (i + 1), "id", getActivity().getPackageName()));
            final int index = i; // Create a final variable to use in the listener
            imageViews[i].setOnClickListener(v -> selectImage(index));
        }

        // Khởi tạo map với key từ 1 đến 6, và giá trị ban đầu là null
        for (int i = 1; i <= 6; i++) {
            imagesURIs.put(i, null);
        }


        return view;
    }

    private void selectImage(int index) {
        // Check if already 6 images are selected
        if (imageCount >= 6) {
            Toast.makeText(getContext(), "Bạn đã chọn đủ 6 hình ảnh", Toast.LENGTH_SHORT).show();
            return;
        }
        selectedImageIndex = index; // Ghi nhớ vị trí của ImageView
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_FOLDERS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, khởi chạy lại intent chọn hình ảnh.
                selectImage(imageCount);
            } else {
                Toast.makeText(getContext(), "Yêu cầu quyền đọc bộ nhớ để chọn hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Cập nhật ảnh tại vị trí được chọn trong Map
                imagesURIs.put(selectedImageIndex + 1, selectedImageUri); // Vị trí +1 để lấy key từ 1 đến 6
                imageViews[selectedImageIndex].setImageURI(selectedImageUri); // Hiển thị ảnh tương ứng vào ImageView
            }
            Log.d("RegisterStep5Fragment", "imagesURIs: " + imagesURIs.toString());
        }
    }

    public Boolean Upload() {
        if (imagesURIs.size() == 0) {
            Toast.makeText(getContext(), "Không có hình ảnh nào để upload", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (Map.Entry<Integer, Uri> entry : imagesURIs.entrySet()) {
            Integer key = entry.getKey();
            Uri value = entry.getValue();
            String token = SessionManager.getToken();

            if (value != null) {
                System.out.println("Key: " + key + " có giá trị: " + value);
                uploadPhoto(token, value);
            }
        }
        return true;
    }

    private void uploadPhoto(String accessToken, Uri photoUri) {
        if (context == null) {
            Log.e("Photo Upload Error", "Context is null");
            return;
        }

        String realPath = RealPathUtil.getRealPath(context, photoUri);

        File imageFile = new File(realPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        apiService = ApiClient.getClient();
        Call<Void> call = apiService.storeUserPhotos(accessToken, body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Photo Upload", response.message());
                } else {
                    Log.e("Photo Upload Error", "Lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Photo Upload Error", t.getMessage(), t);
            }
        });
    }
}
