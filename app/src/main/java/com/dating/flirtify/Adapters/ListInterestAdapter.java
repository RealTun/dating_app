package com.dating.flirtify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dating.flirtify.Models.InterestType;
import com.dating.flirtify.Models.Responses.InterestResponse;
import com.dating.flirtify.R;

import java.util.ArrayList;
import java.util.List;

public class ListInterestAdapter extends RecyclerView.Adapter<ListInterestAdapter.ViewHolder> {
    private List<InterestType> items;
    private final Context context;
    private InterestResponse interestResponse;
    private final List<Integer> selectedInterestIds = new ArrayList<>();

    public ListInterestAdapter(Context context, List<InterestType> items, InterestResponse interestResponse) {
        this.context = context;
        this.items = items;
        this.interestResponse = interestResponse;
    }

    public void updateList(List<InterestType> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public void setInterestResponse(InterestResponse interestResponse) {
        this.interestResponse = interestResponse;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListInterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_interests_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListInterestAdapter.ViewHolder holder, int position) {
        InterestType item = items.get(position);
        holder.tvInterest.setText(item.getName_interest_type());
        holder.tvInterestID.setText(String.valueOf(item.getId()));

        if (interestResponse != null && interestResponse.getName_interest() != null) {
            List<String> userInterests = interestResponse.getName_interest();

            if (userInterests.contains(item.getName_interest_type())) {
                holder.tvInterest.setTextColor(context.getResources().getColor(R.color.gradient_center));
            } else {
                holder.tvInterest.setTextColor(context.getResources().getColor(R.color.black));
            }

            if (selectedInterestIds.contains(item.getId()) && !userInterests.contains(item.getName_interest_type())) {
                selectedInterestIds.remove(Integer.valueOf(item.getId()));
            } else if (!selectedInterestIds.contains(item.getId()) && userInterests.contains(item.getName_interest_type())) {
                selectedInterestIds.add(item.getId());
            }
        }

        holder.tvInterest.setOnClickListener(v -> {
            if (interestResponse != null && interestResponse.getName_interest() != null) {
                List<String> userInterests = interestResponse.getName_interest();

                if (userInterests.contains(item.getName_interest_type())) {
                    userInterests.remove(item.getName_interest_type());
                    selectedInterestIds.remove(Integer.valueOf(item.getId()));
                } else {
                    if (selectedInterestIds.size() >= 5) {
                        Toast.makeText(context, "Tối đa là 5 sở thích", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    userInterests.add(item.getName_interest_type());
                    selectedInterestIds.add(item.getId());
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Integer> getSelectedInterestIds() {
        return selectedInterestIds;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInterest, tvInterestID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInterest = itemView.findViewById(R.id.tv_interest);
            tvInterestID = itemView.findViewById(R.id.tv_interest_id);
        }
    }
}

