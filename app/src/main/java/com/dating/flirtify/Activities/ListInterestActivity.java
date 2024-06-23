package com.dating.flirtify.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.Adapters.ListInterestAdapter;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.InterestType;
import com.dating.flirtify.Models.Requests.InterestRequest;
import com.dating.flirtify.Models.Responses.InterestResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ListInterestActivity extends AppCompatActivity {
    private ApiService apiService;
    private RecyclerView rvInteresets;
    private ListInterestAdapter interestAdapter;
    private List<InterestType> listInterest = new ArrayList<>();
    private InterestResponse interestResponse;
    private TextView tvCancel, tvDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_interest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        getAllInterests();
        getInterestByUser();
        eventHandler();
    }

    private void initView() {
        tvCancel = findViewById(R.id.tv_cancel);
        tvDone = findViewById(R.id.tv_done);

        rvInteresets = findViewById(R.id.rv_interests);
        apiService = ApiClient.getClient();

        interestAdapter = new ListInterestAdapter(this, listInterest, interestResponse);
        rvInteresets.setAdapter(interestAdapter);
    }

    private void getAllInterests() {
        String accessToken = SessionManager.getToken();
        Call<List<InterestType>> call = apiService.getInterestTypes(accessToken);
        call.enqueue(new retrofit2.Callback<List<InterestType>>() {
            @Override
            public void onResponse(Call<List<InterestType>> call, retrofit2.Response<List<InterestType>> response) {
                if (response.isSuccessful()) {
                    listInterest = response.body();
                    updateUI();
                } else {
                    Log.e("getInterestTypes", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<InterestType>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getInterestByUser() {
        String accessToken = SessionManager.getToken();
        Call<InterestResponse> call = apiService.getInterestByUser(accessToken);
        call.enqueue(new retrofit2.Callback<InterestResponse>() {
            @Override
            public void onResponse(Call<InterestResponse> call, retrofit2.Response<InterestResponse> response) {
                if (response.isSuccessful()) {
                    interestResponse = response.body();
                    updateUI();
                } else {
                    Log.e("getInterestByUser", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<InterestResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateUI() {
        if (rvInteresets != null && interestResponse != null) {
            interestAdapter.updateList(listInterest);
            interestAdapter.setInterestResponse(interestResponse);
        } else {
            Log.e("updateUI", "rvInteresets or interestResponse is null");
        }
    }


    private void eventHandler() {
        tvCancel.setOnClickListener(v -> finish());
        tvDone.setOnClickListener(v -> {
            InterestRequest interestRequest = new InterestRequest(interestAdapter.getSelectedInterestIds());
            String accessToken = SessionManager.getToken();
            Call<Void> call = apiService.storeInterestUser(accessToken, interestRequest);
            call.enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ListInterestActivity.this, "Cập nhật sở thích thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }
}
