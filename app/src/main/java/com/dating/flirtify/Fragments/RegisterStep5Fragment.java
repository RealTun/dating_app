package com.dating.flirtify.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.util.HashMap;
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
    private static final int MAX_IMAGES = 6;

    private Map<Integer, Uri> imagesURIs = new HashMap<>();
    private int selectedImageIndex = -1; // Vị trí của ImageView đang được chọn
    private ImageView[] imageViews; // Mảng chứa các ImageView
    private ImageView[] imageViewsCloses; // Mảng chứa các ImageViewClose
    private ApiService apiService;
    private Context context;

    public RegisterStep5Fragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step5, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Khởi tạo các ImageView từ layout.
        imageViews = new ImageView[MAX_IMAGES];
        imageViewsCloses = new ImageView[MAX_IMAGES];
        for (int i = 0; i < MAX_IMAGES; i++) {
            imageViews[i] = view.findViewById(getResources().getIdentifier("im" + (i + 1), "id", getActivity().getPackageName()));
            imageViewsCloses[i] = view.findViewById(getResources().getIdentifier("imClose" + (i + 1), "id", getActivity().getPackageName()));
            final int index = i; // Tạo biến final để sử dụng trong listener
            imageViews[i].setOnClickListener(v -> selectImage(index));
            imageViewsCloses[i].setOnClickListener(v -> removeSelectImage(index)); // Set click listener for close icon
        }

        // Khởi tạo map với key từ 1 đến 6, và giá trị ban đầu là null
        for (int i = 1; i <= MAX_IMAGES; i++) {
            imagesURIs.put(i, null);
        }

        // Ban đầu, chỉ kích hoạt ImageView đầu tiên
        selectedImageIndex = 0;
        updateImageViews();

        return view;
    }

    private void selectImage(int index) {
        // Tìm vị trí đầu tiên trong imagesURIs mà có giá trị là null
        int firstNullIndex = -1;
        for (int i = 0; i < MAX_IMAGES; i++) {
            if (imagesURIs.get(i + 1) == null) {
                firstNullIndex = i + 1;
                break;
            }
        }

        // Kiểm tra điều kiện chỉ cho phép chọn ảnh ở vị trí đầu tiên có giá trị null
        if (firstNullIndex != -1) {
            selectedImageIndex = firstNullIndex - 1; // Gán vị trí đã chọn là vị trí có giá trị null
            updateImageViews(); // Cập nhật trạng thái của các ImageView
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_READ_FOLDERS);
            } else {
                // Quyền đã được cấp, khởi chạy intent chọn hình ảnh.
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        } else {
            Toast.makeText(getContext(), "Bạn đã chọn đủ " + MAX_IMAGES + " ảnh rồi", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeSelectImage(int index) {
        // Xóa ảnh khỏi Map và cập nhật lại ImageView tương ứng
        imagesURIs.remove(index + 1); // Xóa phần tử với key là index + 1

        // Dồn các phần tử phía sau lên
        for (int i = index + 1; i <= MAX_IMAGES; i++) {
            if (imagesURIs.containsKey(i + 1)) {
                imagesURIs.put(i, imagesURIs.get(i + 1));
                imagesURIs.remove(i + 1);
            } else {
                imagesURIs.put(i, null);
            }
        }
        updateImageViews(); // Cập nhật lại giao diện
    }

    private void updateImageViews() {
        boolean firstNullFound = false; // Biến để xác định đã tìm thấy URI null đầu tiên chưa
        for (int i = 0; i < imageViews.length; i++) {
            if (imagesURIs.get(i + 1) == null && !firstNullFound) {
                // Nếu không có ảnh được chọn và chưa tìm thấy URI null đầu tiên, hiển thị biểu tượng camera gradient
                imageViewsCloses[i].setVisibility(View.GONE);
                imageViews[i].setImageResource(R.drawable.icon_camera_gradient);
                imageViews[i].setEnabled(i == selectedImageIndex); // Cho phép ImageView nếu đây là vị trí được chọn hiện tại
                firstNullFound = true; // Đánh dấu đã tìm thấy URI null đầu tiên
            } else if (imagesURIs.get(i + 1) == null) {
                // Nếu không có ảnh được chọn, nhưng đã tìm thấy URI null đầu tiên, hiển thị biểu tượng camera thường
                imageViewsCloses[i].setVisibility(View.GONE);
                imageViews[i].setImageResource(R.drawable.icon_camera);
                imageViews[i].setEnabled(i == selectedImageIndex); // Cho phép ImageView nếu đây là vị trí được chọn hiện tại
            } else {
                // Nếu có ảnh được chọn, hiển thị ảnh đã chọn và hiện ImageViewClose
                imageViewsCloses[i].setVisibility(View.VISIBLE);
                imageViews[i].setImageURI(imagesURIs.get(i + 1));
                imageViews[i].setBackgroundResource(0); // Xóa nền nếu đã có ảnh được chọn
                imageViews[i].setEnabled(true); // Cho phép ImageView
            }
        }
        Log.e("imagesURIs", imagesURIs.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_FOLDERS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, khởi chạy intent chọn hình ảnh.
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
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

                // Hiển thị ImageViewClose tương ứng
                imageViewsCloses[selectedImageIndex].setVisibility(View.VISIBLE);

                // Kích hoạt ImageView tiếp theo nếu có
                if (selectedImageIndex < MAX_IMAGES - 1) {
                    selectedImageIndex++;
                    updateImageViews();
                }
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
