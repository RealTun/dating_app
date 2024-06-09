package com.dating.flirtify.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.Adapters.MatcherAdapter;
import com.dating.flirtify.Adapters.MatcherMessageAdapter;
import com.dating.flirtify.Models.Matcher;
import com.dating.flirtify.R;

import java.util.ArrayList;

public class MatcherFragment extends Fragment {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private ListView listView;
    private MatcherAdapter matcherAdapter;
    private MatcherMessageAdapter messageAdapter;
    private ArrayList<Matcher> matchers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        apiService = ApiClient.getClient();
        recyclerView = view.findViewById(R.id.recyclerView);
        listView = view.findViewById(R.id.listView);

        matchers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            matchers.add(new Matcher("R.drawable.avatar_dung", "Name " + i));
        }

        matcherAdapter = new MatcherAdapter(getActivity(), R.layout.matcher_items, matchers);
        messageAdapter = new MatcherMessageAdapter(getActivity(), R.layout.message_items, matchers);
        recyclerView.setAdapter(matcherAdapter);
        listView.setAdapter(messageAdapter);

        return view;
    }

    private void getMatchers(){
        String accessToken = "Bearer " + "2|xB7YKMGNrtVe8JqhpPQYc28ymFNav2kqWglXW61d55e976a5";
    }
}
