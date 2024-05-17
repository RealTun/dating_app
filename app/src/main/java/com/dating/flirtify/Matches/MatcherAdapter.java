package com.dating.flirtify.Matches;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dating.flirtify.Models.Matcher;
import com.dating.flirtify.R;

import java.util.ArrayList;

public class MatcherAdapter extends ArrayAdapter {
    Activity context;
    int idLayout;
    ArrayList<Matcher> arrMatcher;

    public MatcherAdapter(Activity _context, int _idLayout, ArrayList<Matcher> _arrMatcher){
        super(_context, _idLayout, _arrMatcher);
        this.context = _context;
        this.idLayout = _idLayout;
        this.arrMatcher = _arrMatcher;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myFlater = context.getLayoutInflater();
        convertView = myFlater.inflate(idLayout, null);
        Matcher matcher = arrMatcher.get(position);
        ImageView img_matcher = convertView.findViewById(R.id.imgMatcher);
        img_matcher.setImageResource(matcher.getImage());

        TextView tv_name = convertView.findViewById(R.id.tv_nameMatcher);
        tv_name.setText(matcher.getName());

        return convertView;
    }
}
