package com.example.travelappcw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class PaymentFormActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView totalCostTextView;
    private Button confirmPaymentButton;
    private ProgressDialog progressDialog;

    private String checkIn, checkOut, guests, specialRequests, fullName, userEmail, phoneNumber;
    private String loggedInEmail, correctUsername;
    private Hotel hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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

        // Get logged-in user's email from FirebaseAuth
        if (auth.getCurrentUser() != null) {
            loggedInEmail = auth.getCurrentUser().getEmail();
            Log.d("PaymentFormActivity", "Logged in with email: " + loggedInEmail);
            fetchCorrectUsername();
        } else {
            Log.e("PaymentFormActivity", "Error: No user logged in!");
            Toast.makeText(this, "Error: User is not logged in!", Toast.LENGTH_LONG).show();
        }

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

    /**
     * Fetches the correct username from Firestore using the logged-in email.
     */
    private void fetchCorrectUsername() {
        if (loggedInEmail == null) {
            Log.e("PaymentFormActivity", "Error: No email found for logged-in user!");
            return;
        }

        // Query Firestore: Find user document where "email" matches
        db.collection("Users")
                .whereEqualTo("email", loggedInEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        correctUsername = document.getId(); // Get username from document ID
                        Log.d("PaymentFormActivity", "Correct Username found: " + correctUsername);
                    } else {
                        Log.e("PaymentFormActivity", "No user found with email: " + loggedInEmail);
                        Toast.makeText(this, "User data not found!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PaymentFormActivity", "Error fetching username", e);
                    Toast.makeText(this, "Error fetching user data!", Toast.LENGTH_LONG).show();
                });
    }

    private void processPayment() {
        progressDialog.show();

        // Ensure correct username is fetched
        if (correctUsername == null || correctUsername.isEmpty()) {
            Log.e("PaymentFormActivity", "Error: correctUsername is null or empty!");
            Toast.makeText(this, "Error: User not recognized!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
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

        // Save booking to Firestore under "Users/{correctUsername}/Bookings/"
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

        // Save booking under correct user in Firestore
        db.collection("Users")
                .document(correctUsername)
                .collection("Bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Log.d("PaymentFormActivity", "Booking saved under Users/" + correctUsername + "/Bookings/");
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
