package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class BookingForm2Activity extends AppCompatActivity {

    private TextInputEditText name, email, phone;
    private Button continueToPaymentButton;
    private String loggedInUsername;
    private Hotel hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form2);

        // Initialize views
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        continueToPaymentButton = findViewById(R.id.continueToPaymentButton);

        // Retrieve loggedInUsername and hotel object
        loggedInUsername = getIntent().getStringExtra("USER_ID");
        hotel = getIntent().getParcelableExtra("hotel");

        // Debugging
        Log.d("BookingForm2Activity", "Logged in username: " + loggedInUsername);
        Log.d("BookingForm2Activity", "Hotel: " + (hotel != null ? hotel.getName() : "null"));

        continueToPaymentButton.setOnClickListener(v -> {
            String fullName = name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String phoneNumber = phone.getText().toString().trim();

            if (fullName.isEmpty() || userEmail.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(BookingForm2Activity.this, PaymentFormActivity.class);
                intent.putExtra("checkIn", getIntent().getStringExtra("checkIn"));
                intent.putExtra("checkOut", getIntent().getStringExtra("checkOut"));
                intent.putExtra("guests", getIntent().getStringExtra("guests"));
                intent.putExtra("specialRequests", getIntent().getStringExtra("specialRequests"));
                intent.putExtra("name", fullName);
                intent.putExtra("email", userEmail);
                intent.putExtra("phone", phoneNumber);
                intent.putExtra("hotel", hotel);
                intent.putExtra("USER_ID", loggedInUsername);
                startActivity(intent);
            }
        });
    }
}
