package com.dating.flirtify.Register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dating.flirtify.R;

public class RegisterStep4Fragment extends Fragment {
    Spinner genderSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_step4, container, false);

        genderSpinner = view.findViewById(R.id.gender_spinner);

        String[] genders = {"Nam", "Nữ", "Khác"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.select_item, genders);

        adapter.setDropDownViewResource(R.layout.dropdown_item);

        genderSpinner.setAdapter(adapter);

        return view;
    }
}
