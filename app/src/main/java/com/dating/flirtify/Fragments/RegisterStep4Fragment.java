package com.dating.flirtify.Fragments;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dating.flirtify.R;

import java.util.Calendar;
import java.util.Date;

public class RegisterStep4Fragment extends Fragment {

    private Spinner genderSpinner, daySpinner, monthSpinner, yearSpinner;
    private EditText etName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_step4, container, false);

        initializeViews(view);
        setupSpinners();
        addSpinnerListeners();

        return view;
    }

    private void initializeViews(View view) {
        genderSpinner = view.findViewById(R.id.gender_spinner);
        daySpinner = view.findViewById(R.id.spinner_day);
        monthSpinner = view.findViewById(R.id.spinner_month);
        yearSpinner = view.findViewById(R.id.spinner_year);
        etName = view.findViewById(R.id.etName);
    }

    private void setupSpinners() {
        setupGenderSpinner();
        setupDaySpinner();
        setupMonthSpinner();
        setupYearSpinner();
    }

    private void setupGenderSpinner() {
        String[] genders = getResources().getStringArray(R.array.genders);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.select_item,
                genders
        );
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private void setupDaySpinner() {
        updateDaySpinner(31);
        daySpinner.setSelection(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1);
    }

    private void setupMonthSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.months,
                R.layout.select_item
        );
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setSelection(Calendar.getInstance().get(Calendar.MONTH));
    }

    private void setupYearSpinner() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer[] years = new Integer[100];
        for (int i = 0; i < 100; i++) {
            years[i] = currentYear - i;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.select_item,
                years
        );
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setSelection(0);
    }

    private void addSpinnerListeners() {
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
        monthSpinner.setOnItemSelectedListener(listener);
        yearSpinner.setOnItemSelectedListener(listener);
    }

    private void updateDaysBasedOnMonthAndYear() {
        int selectedMonth = monthSpinner.getSelectedItemPosition();
        int selectedYear = (int) yearSpinner.getSelectedItem();
        int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);
        int currentDay = daySpinner.getSelectedItemPosition() + 1;

        updateDaySpinner(daysInMonth);
        daySpinner.setSelection(Math.min(currentDay - 1, daysInMonth - 1));
    }

    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 1:
                return isLeapYear(year) ? 29 : 28;
            default:
                return 31;
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void updateDaySpinner(int days) {
        Integer[] daysArray = new Integer[days];
        for (int i = 0; i < days; i++) {
            daysArray[i] = i + 1;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.select_item,
                daysArray
        );
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        daySpinner.setAdapter(adapter);
    }

    public boolean validateFields() {
        if (etName.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập tên.");
            return false;
        }

        if (genderSpinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            showToast("Vui lòng chọn giới tính.");
            return false;
        }

        if (daySpinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION ||
                monthSpinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION ||
                yearSpinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            showToast("Vui lòng chọn ngày sinh hợp lệ.");
            return false;
        }

        int selectedDay = (int) daySpinner.getSelectedItem();
        int selectedMonth = monthSpinner.getSelectedItemPosition();
        int selectedYear = (int) yearSpinner.getSelectedItem();

        Calendar birthDate = Calendar.getInstance();
        birthDate.set(selectedYear, selectedMonth, selectedDay);

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        if (age < 18) {
            showToast("Bạn phải đủ 18 tuổi trở lên.");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    public String getName() {
        String name = etName.getText().toString().trim();
        return name;
    }

    public String getGender() {
        String gender = genderSpinner.getSelectedItem().toString();
        return gender;
    }

    public Date getDateOfBirth() {

        int day = (int) daySpinner.getSelectedItem();
        int month = monthSpinner.getSelectedItemPosition(); // Tháng bắt đầu từ 0
        int year = (int) yearSpinner.getSelectedItem();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
