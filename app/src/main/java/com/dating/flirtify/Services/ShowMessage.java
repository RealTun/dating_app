package com.dating.flirtify.Services;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.flirtify.R;

public class ShowMessage {


    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showCustomDialog(Context context, String title, String message) {
        if (context != null) {
            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.messagebox, null);

            // Create the AlertDialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);

            // Customize the dialog elements
            TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
            TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
            Button dialogButton = dialogView.findViewById(R.id.dialog_button);

            dialogTitle.setText(title);
            dialogMessage.setText(message);
            dialogButton.setText("Đồng ý");

            // Set up the button click listener
            AlertDialog alertDialog = builder.create();
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            // Show the dialog
            alertDialog.show();
        }
    }
}
