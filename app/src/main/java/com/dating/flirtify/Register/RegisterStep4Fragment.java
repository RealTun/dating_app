package com.dating.flirtify.Register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dating.flirtify.R;

import java.util.Calendar;

public class RegisterStep4Fragment extends Fragment {
    private Spinner genderSpinner, spinnerDay, spinnerMonth, spinnerYear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_step4, container, false);

        genderSpinner = view.findViewById(R.id.gender_spinner);
        spinnerDay = view.findViewById(R.id.spinner_day);
        spinnerMonth = view.findViewById(R.id.spinner_month);
        spinnerYear = view.findViewById(R.id.spinner_year);

        setupGenderSpinner();
        setupDaySpinner();
        setupMonthSpinner();
        setupYearSpinner();

        addListeners();

        return view;
    }

    private void setupGenderSpinner() {
        String[] genders = getResources().getStringArray(R.array.genders);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.select_item, genders);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private void setupDaySpinner() {
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        updateDaysSpinner(31); // Khởi tạo với 31 ngày
        spinnerDay.setSelection(currentDay - 1);
    }

    private void setupMonthSpinner() {
        String[] months = getResources().getStringArray(R.array.months);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(), R.layout.select_item, months);
        monthAdapter.setDropDownViewResource(R.layout.dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        spinnerMonth.setSelection(currentMonth);
    }

    private void setupYearSpinner() {
        Integer[] years = new Integer[100];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 100; i++) {
            years[i] = currentYear - i;
        }
        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(requireContext(), R.layout.select_item, years);
        yearAdapter.setDropDownViewResource(R.layout.dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        spinnerYear.setSelection(0); // Chọn năm hiện tại

    }

    private void addListeners() {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDaysBasedOnMonthAndYear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        };

        spinnerMonth.setOnItemSelectedListener(listener);
        spinnerYear.setOnItemSelectedListener(listener);
    }

    private void updateDaysBasedOnMonthAndYear() {
        int selectedMonth = spinnerMonth.getSelectedItemPosition();
        int selectedYear = (int) spinnerYear.getSelectedItem();

        int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);
        int currentDay = spinnerDay.getSelectedItemPosition() + 1;
        updateDaysSpinner(daysInMonth);

        if (currentDay <= daysInMonth) {
            spinnerDay.setSelection(currentDay - 1);
        } else {
            spinnerDay.setSelection(daysInMonth - 1);
        }
    }

    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 0: // January
            case 2: // March
            case 4: // May
            case 6: // July
            case 7: // August
            case 9: // October
            case 11: // December
                return 31;
            case 3: // April
            case 5: // June
            case 8: // September
            case 10: // November
                return 30;
            case 1: // February
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    return 29; // Leap year
                } else {
                    return 28;
                }
            default:
                return 31;
        }
    }

    private void updateDaysSpinner(int days) {
        Integer[] daysArray = new Integer[days];
        for (int i = 0; i < days; i++) {
            daysArray[i] = i + 1;
        }
        ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<>(requireContext(), R.layout.select_item, daysArray);
        dayAdapter.setDropDownViewResource(R.layout.dropdown_item);
        spinnerDay.setAdapter(dayAdapter);
    }
}
