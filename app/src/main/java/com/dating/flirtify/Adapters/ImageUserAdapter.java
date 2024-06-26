package com.dating.flirtify.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Requests.PhotoRequest;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUserAdapter extends ArrayAdapter {
    Activity context;
    int idLayout;
    List<String> userPhotos;

    public ImageUserAdapter(Activity _context, int _idLayout, List<String> _userPhotos) {
        super(_context, _idLayout, _userPhotos);
        this.context = _context;
        this.idLayout = _idLayout;
        this.userPhotos = _userPhotos;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInlater = context.getLayoutInflater();

        convertView = myInlater.inflate(idLayout, null);

        String url = userPhotos.get(position);

        ImageView imgview = convertView.findViewById(R.id.imageUser);
        ImageView btn_delete = convertView.findViewById(R.id.btn_DeletePhoto);
        ProgressBar progressBarImage = convertView.findViewById(R.id.progressBarImage);
        ApiService apiService = ApiClient.getClient();
        String token = SessionManager.getToken();

        if(!Objects.equals(url, "")){
            Glide.with(context)
                    .load(url)
                    .into(imgview);
            imgview.setBackgroundResource(0);
            progressBarImage.setVisibility(View.GONE);

            btn_delete.setOnClickListener(v -> {
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(url, mediaType);

                Call<Void> call = apiService.deleteUserPhotos(token, body);

                progressBarImage.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            imgview.setBackgroundResource(0);
                            progressBarImage.setVisibility(View.GONE);
                            btn_delete.setVisibility(View.GONE);
                            userPhotos.set(position, "");
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            });
        }
        else{
            btn_delete.setVisibility(View.GONE);
            progressBarImage.setVisibility(View.GONE);
        }

        return convertView;
    }
}
