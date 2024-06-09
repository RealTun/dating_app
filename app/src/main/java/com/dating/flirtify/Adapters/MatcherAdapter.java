package com.dating.flirtify.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.Models.Matcher;
import com.dating.flirtify.R;

import java.util.ArrayList;

public class MatcherAdapter extends RecyclerView.Adapter<MatcherAdapter.ViewHolder> {
    Activity context;
    int idLayout;
    ArrayList<Matcher> arrMatcher;

    public MatcherAdapter(Activity _context, int _idLayout, ArrayList<Matcher> _arrMatcher) {
        this.context = _context;
        this.idLayout = _idLayout;
        this.arrMatcher = _arrMatcher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matcher_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Matcher matcher = arrMatcher.get(position);
        holder.img_matcher.setImageURI(Uri.parse(matcher.getImage()));
        holder.tv_name.setText(matcher.getName());
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
}
