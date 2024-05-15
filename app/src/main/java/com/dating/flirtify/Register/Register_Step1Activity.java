package com.dating.flirtify.Register;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dating.flirtify.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_Step1Activity extends AppCompatActivity {

    Button btnContinue;
    EditText etNumberPhone;

    private static final String PHONE_NUMBER_PATTERN = "^(?:\\+?\\d{1,3}[-.\\s]?)?(?:\\(\\d{3}\\)|\\d{3})[-.\\s]?\\d{3}[-.\\s]?\\d{4}$";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_step1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvAppName = findViewById(R.id.tvAppName);
        setColorGradient(tvAppName, getResources().getColor(R.color.gradient_top), getResources().getColor(R.color.gradient_center), getResources().getColor(R.color.gradient_bottom));

        btnContinue = findViewById(R.id.nextButton);
        etNumberPhone = findViewById(R.id.etNumberPhone);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidPhoneNumber(etNumberPhone.getText().toString())) {
                    Intent intent = new Intent(Register_Step1Activity.this, Register_Step2_Activity.class);
                    intent.putExtra("PhoneNumber", etNumberPhone.getText().toString());
                    Log.d("phonepass", etNumberPhone.getText().toString());
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(Register_Step1Activity.this)
                            .setTitle("Lỗi")
                            .setMessage("Số điện thoại không hợp lệ!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
//                    etNumberPhone.setError("Số điện thoại không hợp lệ!");
//                    Toast.makeText(Register_Step1Activity.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void setColorGradient(TextView tv, int... color) {
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