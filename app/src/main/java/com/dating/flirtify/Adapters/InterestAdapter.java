package com.dating.flirtify.Adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.R;

import java.util.List;
import java.util.Random;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {
    private final List<String> mData;

    public InterestAdapter(List<String> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(mData.get(position));

        // Tính toán dòng hiện tại
        int itemsPerRow = 3; // Giả sử mỗi dòng có 3 phần tử
        int rowIndex = position / itemsPerRow;
        int left = holder.textView.getPaddingLeft();
        int top = holder.textView.getPaddingTop();
        int right = holder.textView.getPaddingRight();
        int bottom = holder.textView.getPaddingBottom();

        holder.textView.setBackgroundResource(R.drawable.circle_button_gradient);
        holder.textView.setPadding(left, top, right, bottom);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_interest);
        }
    }
}

