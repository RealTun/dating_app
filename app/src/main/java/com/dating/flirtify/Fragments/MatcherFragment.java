package com.dating.flirtify.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.dating.flirtify.Activities.ChatActivity;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;

import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.Adapters.MatcherAdapter;
import com.dating.flirtify.Adapters.MatcherMessageAdapter;
import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatcherFragment extends Fragment {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private ListView listView;
    private MatcherAdapter matcherAdapter;
    private MatcherMessageAdapter messageAdapter;
    private ArrayList<MatcherResponse> matchers = new ArrayList<>();
    private ProgressBar processBar1, processBar2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        listView = view.findViewById(R.id.listView);
        processBar1 = view.findViewById(R.id.processBar1);
        processBar2 = view.findViewById(R.id.processBar2);

        getMatchers();

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void getMatchers(){
        String accessToken = "Bearer " + "2|xB7YKMGNrtVe8JqhpPQYc28ymFNav2kqWglXW61d55e976a5";
        apiService = ApiClient.getClient();
        Call<ArrayList<MatcherResponse>> call = apiService.getMatchers(accessToken);
        call.enqueue(new Callback<ArrayList<MatcherResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MatcherResponse>> call, Response<ArrayList<MatcherResponse>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MatcherResponse> arrMatcher = response.body();
                    if(arrMatcher != null){
                        matchers.addAll(arrMatcher);
                        updateUIWithMatchers(matchers);
                    }
                    else {
                        Log.d("dcmm", "vl2");
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MatcherResponse>> call, Throwable t) {
                Log.e("Matches", "Request failed: " + t.getMessage());
            }
        });
    }

    private void updateUIWithMatchers(ArrayList<MatcherResponse> matchers) {
        matcherAdapter = new MatcherAdapter(getActivity(), R.layout.matcher_items, matchers);
        messageAdapter = new MatcherMessageAdapter(getActivity(), R.layout.message_items, matchers);
        recyclerView.setAdapter(matcherAdapter);
        listView.setAdapter(messageAdapter);
        if(matchers.size() >= 7){
            listView.setTranscriptMode(0);
        }
        processBar1.setVisibility(View.GONE);
        processBar2.setVisibility(View.GONE);
    }
}
