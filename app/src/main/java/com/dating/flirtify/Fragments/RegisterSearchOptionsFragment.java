package com.dating.flirtify.Fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.dating.flirtify.R;

public class RegisterSearchOptionsFragment extends Fragment {

    private CardView cvLover, cvLongTerm, cvAnything, cvNoStringsAttached, cvNewFriends, cvNotSure;
    private boolean isLover, isLongTerm, isAnything, isNoStringsAttached, isNewFriends, isNotSure;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_search_options, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        // Khởi tạo CardView
        cvLover = view.findViewById(R.id.cvLover);
        cvLongTerm = view.findViewById(R.id.cvLongTerm);
        cvAnything = view.findViewById(R.id.cvAnything);
        cvNoStringsAttached = view.findViewById(R.id.cvNoStringsAttached);
        cvNewFriends = view.findViewById(R.id.cvNewFriends);
        cvNotSure = view.findViewById(R.id.cvNotSure);

        // Khởi tạo trạng thái ban đầu
        resetStates();

        // Thiết lập các listener
        setupListeners();

        return view;
    }

    private void setupListeners() {
        GradientDrawable backgroundChecked = new GradientDrawable();
        backgroundChecked.setColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        backgroundChecked.setCornerRadius(20);
        GradientDrawable backgroundUnChecked = new GradientDrawable();
        backgroundUnChecked.setColor(ResourcesCompat.getColor(getResources(), R.color.unchecked_color, null));
        backgroundUnChecked.setCornerRadius(20);

        CardView[] cardViews = {cvLover, cvLongTerm, cvAnything, cvNoStringsAttached, cvNewFriends, cvNotSure};

        for (CardView cardView : cardViews) {
            cardView.setOnClickListener(v -> selectOption(cardView, backgroundChecked, backgroundUnChecked));
        }
    }

    private void selectOption(CardView cardView, GradientDrawable backgroundChecked, GradientDrawable backgroundUnChecked) {
        // Đặt lại trạng thái ban đầu của các CardView
        resetStates();

        // Cập nhật trạng thái cho lựa chọn mới
        if (cardView == cvLover) {
            isLover = true;
        } else if (cardView == cvLongTerm) {
            isLongTerm = true;
        } else if (cardView == cvAnything) {
            isAnything = true;
        } else if (cardView == cvNoStringsAttached) {
            isNoStringsAttached = true;
        } else if (cardView == cvNewFriends) {
            isNewFriends = true;
        } else if (cardView == cvNotSure) {
            isNotSure = true;
        }

        // Cập nhật màu nền của CardView
        cardView.setBackground(backgroundChecked);

        // Reset màu nền của các CardView khác
        for (CardView cv : new CardView[]{cvLover, cvLongTerm, cvAnything, cvNoStringsAttached, cvNewFriends, cvNotSure}) {
            if (cv != cardView) {
                cv.setBackground(backgroundUnChecked);
            }
        }
    }

    private void resetStates() {
        isLover = isLongTerm = isAnything = isNoStringsAttached = isNewFriends = isNotSure = false;
    }

    public int getRelationshipType() {
        if (isLover) {
            return 1;
        } else if (isNewFriends) {
            return 2;
        } else if (isLongTerm) {
            return 6;
        } else if (isAnything) {
            return 3;
        } else if (isNoStringsAttached) {
            return 5;
        } else if (isNotSure) {
            return 4;
        } else {
            return 0;
        }
    }
}
