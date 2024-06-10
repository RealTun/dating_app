package com.dating.flirtify.Fragments;

import static android.app.Activity.RESULT_OK;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

public class RegisterStep5Fragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView selectedImageView;
    private static final int PERMISSION_REQUEST_READ_FOLDERS = 101;
//    FirebaseStorage storage = FirebaseStorage.getInstance();

    private List<Uri> imagesURIs = new ArrayList<Uri>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step5, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ImageView imageView1 = view.findViewById(R.id.imageView1);
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        ImageView imageView3 = view.findViewById(R.id.imageView3);
        ImageView imageView4 = view.findViewById(R.id.imageView4);
        ImageView imageView5 = view.findViewById(R.id.imageView5);
        ImageView imageView6 = view.findViewById(R.id.imageView6);

//        imageView1.setOnClickListener(v -> selectImage(imageView1));
//        imageView2.setOnClickListener(v -> selectImage(imageView2));
//        imageView3.setOnClickListener(v -> selectImage(imageView3));
//        imageView4.setOnClickListener(v -> selectImage(imageView4));
//        imageView5.setOnClickListener(v -> selectImage(imageView5));
//        imageView6.setOnClickListener(v -> selectImage(imageView6));

        return view;
    }

//    private void selectImage(ImageView imageView) {
//        selectedImageView = imageView;
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//
//        // Kiểm tra xem quyền đã được cấp chưa trước khi bắt đầu activity.
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // Yêu cầu quyền nếu chưa được cấp.
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO}, PERMISSION_REQUEST_READ_FOLDERS);
//        } else {
//            // Quyền đã được cấp. Bạn có thể bắt đầu activity chọn ảnh.
//            startActivityForResult(intent, PICK_IMAGE_REQUEST);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == PERMISSION_REQUEST_READ_FOLDERS) {
//            // Kiểm tra xem tất cả quyền có được cấp không.
//            boolean allPermissionsGranted = true;
//            for (int grantResult : grantResults) {
//                if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                    allPermissionsGranted = false;
//                    break;
//                }
//            }
//
//            if (allPermissionsGranted) {
//                // Quyền được cấp, bạn có thể bắt đầu activity chọn ảnh.
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//                startActivityForResult(intent, PICK_IMAGE_REQUEST);
//            } else {
//                // Quyền bị từ chối. Xử lý tình huống này tương ứng.
//            }
//        }
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
//            selectedImageView.setImageURI(imageUri);
//            imagesURIs.add(imageUri);
//
//            StorageReference storageRef = storage.getReference();
//            // Tạo tham chiếu đến thư mục trên Firebase Storage
//            StorageReference imagesRef = storageRef.child("images/" + UUID.randomUUID().toString() + ".jpg");
//
//            // Tải tệp lên Firebase Storage
//            UploadTask uploadTask = imagesRef.putFile(imageUri);
//
//            uploadTask.addOnFailureListener(exception -> {
//                // Xử lý khi tải lên thất bại và hiển thị thông báo lỗi
//                Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//            }).addOnSuccessListener(taskSnapshot -> {
//                // Xử lý khi tải lên thành công và hiển thị thông báo
//                Toast.makeText(requireContext(), "Hình ảnh đã được tải lên thành công", Toast.LENGTH_SHORT).show();
//
//                // Lấy URL của hình ảnh đã tải lên
//                imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                    String imageUrl = uri.toString();
//                    // Ở đây bạn có thể làm gì đó với URL, ví dụ: lưu vào cơ sở dữ liệu hoặc hiển thị ảnh
//                    // imageUrl có thể được lưu vào Firestore hoặc Realtime Database
//                    // Ví dụ: saveImageUrlToDatabase(imageUrl);
//                });
//            });
//
//
//        }
//    }


}
