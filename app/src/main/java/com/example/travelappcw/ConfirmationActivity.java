package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize the Back to Home button
        Button backToHomeButton = findViewById(R.id.backToHomeButton);

        // Set click listener for the button
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to AppMainPage
                Intent intent = new Intent(ConfirmationActivity.this, AppMainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Clear the back stack
                startActivity(intent);
                finish(); // Close the current activity
            }
        });
    }
}