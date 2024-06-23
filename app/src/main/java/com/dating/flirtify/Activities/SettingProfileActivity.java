package com.dating.flirtify.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dating.flirtify.Adapters.RelationshipAdapter;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Interfaces.OnFilterClickListener;
import com.dating.flirtify.Models.Requests.RelationshipRequest;
import com.dating.flirtify.Models.Responses.RelationshipResponse;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingProfileActivity extends AppCompatActivity implements OnFilterClickListener {
    private ApiService apiService;
    private String accessToken;
    private List<String> listInterest;
    private TextView tvInterests, tvRelationship, tvGender, tvDone;
    private EditText edtFullname, edtBio;
    private RecyclerView rvRelationship;
    private BottomSheetDialog bottomRelationship;
    private View bottomRelationshipView;
    private RelationshipAdapter relationshipAdapter;
    private String textRelationship;
    private final ArrayList<RelationshipResponse> relationshipItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        handlerEvent();
        fetchRelationships();
    }

    @Override
    public void onItemClick(RelationshipResponse relationship) {
        accessToken = SessionManager.getToken();
        RelationshipRequest relationshipRequest = new RelationshipRequest(relationship.getId());
        Call<Void> call = apiService.updateRelationshipType(accessToken, relationshipRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        bottomRelationship.dismiss();
                        Toast.makeText(SettingProfileActivity.this, "Cập nhật mục đích thành công", Toast.LENGTH_SHORT).show();
                        getCurrentUser();
                    }
                } else {
                    Log.e("API Error", "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Error", "Request failed: " + t.getMessage());
            }
        });
    }

    private void initView() {
        edtFullname = findViewById(R.id.edt_fullname);
        edtBio = findViewById(R.id.edt_bio);
        tvInterests = findViewById(R.id.tv_interests);
        tvRelationship = findViewById(R.id.tv_relationships);
        tvGender = findViewById(R.id.tv_gender);
        tvDone = findViewById(R.id.tv_done);

        apiService = ApiClient.getClient();
        accessToken = SessionManager.getToken();
        getCurrentUser();

        bottomRelationship = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomRelationshipView = getLayoutInflater().inflate(R.layout.bottom_relationship, null);
    }

    private void fetchRelationships() {
        Call<ArrayList<RelationshipResponse>> call = apiService.getRelationships();
        call.enqueue(new Callback<ArrayList<RelationshipResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RelationshipResponse>> call, Response<ArrayList<RelationshipResponse>> response) {
                if (response.isSuccessful()) {
                    ArrayList<RelationshipResponse> arrRelationship = response.body();
                    if (arrRelationship != null) {
                        relationshipItems.addAll(arrRelationship);
                    }
                } else {
                    Log.e("API Error:", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RelationshipResponse>> call, Throwable t) {
                Log.e("API Error:", "Request failed: " + t.getMessage());
            }
        });
    }

    private void getCurrentUser() {
        Call<UserResponse> call = apiService.getUser(accessToken);
        call.enqueue(new retrofit2.Callback<UserResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    textRelationship = userResponse.getRelationship();

                    listInterest = userResponse.getInterests();
                    String listInterestString = listInterest.toString();
                    listInterestString = listInterestString.replace("[", "").replace("]", "");

                    tvInterests.setText(listInterestString);
                    if (textRelationship.equals("Người yêu")) {
                        tvRelationship.setText(textRelationship + " \uD83D\uDC98");
                    } else if (textRelationship.equals("Những người bạn mới")) {
                        tvRelationship.setText(textRelationship + " \uD83D\uDC4B");
                    } else if (textRelationship.equals("Bất kì điều gì có thể")) {
                        tvRelationship.setText(textRelationship + " \uD83E\uDD42");
                    } else if (textRelationship.equals("Mình cũng chưa rõ lắm")) {
                        tvRelationship.setText(textRelationship + " \uD83E\uDD14");
                    } else if (textRelationship.equals("Quan hệ không ràng buộc")) {
                        tvRelationship.setText(textRelationship + " \uD83C\uDF89");
                    } else if (textRelationship.equals("Bạn hẹn hò lâu dài")) {
                        tvRelationship.setText(textRelationship + " \uD83D\uDE0D");
                    }
                    edtFullname.setText(userResponse.getFullname());
                    edtBio.setText(userResponse.getBio());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void handlerEvent() {
        tvInterests.setOnClickListener(v -> {
            Intent intent = new Intent(SettingProfileActivity.this, ListInterestActivity.class);
            startActivityForResult(intent, 9);
        });
        tvRelationship.setOnClickListener(v -> {
            bottomRelationship.setContentView(bottomRelationshipView);
            initRelationshipRecyclerView();
            bottomRelationship.show();
        });
        tvDone.setOnClickListener(v -> finish());
        tvGender.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn giới tính");

            // Các lựa chọn giới tính
            String[] genders = {"Nam", "Nữ", "Khác"};
            builder.setItems(genders, (dialog, which) -> {
                switch (which) {
                    case 0:
                        tvGender.setText("Nam");
                        break;
                    case 1:
                        tvGender.setText("Nữ");
                        break;
                    case 2:
                        tvGender.setText("Khác");
                        break;
                }
            });

            builder.create().show();
        });
    }

    private void initRelationshipRecyclerView() {
        rvRelationship = bottomRelationshipView.findViewById(R.id.rv_relationships);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);
        rvRelationship.setLayoutManager(layoutManager);
        relationshipAdapter = new RelationshipAdapter(this, relationshipItems, textRelationship, this);
        rvRelationship.setAdapter(relationshipAdapter);
    }
}