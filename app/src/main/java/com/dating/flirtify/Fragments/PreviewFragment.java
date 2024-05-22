package com.dating.flirtify.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dating.flirtify.R;

public class PreviewFragment extends Fragment {
    private View view;
    private CardView cvPreview, detailInformationWrapper; // Declare a member variable to store the CardView reference
    private LinearLayout infoWrapper;
    private ScrollView svInformation;
    private ConstraintLayout previewAvatarWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preview, container, false);
        cvPreview = (CardView) view.findViewById(R.id.cv_preview);
        infoWrapper = (LinearLayout) view.findViewById(R.id.infoWrapper);
        detailInformationWrapper = (CardView) view.findViewById(R.id.detail_information_wrapper);
        previewAvatarWrapper = (ConstraintLayout) view.findViewById(R.id.preview_avatar_wrapper);

        /*svInformation.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("Scroll Y", String.valueOf(scrollY));

                if (scrollY >= 400) {
                    // Tránh ẩn/hiện View trực tiếp
                    // Thay vào đó, sử dụng postOnAnimation để thực hiện sau khi cuộn kết thúc
                    ViewCompat.postOnAnimation(previewAvatarWrapper, new Runnable() {
                        @Override
                        public void run() {
                            previewAvatarWrapper.setVisibility(View.GONE);
                        }
                    });
                } else {
                    // Hiển thị View khi kéo về vị trí khác
                    ViewCompat.postOnAnimation(previewAvatarWrapper, new Runnable() {
                        @Override
                        public void run() {
                            previewAvatarWrapper.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });*/

        return view;
    }

    public void SlideUp() {
        this.DisabledRadius();
        this.VisibleInfo();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        view.setLayoutParams(params);
    }

    public void SlideDown() {
        this.EnableRadius();
        this.InvisibleInfo();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        view.setLayoutParams(params);
    }

    public void DisabledRadius() {
        if (cvPreview != null || infoWrapper != null) {
            cvPreview.setRadius(0);
        } else {
            Log.w("PreviewFragment", "CardView chưa được khởi tạo.");
        }
    }

    public void EnableRadius() {
        if (cvPreview != null || infoWrapper != null) {
            cvPreview.setRadius(24);
        } else {
            Log.w("PreviewFragment", "CardView chưa được khởi tạo.");
        }
    }

    public void VisibleInfo() {
        infoWrapper.setVisibility(View.GONE);
        detailInformationWrapper.setVisibility(View.VISIBLE);
    }

    public void InvisibleInfo() {
        infoWrapper.setVisibility(View.VISIBLE);
        detailInformationWrapper.setVisibility(View.GONE);
    }
}