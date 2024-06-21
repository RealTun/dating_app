package com.dating.flirtify.Activities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dating.flirtify.Adapters.MessageListAdapter;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Models.Requests.MessageRequest;
import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.Models.Responses.MessageResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.NotificationHelper;
import com.dating.flirtify.Services.SessionManager;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    EditText etMessage;
    TextView tv_name;
    ImageButton btn_back;
    ImageView img_avt;
    RecyclerView recyclerViewChat;
    ProgressBar progressBar;
    ApiService apiService;
    MessageListAdapter messageListAdapter;
    ArrayList<MessageResponse> messageResponses = new ArrayList<>();
    Intent intent;
    MatcherResponse matcher;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        matcher = (MatcherResponse) intent.getSerializableExtra("matcher");
        setData();

        getMessages(matcher.getMatcher_id());

        handleEvent();

        // pusher realtime event
        Pusher pusher = new Pusher("7797270065e276fe4342", new PusherOptions().setCluster("ap1"));

        Channel channel = pusher.subscribe("chat");

        channel.bind("chat-flirtify", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.d("Pusher", "Received event with data: " + event.getData());
                MessageResponse message = new Gson().fromJson(event.getData(),MessageResponse.class);
                Log.d("Pusher", "Data message: " + message.getMessage_content());
                if(matcher.getMatcher_id() == message.getSender_id()){
                    message.setSentByCurrentUser(false);
                    message.setImageReceiverUrl(matcher.getImageUrl());
                    messageResponses.add(message);
                    runOnUiThread(() -> {
                        updateUIWithMessage(messageResponses);
//                        showNotification(getApplicationContext(), matcher);
                        NotificationHelper.showNotification(getApplicationContext(), matcher);
                    });
                }
            }
        });

        pusher.disconnect();
        pusher.connect();
    }

    private void init(){
        etMessage = findViewById(R.id.etMessage);
        tv_name = findViewById(R.id.tv_nameReceiver);
        img_avt = findViewById(R.id.imgview_receiver);
        btn_back = findViewById(R.id.imgbtn_back);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        apiService = ApiClient.getClient();
        intent = getIntent();
    }

    private void setData(){
        tv_name.setText(matcher.getFullname());
        Glide.with(this)
                .load(matcher.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(img_avt);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handleEvent(){
        etMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2; // Right drawable
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (etMessage.getCompoundDrawables()[DRAWABLE_END] != null) {
                        int drawableWidth = etMessage.getCompoundDrawables()[DRAWABLE_END].getBounds().width();
                        if (event.getRawX() >= (etMessage.getRight() - drawableWidth - etMessage.getPaddingEnd())) {
                            if (etMessage.length() < 1) {
                                return false;
                            }
                            MessageRequest messageRequest = new MessageRequest(matcher.getMatch_id(), matcher.getMatcher_id(), etMessage.getText().toString());
                            sendMessage(messageRequest);
                            etMessage.setText("");
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        // back to matcher display
        btn_back.setOnClickListener(v -> {
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void sendMessage(MessageRequest messageRequest) {
        Call<MessageResponse> call = apiService.sendMessage(SessionManager.getToken(), messageRequest);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse sentMessage = response.body();
                    if (sentMessage != null) {
                        messageResponses.add(sentMessage);
                        runOnUiThread(() -> updateUIWithMessage(messageResponses));
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(ChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e("SendMessage", t.getMessage());
            }
        });
    }

    private void getMessages(int receiver_id) {
        Call<ArrayList<MessageResponse>> call = apiService.getMessages(SessionManager.getToken(), receiver_id);
        call.enqueue(new Callback<ArrayList<MessageResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MessageResponse>> call, Response<ArrayList<MessageResponse>> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        ArrayList<MessageResponse> messages = response.body();
                        if (messages != null) {
                            messageResponses.addAll(messages);
                            runOnUiThread(() -> updateUIWithMessage(messageResponses));
                        }
                    });
                } else if (response.code() == 401) {
                    Toast.makeText(ChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("ChatActivity", response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MessageResponse>> call, Throwable t) {
                Log.e("ChatActivity", t.getMessage());
            }
        });
    }

    private void updateUIWithMessage(ArrayList<MessageResponse> messages) {
//        messageListAdapter = new MessageListAdapter(this, messages);
//        recyclerViewChat.setAdapter(messageListAdapter);
//        progressBar.setVisibility(View.GONE);
//        messageResponses = messages;
//        recyclerViewChat.scrollToPosition(messages.size() - 1);

        if (messageListAdapter == null) {
            messageListAdapter = new MessageListAdapter(this, messages);
            recyclerViewChat.setAdapter(messageListAdapter);
        } else {
            messageListAdapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
        recyclerViewChat.scrollToPosition(messages.size() - 1);
    }
}