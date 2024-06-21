package com.dating.flirtify.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.Activities.ChatActivity;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;

import com.dating.flirtify.Adapters.MatcherAdapter;
import com.dating.flirtify.Adapters.MatcherMessageAdapter;
import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatcherFragment extends Fragment {
    private ApiService apiService;
    private RecyclerView listViewMatcher;
    private ListView listViewMatcherMesage;
    private MatcherAdapter matcherAdapter;
    private MatcherMessageAdapter messageAdapter;
    private ArrayList<MatcherResponse> matchers = new ArrayList<>();
    private final ArrayList<MatcherResponse> matchersNew = new ArrayList<>();
    private final ArrayList<MatcherResponse> matchersOld = new ArrayList<>();
    private ProgressBar processBar1, processBar2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        listViewMatcher = view.findViewById(R.id.recyclerView);
        listViewMatcherMesage = view.findViewById(R.id.listView);
        processBar1 = view.findViewById(R.id.processBar1);
        processBar2 = view.findViewById(R.id.processBar2);

        getMatchers();

        listViewMatcherMesage.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("matcher", matchersOld.get(position));
            chatActivityLauncher.launch(intent);
        });

        return view;
    }

    private void getMatchers() {
        matchers.clear();
        matchersNew.clear();
        matchersOld.clear();

        apiService = ApiClient.getClient();
        Call<ArrayList<MatcherResponse>> call = apiService.getMatchers(SessionManager.getToken());
        call.enqueue(new Callback<ArrayList<MatcherResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MatcherResponse>> call, Response<ArrayList<MatcherResponse>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MatcherResponse> arrMatcher = response.body();
                    if (arrMatcher != null) {
                        matchers.addAll(arrMatcher);
                        updateUIWithMatchers(matchers);
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MatcherResponse>> call, Throwable t) {
                Log.e("Matches", "Request failed: " + t.getMessage());
            }
        });
    }

    private void updateUIWithMatchers(ArrayList<MatcherResponse> matchers) {
        matchers.forEach(matcherResponse -> {
            if (matcherResponse.getLast_message() != null) {
                matchersOld.add(matcherResponse);
                Log.d("matchersOld", matcherResponse.getFullname());
            } else {
                matchersNew.add(matcherResponse);
                Log.d("matchersNew", matcherResponse.getFullname());
            }
        });

        matcherAdapter = new MatcherAdapter(getActivity(), R.layout.matcher_items, matchersNew, matcher -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("matcher", matcher);
            startActivity(intent);
        });
        messageAdapter = new MatcherMessageAdapter(getActivity(), R.layout.message_items, matchersOld);

        listViewMatcher.setAdapter(matcherAdapter);
        listViewMatcherMesage.setAdapter(messageAdapter);
        if (matchers.size() >= 7) {
            listViewMatcherMesage.setTranscriptMode(0);
        }
        processBar1.setVisibility(View.GONE);
        processBar2.setVisibility(View.GONE);
        this.matchers = matchers;
    }

    private final ActivityResultLauncher<Intent> chatActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            getMatchers();
        }
    });
}
