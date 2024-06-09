package com.dating.flirtify.Fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dating.flirtify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PreviewFragment extends Fragment {
    private View view;
    private CardView cvPreview, detailInformationWrapper; // Declare a member variable to store the CardView reference
    private LinearLayout infoWrapper, bottomCardWrapper;
    private ImageButton ibArrowUp;
    BottomNavigationView footerWrapper;
    ConstraintLayout mConstraintLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preview, container, false);
        initViews(view);
        handlerEvent();

        return view;
    }

    private void handlerEvent() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        HeaderFragment headerFragment = (HeaderFragment) fragmentManager.findFragmentById(R.id.fragment_header);

        ibArrowUp.setOnClickListener(v -> {
            ibArrowUp.setVisibility(View.GONE);
            footerWrapper.setVisibility(View.GONE);

            ObjectAnimator animator = ObjectAnimator.ofFloat(bottomCardWrapper, "translationY", 0, 125f);
            animator.setDuration(300);
            animator.start();
            headerFragment.setHeaderType(2);

            this.SlideUp();
            ConstraintSet mConstraintSet = new ConstraintSet();
            mConstraintSet.clone(mConstraintLayout);
            mConstraintSet.constrainPercentHeight(R.id.content_wrapper, 0.92f);
            mConstraintSet.applyTo(mConstraintLayout);
        });
    }

    private void initViews(View view) {
        cvPreview = view.findViewById(R.id.cv_preview);
        ibArrowUp = view.findViewById(R.id.ib_arrow_up);
        infoWrapper = view.findViewById(R.id.infoWrapper);
        detailInformationWrapper = view.findViewById(R.id.detail_information_wrapper);
        bottomCardWrapper = requireActivity().findViewById(R.id.bottom_card_wrapper);
        footerWrapper = requireActivity().findViewById(R.id.footer_wrapper);
        mConstraintLayout = requireActivity().findViewById(R.id.main);
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