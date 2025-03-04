package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentFormActivity extends AppCompatActivity {

    private TextView totalCostTextView;
    private Button confirmPaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form);

        // Initialize views
        totalCostTextView = findViewById(R.id.totalCostTextView);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        // Retrieve total cost from the intent
        double totalCost = getIntent().getDoubleExtra("totalCost", 0.0);

        // Display the total cost
        totalCostTextView.setText("Total to Pay: $" + String.format("%.2f", totalCost));

        // Confirm Payment Button Click Listener
        confirmPaymentButton.setOnClickListener(v -> {
            // Handle payment confirmation
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();

            // Navigate to ConfirmationActivity
            Intent intent = new Intent(PaymentFormActivity.this, ConfirmationActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}