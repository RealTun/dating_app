package com.dating.flirtify;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Shader textShader = new LinearGradient(
//                0, 0, 0, 200,
//                new int[]{Color.parseColor("#E32F70"), Color.parseColor("#E64B77"), Color.parseColor("#EE8585")},
//                new float[]{0, 0.5f, 1},
//                Shader.TileMode.CLAMP
//        );
//
        TextView tvAppName = findViewById(R.id.tvAppName);
//        tvAppName.getPaint().setShader(textShader);

        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom));



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