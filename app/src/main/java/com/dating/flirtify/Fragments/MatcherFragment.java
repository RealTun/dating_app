package com.dating.flirtify.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.R;

public class MatcherFragment extends Fragment {
    ListView listViewMatcher;
    ApiService apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        listViewMatcher = view.findViewById(R.id.listViewMatcher);
        apiService = ApiClient.getClient();

        return view;
    }

    private void getMatchers(){
        String accessToken = "Bearer " + "2|xB7YKMGNrtVe8JqhpPQYc28ymFNav2kqWglXW61d55e976a5";
    }
}
