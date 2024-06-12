package com.dating.flirtify.Adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.dating.flirtify.Interfaces.OnCardActionListener;
import com.dating.flirtify.Interfaces.OnCurrentViewHolderChangeListener;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.R;

import java.util.ArrayList;
import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    private final ArrayList<UserResponse> items;
    private final OnCardActionListener onCardActionListener;
    private OnCurrentViewHolderChangeListener currentViewHolderChangeListener;

    public CardStackAdapter(ArrayList<UserResponse> items, OnCardActionListener listener) {
        this.items = items;
        this.onCardActionListener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserResponse item = items.get(position);

        List<String> listImages = item.getPhotos();
        List<String> imageUrls = new ArrayList<>(listImages);

        ImagePagerAdapter adapter = new ImagePagerAdapter(holder.itemView.getContext(), imageUrls);
        holder.tvName.setText(item.getFullname());
        holder.tvAge.setText(String.valueOf(item.getAge()));
        holder.ibArrowUp.setOnClickListener(v -> {
            if (onCardActionListener != null) {
                holder.ibArrowUp.setVisibility(View.GONE);
                holder.infoWrapper.setVisibility(View.GONE);
                onCardActionListener.onArrowUpClick(item);
            }
        });
        holder.viewPager.setAdapter(adapter);
        holder.viewPager.setUserInputEnabled(false);
        holder.viewPager.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() < (float) v.getWidth() / 2) {
                    holder.viewPager.setCurrentItem(holder.viewPager.getCurrentItem() - 1);
                } else {
                    holder.viewPager.setCurrentItem(holder.viewPager.getCurrentItem() + 1);
                }
            }
            return true;
        });

        holder.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(holder, position);
            }
        });

        holder.viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateIndicators(holder, holder.viewPager.getCurrentItem());
            }
        });

        if (currentViewHolderChangeListener != null) {
            currentViewHolderChangeListener.onCurrentViewHolderChanged(holder);
        }
    }

    public void setCurrentViewHolderChangeListener(OnCurrentViewHolderChangeListener listener) {
        this.currentViewHolderChangeListener = listener;
    }

    private void updateIndicators(ViewHolder holder, int position) {
        holder.indicatorLayout.removeAllViews();

        int count = holder.viewPager.getAdapter().getItemCount();
        if (count == 0) {
            return;
        }

        int viewPagerWidth = holder.viewPager.getWidth();
        int indicatorWidth = count >= 5 ? (viewPagerWidth / count) - (count * 4) : (viewPagerWidth / count) - (count * 8);
        int indicatorHeight = 20;

        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(holder.itemView.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(indicatorWidth, indicatorHeight);
            layoutParams.setMargins(8, 0, 8, 0);
            imageView.setLayoutParams(layoutParams);

            if (i == position) {
                imageView.setImageResource(R.drawable.indicator_active);
            } else {
                imageView.setImageResource(R.drawable.indicator_inactive);
            }

            holder.indicatorLayout.addView(imageView);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge;
        public ImageButton ibArrowUp;
        ViewPager2 viewPager;
        public LinearLayout infoWrapper;
        LinearLayout indicatorLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAge = itemView.findViewById(R.id.tv_age);
            ibArrowUp = itemView.findViewById(R.id.ib_arrow_up);
            infoWrapper = itemView.findViewById(R.id.ll_info_wrapper);
            viewPager = itemView.findViewById(R.id.vp_card);
            indicatorLayout = itemView.findViewById(R.id.ll_indicator);
        }
    }
}
