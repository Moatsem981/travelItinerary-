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

    private String checkIn, checkOut, guests, specialRequests, fullName, userEmail, phoneNumber;
    private String loggedInUsername;  // 🔥 Now using username like in ItineraryFragment
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

        // Retrieve data from the intent
        Intent intent = getIntent();
        checkIn = intent.getStringExtra("checkIn");
        checkOut = intent.getStringExtra("checkOut");
        guests = intent.getStringExtra("guests");
        specialRequests = intent.getStringExtra("specialRequests");
        fullName = intent.getStringExtra("name");
        userEmail = intent.getStringExtra("email");
        phoneNumber = intent.getStringExtra("phone");
        hotel = intent.getParcelableExtra("hotel");

        // ✅ Get the logged-in username, just like ItineraryFragment does!
        loggedInUsername = intent.getStringExtra("USER_ID");

        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            Log.e("PaymentFormActivity", "Error: No logged-in user found!");
            Toast.makeText(this, "Error: User is not logged in!", Toast.LENGTH_LONG).show();
            finish(); // Close activity if no user is logged in
            return;
        }

        Log.d("PaymentFormActivity", "Logged in username: " + loggedInUsername);

        // Set total cost dynamically if available
        if (hotel != null) {
            totalCostTextView.setText("Total to Pay: $" + hotel.getPrice());
        } else {
            Log.e("PaymentFormActivity", "Hotel object is null!");
            Toast.makeText(this, "Error: Hotel details missing!", Toast.LENGTH_LONG).show();
        }

        // Confirm payment button listener
        confirmPaymentButton.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        progressDialog.show();

        // Ensure correct username is fetched
        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            Log.e("PaymentFormActivity", "❌ ERROR: No user logged in! USER_ID is NULL.");
            Toast.makeText(this, "Error: User is not logged in!", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            Log.d("PaymentFormActivity", "✅ User logged in as: " + loggedInUsername);
        }


        if (hotel == null) {
            Log.e("PaymentFormActivity", "Error: Hotel is null!");
            Toast.makeText(this, "Error: Hotel details missing!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }

        if (checkIn == null || checkOut == null || guests == null || fullName == null || userEmail == null || phoneNumber == null) {
            Log.e("PaymentFormActivity", "Error: Missing booking information!");
            Toast.makeText(this, "Error: Booking details incomplete!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }

        // Save booking to Firestore under "Users/{loggedInUsername}/Bookings/"
        saveBookingToFirestore();
    }

    private void saveBookingToFirestore() {
        Map<String, Object> booking = new HashMap<>();
        booking.put("checkIn", checkIn);
        booking.put("checkOut", checkOut);
        booking.put("guests", guests);
        booking.put("specialRequests", specialRequests);
        booking.put("name", fullName);
        booking.put("email", userEmail);
        booking.put("phone", phoneNumber);

        // Add hotel details (nested map)
        Map<String, Object> hotelMap = new HashMap<>();
        hotelMap.put("name", hotel.getName());
        hotelMap.put("location", hotel.getLocation());
        hotelMap.put("price", hotel.getPrice());
        hotelMap.put("ratings", hotel.getRatings());
        hotelMap.put("description", hotel.getDescription());
        hotelMap.put("amenities", hotel.getAmenities());
        hotelMap.put("imageUrls", hotel.getImageUrls());
        booking.put("hotel", hotelMap);

        db.collection("Users")
                .document(loggedInUsername)
                .collection("Bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Log.d("PaymentFormActivity", "Booking saved under Users/" + loggedInUsername + "/Bookings/");
                    Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

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
