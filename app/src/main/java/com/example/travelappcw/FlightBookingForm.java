package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class FlightBookingForm extends AppCompatActivity {

    private TextInputEditText fullName, phoneNumber, address, passengers, specialRequests;
    private Button continueButton;
    private String loggedInUsername;
    private String airline, flightNumber, departure, arrival, price, departureDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_booking_form);

        // Initialize views (removed departureDate)
        fullName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        passengers = findViewById(R.id.passengers);
        specialRequests = findViewById(R.id.specialRequests);
        continueButton = findViewById(R.id.continueButton);

        // Retrieve logged-in username and flight details from Intent
        loggedInUsername = getIntent().getStringExtra("USER_ID");
        airline = getIntent().getStringExtra("airline");
        flightNumber = getIntent().getStringExtra("flightNumber");
        departure = getIntent().getStringExtra("departure");
        arrival = getIntent().getStringExtra("arrival");
        price = getIntent().getStringExtra("price");
        departureDate = getIntent().getStringExtra("departureDate"); // ✅ Retrieve but not display

        if (loggedInUsername == null) {
            Log.e("FlightBookingForm", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Handle "Proceed to Payment" button click
        continueButton.setOnClickListener(v -> {
            String name = fullName.getText().toString().trim();
            String phone = phoneNumber.getText().toString().trim();
            String userAddress = address.getText().toString().trim();
            String numPassengers = passengers.getText().toString().trim();
            String requests = specialRequests.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || userAddress.isEmpty() || numPassengers.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Pass booking details to the payment screen
                Intent intent = new Intent(FlightBookingForm.this, FlightBookingPayment.class);
                intent.putExtra("USER_ID", loggedInUsername);
                intent.putExtra("fullName", name);
                intent.putExtra("phoneNumber", phone);
                intent.putExtra("address", userAddress);
                intent.putExtra("departureDate", departureDate);
                intent.putExtra("passengers", numPassengers);
                intent.putExtra("specialRequests", requests);
                intent.putExtra("airline", airline);
                intent.putExtra("flightNumber", flightNumber);
                intent.putExtra("departure", departure);
                intent.putExtra("arrival", arrival);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });
    }
}
