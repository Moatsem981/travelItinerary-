package com.example.travelappcw;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MyCurrentBookings extends AppCompatActivity {

    private RecyclerView hotelRecyclerView, flightRecyclerView;
    private HotelAdapter hotelAdapter;
    private FlightAdapter flightAdapter;
    private List<Hotel> hotelBookingsList = new ArrayList<>();
    private List<FlightModel> flightBookingsList = new ArrayList<>();
    private FirebaseFirestore db;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_current_bookings);

        // Retrieve logged-in username
        loggedInUsername = getIntent().getStringExtra("USER_ID");

        if (loggedInUsername == null) {
            Log.e("MyCurrentBookings", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerViews
        hotelRecyclerView = findViewById(R.id.hotelRecyclerView);
        flightRecyclerView = findViewById(R.id.flightRecyclerView);

        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flightRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapters
        hotelAdapter = new HotelAdapter(this, hotelBookingsList, hotel -> {});
        flightAdapter = new FlightAdapter(flightBookingsList, loggedInUsername);

        hotelRecyclerView.setAdapter(hotelAdapter);
        flightRecyclerView.setAdapter(flightAdapter);

        // Load bookings from Firestore
        loadHotelBookings();
        loadFlightBookings();
    }

    private void loadHotelBookings() {
        CollectionReference bookingsRef = db.collection("Users").document(loggedInUsername).collection("Bookings");
        bookingsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            hotelBookingsList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Hotel hotel = document.toObject(Hotel.class);
                hotelBookingsList.add(hotel);
            }
            hotelAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.e("Firestore", "Failed to load hotel bookings", e));
    }

    private void loadFlightBookings() {
        CollectionReference flightBookingsRef = db.collection("Users").document(loggedInUsername).collection("FlightBookings");
        flightBookingsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            flightBookingsList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                FlightModel flight = document.toObject(FlightModel.class);
                flightBookingsList.add(flight);
            }
            flightAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.e("Firestore", "Failed to load flight bookings", e));
    }
}
