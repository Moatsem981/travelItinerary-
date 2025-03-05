package com.example.travelappcw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class PaymentFormActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView totalCostTextView;
    private Button confirmPaymentButton;
    private ProgressDialog progressDialog;

    private String checkIn, checkOut, guests, specialRequests, fullName, userEmail, phoneNumber, loggedInUsername;
    private Hotel hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        totalCostTextView = findViewById(R.id.totalCostTextView);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing payment...");
        progressDialog.setCancelable(false);

        // Retrieve data from the intent with null checks
        Intent intent = getIntent();
        checkIn = intent.getStringExtra("checkIn");
        checkOut = intent.getStringExtra("checkOut");
        guests = intent.getStringExtra("guests");
        specialRequests = intent.getStringExtra("specialRequests");
        fullName = intent.getStringExtra("name");
        userEmail = intent.getStringExtra("email");
        phoneNumber = intent.getStringExtra("phone");
        loggedInUsername = intent.getStringExtra("LOGGED_IN_USERNAME");
        hotel = intent.getParcelableExtra("hotel");

        // Debug logs
        Log.d("PaymentFormActivity", "Logged in username: " + loggedInUsername);
        Log.d("PaymentFormActivity", "Hotel: " + (hotel != null ? hotel.getName() : "null"));


        // Set total cost dynamically if available
        if (hotel != null) {
            totalCostTextView.setText("Total to Pay: $" + hotel.getPrice());
        }

        // Confirm payment button listener
        confirmPaymentButton.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        // Show loading
        progressDialog.show();

        if (loggedInUsername == null || hotel == null) {
            Toast.makeText(this, "Error: Missing booking details!", Toast.LENGTH_LONG).show();
            Log.e("PaymentFormActivity", "Missing user or hotel information");
            progressDialog.dismiss();
            return;
        }

        // Save booking details to Firestore
        saveBookingToFirestore();
    }

    private void saveBookingToFirestore() {
        // Create a map for the booking details
        Map<String, Object> booking = new HashMap<>();
        booking.put("checkIn", checkIn);
        booking.put("checkOut", checkOut);
        booking.put("guests", guests);
        booking.put("specialRequests", specialRequests);
        booking.put("name", fullName);
        booking.put("email", userEmail);
        booking.put("phone", phoneNumber);

        // Add hotel details (as a nested map)
        Map<String, Object> hotelMap = new HashMap<>();
        hotelMap.put("name", hotel.getName());
        hotelMap.put("location", hotel.getLocation());
        hotelMap.put("price", hotel.getPrice());
        hotelMap.put("ratings", hotel.getRatings());
        hotelMap.put("description", hotel.getDescription());
        hotelMap.put("amenities", hotel.getAmenities());
        hotelMap.put("imageUrls", hotel.getImageUrls());
        booking.put("hotel", hotelMap);

        // Save the booking under the user's document in Firestore
        db.collection("users")
                .document(loggedInUsername)
                .collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Log.d("PaymentFormActivity", "Booking saved with ID: " + documentReference.getId());
                    Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    // Navigate to confirmation screen
                    Intent intent = new Intent(this, ConfirmationActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("PaymentFormActivity", "Error saving booking", e);
                    Toast.makeText(this, "Payment Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                });
    }
}
