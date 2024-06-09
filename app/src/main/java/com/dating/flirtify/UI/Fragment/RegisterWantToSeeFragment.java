package com.dating.flirtify.UI.Fragment;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dating.flirtify.R;

public class RegisterWantToSeeFragment extends Fragment {
    private TextView tvMale, tvFemale, tvPeople;
    private Boolean isMale, isFemale, isPeople;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_want_to_see, container, false);

        // Khởi tạo các thành phần giao diện
        tvMale = view.findViewById(R.id.tvMale);
        tvFemale = view.findViewById(R.id.tvFemale);
        tvPeople = view.findViewById(R.id.tvPeople);

        // Khởi tạo các biến trạng thái
        isMale = isFemale = isPeople = false;

        // Thiết lập sự kiện lắng nghe
        setupListeners();

        return view;
    }

    private void setupListeners() {
        tvMale.setOnClickListener(v -> updateSelection(true, false, false));
        tvFemale.setOnClickListener(v -> updateSelection(false, true, false));
        tvPeople.setOnClickListener(v -> updateSelection(false, false, true));
    }

    private void updateSelection(boolean maleSelected, boolean femaleSelected, boolean peopleSelected) {
        isMale = maleSelected;
        isFemale = femaleSelected;
        isPeople = peopleSelected;

        tvMale.setBackground(ResourcesCompat.getDrawable(getResources(), maleSelected ? R.drawable.config_checked_button : R.drawable.config_unchecked_button, null));
        tvFemale.setBackground(ResourcesCompat.getDrawable(getResources(), femaleSelected ? R.drawable.config_checked_button : R.drawable.config_unchecked_button, null));
        tvPeople.setBackground(ResourcesCompat.getDrawable(getResources(), peopleSelected ? R.drawable.config_checked_button : R.drawable.config_unchecked_button, null));
    }
}
