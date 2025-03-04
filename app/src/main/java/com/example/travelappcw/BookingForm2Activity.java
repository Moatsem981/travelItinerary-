package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class BookingForm2Activity extends AppCompatActivity {

    private TextInputEditText name, email, phone;
    private Button continueToPaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form2);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        continueToPaymentButton = findViewById(R.id.continueToPaymentButton);

        // Continue to Payment Button Click Listener
        continueToPaymentButton.setOnClickListener(v -> {
            String fullName = name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String phoneNumber = phone.getText().toString().trim();

            if (fullName.isEmpty() || userEmail.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Retrieve USER_ID and hotel object from the intent
                String userId = getIntent().getStringExtra("USER_ID");
                Hotel hotel = getIntent().getParcelableExtra("hotel");

                // Debug logs to verify USER_ID and hotel object
                Log.d("BookingForm2Activity", "USER_ID: " + userId);
                Log.d("BookingForm2Activity", "Hotel: " + (hotel != null ? hotel.getName() : "null"));

                if (userId == null) {
                    Log.e("BookingForm2Activity", "USER_ID is null");
                    Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Proceed to the payment form
                Intent intent = new Intent(BookingForm2Activity.this, PaymentFormActivity.class);
                intent.putExtra("checkIn", getIntent().getStringExtra("checkIn"));
                intent.putExtra("checkOut", getIntent().getStringExtra("checkOut"));
                intent.putExtra("guests", getIntent().getStringExtra("guests"));
                intent.putExtra("specialRequests", getIntent().getStringExtra("specialRequests"));
                intent.putExtra("name", fullName);
                intent.putExtra("email", userEmail);
                intent.putExtra("phone", phoneNumber);
                intent.putExtra("hotel", hotel); // Pass the hotel object
                intent.putExtra("USER_ID", userId); // Pass the USER_ID
                startActivity(intent);
            }
        });

        // Inside onCreate
        String userId = getIntent().getStringExtra("USER_ID");
        Hotel hotel = getIntent().getParcelableExtra("hotel");
        Log.d("BookingForm2Activity", "USER_ID: " + userId);
        Log.d("BookingForm2Activity", "Hotel: " + (hotel != null ? hotel.getName() : "null"));
    }
}