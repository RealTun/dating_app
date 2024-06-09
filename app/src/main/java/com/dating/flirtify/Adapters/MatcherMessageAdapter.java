package com.dating.flirtify.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dating.flirtify.Models.Matcher;
import com.dating.flirtify.R;

import java.util.ArrayList;
import java.util.List;

public class MatcherMessageAdapter extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final ArrayList<Matcher> matchers;

    public MatcherMessageAdapter(Context context, int layout, ArrayList<Matcher> matchers) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Matcher matcher = matchers.get(position);
        holder.ivMatcher.setImageResource(matcher.getImage());
        holder.tvName.setText(matcher.getName());
        return convertView;
    }
}
