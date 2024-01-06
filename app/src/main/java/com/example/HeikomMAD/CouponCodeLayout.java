package com.example.HeikomMAD;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CouponCodeLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponcode_layout);

        // Retrieve the position from Intent extras
        int position = getIntent().getIntExtra("position", -1);

        // Determine what text (code) to display based on the position
        String codeText = getCodeTextForPosition(position);

        // Update the layout with the code text
        TextView textView = findViewById(R.id.centerTextView);
        textView.setText(codeText);
    }

    private String getCodeTextForPosition(int position) {
        // Replace this with your logic to determine the code text based on the position
        // You can use a switch statement, if-else, or any other logic to map positions to code text
        String codeText = "Default Code Text";

        switch (position) {
            case 0:
                codeText = "Code for position 0";
                break;
            case 1:
                codeText = "Code for position 1";
                break;
            // Add more cases as needed for other positions
        }

        return codeText;
    }
}

