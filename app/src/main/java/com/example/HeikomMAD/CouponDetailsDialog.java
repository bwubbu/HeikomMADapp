package com.example.HeikomMAD;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class CouponDetailsDialog extends Dialog {
    private int position;

    public CouponDetailsDialog(@NonNull Context context, int position) {
        super(context);
        this.position = position;
        init();
    }

    private void init() {
        // Set the custom dialog layout
        setContentView(R.layout.activity_couponcode_layout);

        // Retrieve the code text based on the position and display it
        String codeText = getCodeTextForPosition(position);
        TextView textView = findViewById(R.id.centerTextView);
        textView.setText(codeText);

        // Configure dialog properties (e.g., width, height, animations)

        // Initialize views and set coupon details content
        CardView cardView = findViewById(R.id.cardView);
        // Set coupon details content using TextViews or other views

        // Find the "x" icon and add a click listener to close the dialog
        ImageView closeButton = findViewById(R.id.backButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog when the "x" icon is clicked
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add any additional initialization code here if needed
    }

    private String getCodeTextForPosition(int position) {
        // Ensure that the position is within the bounds of the codeTexts array
        if (position >= 0 && position < codeTexts.length) {
            return codeTexts[position];
        } else {
            // Handle the case where the position is out of bounds
            return "Invalid Code Text"; // Or return a default text or handle it as needed
        }
    }

    // Define an array of code texts
    private String[] codeTexts = {
            "8888-1111-2222-3333",
            "4444-1111-3333-1111",
            "5555-4444-3333-4444",
            // Add more code texts as needed
    };
}
