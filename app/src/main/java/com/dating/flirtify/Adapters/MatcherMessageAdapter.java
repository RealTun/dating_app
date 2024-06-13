package com.dating.flirtify.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.R;

import java.util.ArrayList;

public class MatcherMessageAdapter extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final ArrayList<MatcherResponse> matchers;

    public MatcherMessageAdapter(Context context, int layout, ArrayList<MatcherResponse> matchers) {
        this.context = context;
        this.layout = layout;
        this.matchers = matchers;
    }

    public static class ViewHolder {
        public ImageView ivMatcher;
        public TextView tvName, tvMessage;
    }

    @Override
    public int getCount() {
        return matchers.size();
    }

    @Override
    public Object getItem(int position) {
        return matchers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();
            holder.ivMatcher = convertView.findViewById(R.id.imageView3);
            holder.tvName = convertView.findViewById(R.id.tv_name);
            holder.tvMessage = convertView.findViewById(R.id.tv_message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MatcherResponse matcher = matchers.get(position);
        Glide.with(context)
                .load(matcher.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivMatcher);
        holder.tvName.setText(matcher.getFullname());
        holder.tvMessage.setText(matcher.getLast_message());
        return convertView;
    }
}
