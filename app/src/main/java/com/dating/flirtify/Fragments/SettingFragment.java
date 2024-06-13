package com.dating.flirtify.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.dating.flirtify.Activities.LoginActivity;
import com.dating.flirtify.Activities.MainActivity;
import com.dating.flirtify.Activities.PreviewActivity;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Responses.LoginResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    Spinner spinner;
    Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(view);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return view;
    }

    private void initView(View view) {
        spinner = view.findViewById(R.id.spinner);
        String[] languages = new String[]{"Tiếng Việt (VN)", "English (EN)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void logout() {
        // Lấy instance của ApiService thông qua ApiClient
        ApiService apiService = ApiClient.getClient();

        // Gửi yêu cầu đăng nhập
        Call<Void> call = apiService.logout(SessionManager.getToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    SessionManager.clearSession();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else if (response.code() == 401) {
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error Logout", t.getMessage());
            }
        });

    }
}