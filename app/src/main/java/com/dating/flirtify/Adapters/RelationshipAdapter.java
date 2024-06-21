package com.dating.flirtify.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.Models.Responses.RelationshipResponse;
import com.dating.flirtify.R;

import java.util.ArrayList;

public class RelationshipAdapter extends RecyclerView.Adapter<RelationshipAdapter.ViewHolder> {
    private final ArrayList<RelationshipResponse> items;
    private final Context context;

    public RelationshipAdapter(Context context, ArrayList<RelationshipResponse> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relationship_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RelationshipResponse relationship = items.get(position);
        holder.tvRelationship.setText(relationship.getName_relationship());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRelationship;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRelationship = itemView.findViewById(R.id.tv_relationship);
        }
    }
}
