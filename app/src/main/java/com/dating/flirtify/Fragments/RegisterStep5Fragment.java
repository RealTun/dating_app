package com.dating.flirtify.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.R;

import static android.app.Activity.RESULT_OK;

public class RegisterStep5Fragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView selectedImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step5, container, false);

        ImageView imageView1 = view.findViewById(R.id.imageView1);
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        ImageView imageView3 = view.findViewById(R.id.imageView3);
        ImageView imageView4 = view.findViewById(R.id.imageView4);
        ImageView imageView5 = view.findViewById(R.id.imageView5);
        ImageView imageView6 = view.findViewById(R.id.imageView6);

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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            selectedImageView.setImageURI(imageUri);
        }
    }
}
