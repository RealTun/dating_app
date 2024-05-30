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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preview, container, false);
        cvPreview = (CardView) view.findViewById(R.id.cv_preview);
        infoWrapper = (LinearLayout) view.findViewById(R.id.infoWrapper);
        detailInformationWrapper = (CardView) view.findViewById(R.id.detail_information_wrapper);

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