package com.dating.flirtify.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.R;

import java.util.ArrayList;

public class MatcherAdapter extends RecyclerView.Adapter<MatcherAdapter.ViewHolder> {
    Activity context;
    int idLayout;
    ArrayList<MatcherResponse> arrMatcher;
    OnItemClickListener listener;

    public MatcherAdapter(Activity _context, int _idLayout, ArrayList<MatcherResponse> _arrMatcher) {
        this.context = _context;
        this.idLayout = _idLayout;
        this.arrMatcher = _arrMatcher;
    }

    public MatcherAdapter(Activity _context, int _idLayout, ArrayList<MatcherResponse> _arrMatcher, OnItemClickListener _listener) {
        this.context = _context;
        this.idLayout = _idLayout;
        this.arrMatcher = _arrMatcher;
        this.listener = _listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatcherResponse matcher = arrMatcher.get(position);
        Glide.with(context)
                .load(matcher.getImageUrl())
                .into(holder.img_matcher);
        holder.tv_name.setText(matcher.getFullname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(matcher);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrMatcher.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_matcher;
        TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_matcher = itemView.findViewById(R.id.imgMatcher);
            tv_name = itemView.findViewById(R.id.tv_nameMatcher);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MatcherResponse matcher);
    }
}
