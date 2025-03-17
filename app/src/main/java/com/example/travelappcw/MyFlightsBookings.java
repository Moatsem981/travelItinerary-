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

public class MyFlightsBookings extends AppCompatActivity {

    private RecyclerView flightRecyclerView;
    private FlightAdapter flightAdapter;
    private List<FlightModel> flightBookingsList = new ArrayList<>();
    private FirebaseFirestore db;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_flights_bookings);

        loggedInUsername = getIntent().getStringExtra("USER_ID");

        if (loggedInUsername == null) {
            Log.e("MyFlightsBookings", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        flightRecyclerView = findViewById(R.id.flightRecyclerView);
        flightRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        flightAdapter = new FlightAdapter(flightBookingsList, loggedInUsername);
        flightRecyclerView.setAdapter(flightAdapter);

        loadFlightBookings();
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
