package com.example.travelappcw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FlightBookingPayment extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView totalCostTextView;
    private Button confirmPaymentButton;
    private ProgressDialog progressDialog;

    private String fullName, phoneNumber, address, passengers, specialRequests;
    private String loggedInUsername, airline, flightNumber, departure, arrival, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_booking_payment);

        db = FirebaseFirestore.getInstance();

        totalCostTextView = findViewById(R.id.totalCostTextView);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing payment...");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        fullName = intent.getStringExtra("fullName");
        phoneNumber = intent.getStringExtra("phoneNumber");
        address = intent.getStringExtra("address");
        passengers = intent.getStringExtra("passengers");
        specialRequests = intent.getStringExtra("specialRequests");
        airline = intent.getStringExtra("airline");
        flightNumber = intent.getStringExtra("flightNumber");
        departure = intent.getStringExtra("departure");
        arrival = intent.getStringExtra("arrival");
        price = intent.getStringExtra("price");
        loggedInUsername = intent.getStringExtra("USER_ID");

        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            Log.e("FlightBookingPayment", "Error: No logged-in user found!");
            Toast.makeText(this, "Error: User is not logged in!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Log.d("FlightBookingPayment", "Logged in username: " + loggedInUsername);
        Log.d("FlightBookingPayment", "Flight: " + airline + " " + flightNumber);

        totalCostTextView.setText("Total to Pay: £" + price);

        confirmPaymentButton.setOnClickListener(v -> processPayment());

        MaterialButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void processPayment() {
        progressDialog.show();

        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            Log.e("FlightBookingPayment", "❌ ERROR: No user logged in! USER_ID is NULL.");
            Toast.makeText(this, "Error: User is not logged in!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        } else {
            Log.d("FlightBookingPayment", "✅ User logged in as: " + loggedInUsername);
        }

        if (fullName == null || phoneNumber == null || address == null ||
                passengers == null || airline == null || flightNumber == null ||
                departure == null || arrival == null || price == null) {

            Log.e("FlightBookingPayment", "Error: Missing flight booking information!");
            Toast.makeText(this, "Error: Booking details incomplete!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }

        saveBookingToFirestore();
    }

    private void saveBookingToFirestore() {
        Map<String, Object> booking = new HashMap<>();
        booking.put("fullName", fullName);
        booking.put("phoneNumber", phoneNumber);
        booking.put("address", address);
        booking.put("passengers", passengers);
        booking.put("specialRequests", specialRequests);
        booking.put("airline", airline);
        booking.put("flightNumber", flightNumber);
        booking.put("departure", departure);
        booking.put("arrival", arrival);
        booking.put("price", price);
        booking.put("userId", loggedInUsername);
        db.collection("Users")
                .document(loggedInUsername)
                .collection("FlightBookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FlightBookingPayment", "Booking saved under Users/" + loggedInUsername + "/FlightBookings/");
                    Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    Intent intent = new Intent(this, ConfirmationActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("FlightBookingPayment", "Error saving booking", e);
                    Toast.makeText(this, "Payment Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                });
    }
}
