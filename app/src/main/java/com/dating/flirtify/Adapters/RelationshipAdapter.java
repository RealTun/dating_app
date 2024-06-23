package com.dating.flirtify.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.Interfaces.OnFilterClickListener;
import com.dating.flirtify.Models.Responses.RelationshipResponse;
import com.dating.flirtify.R;

import java.util.ArrayList;

public class RelationshipAdapter extends RecyclerView.Adapter<RelationshipAdapter.ViewHolder> {
    private final ArrayList<RelationshipResponse> items;
    private final Context context;
    private final String textRelationship;
    private final OnFilterClickListener listener;

    public RelationshipAdapter(Context context, ArrayList<RelationshipResponse> items, String textRelationship, OnFilterClickListener listener) {
        this.context = context;
        this.items = items;
        this.textRelationship = textRelationship;
        this.listener = listener;
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
        holder.tvRelationshipText.setText(relationship.getName_relationship());
        holder.tvRelationshipID.setText(String.valueOf(relationship.getId()));

        if (relationship.getId() == 1) {
            holder.tvIcon.setText("\uD83D\uDC98");
        } else if (relationship.getId() == 2) {
            holder.tvIcon.setText("\uD83D\uDC4B");
        } else if (relationship.getId() == 3) {
            holder.tvIcon.setText("\uD83E\uDD42");
        } else if (relationship.getId() == 4) {
            holder.tvIcon.setText("\uD83E\uDD14");
        } else if (relationship.getId() == 5) {
            holder.tvIcon.setText("\uD83C\uDF89");
        } else if (relationship.getId() == 6) {
            holder.tvIcon.setText("\uD83D\uDE0D");
        }

        if (relationship.getName_relationship().equals(textRelationship)) {
            holder.cvInfo.setCardBackgroundColor(Color.WHITE);
            holder.cvOuter.setCardBackgroundColor(Color.RED);
        } else {
            holder.cvInfo.setCardBackgroundColor(ContextCompat.getColor(context, R.color.unchecked_color));
            holder.cvOuter.setCardBackgroundColor(Color.WHITE);
        }

        holder.cvInfo.setOnClickListener(v -> {
            listener.onItemClick(relationship);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRelationshipText, tvRelationshipID, tvIcon;
        CardView cvInfo, cvOuter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRelationshipText = itemView.findViewById(R.id.tv_relationship_text);
            tvRelationshipID = itemView.findViewById(R.id.tv_relationship_id);
            tvIcon = itemView.findViewById(R.id.tv_icon);
            cvInfo = itemView.findViewById(R.id.cv_inner);
            cvOuter = itemView.findViewById(R.id.cv_outer);
        }
    }
}
