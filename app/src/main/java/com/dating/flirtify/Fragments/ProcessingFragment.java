package com.dating.flirtify.Fragments;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dating.flirtify.R;

public class ProcessingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processing, container, false);

        // Inflate the layout for this fragment
        TextView tvAppName = view.findViewById(R.id.tvAppName);
        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom));

        getActivity().getWindow().findViewById(R.id.btnLogin).setVisibility(View.GONE);
        getActivity().getWindow().findViewById(R.id.tvAppName).setVisibility(View.GONE);


        ProgressBar progressBar = view.findViewById((R.id.progressBar));
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.gradient_bottom),
                android.graphics.PorterDuff.Mode.SRC_IN
        );

        return view;
    }


    private void setColorGradient(TextView tv, int... color){
        TextPaint textPaint = tv.getPaint();
        float width = textPaint.measureText(tv.getText().toString());
        float height = tv.getTextSize();

        Shader textShader = new LinearGradient(
                0, 0, 0, height,
                color, null, Shader.TileMode.CLAMP
        );

        tv.getPaint().setShader(textShader);
        tv.setTextColor(color[0]);
    }

}