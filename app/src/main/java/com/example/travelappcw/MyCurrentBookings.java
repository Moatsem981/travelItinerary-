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

        // Initialize adapters (🔥 Updated HotelAdapter to remove Reserve button)
        hotelAdapter = new HotelAdapter(this, hotelBookingsList, null, true); // Hide Reserve Button
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
                String name = document.getString("hotel.name");
                String location = document.getString("hotel.location");
                String price = document.getString("hotel.price");
                String ratings = document.getString("hotel.ratings");
                String description = document.getString("hotel.description");
                List<String> amenities = (List<String>) document.get("hotel.amenities");
                List<String> imageUrls = (List<String>) document.get("hotel.imageUrls");

                Hotel hotel = new Hotel(name, location, price, ratings, description, amenities, imageUrls);
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
