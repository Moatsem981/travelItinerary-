package com.example.travelappcw;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FlightTickets extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FlightAdapter flightAdapter;
    private List<FlightModel> flightList = new ArrayList<>();
    private FirebaseFirestore db;
    private Spinner filterSpinner;
    private String userId; // Add this field to store the USER_ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tickets);

        // Retrieve the USER_ID from the Intent
        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            Log.e("FlightTickets", "ERROR: USER_ID is NULL!");
        } else {
            Log.d("FlightTickets", "USER_ID: " + userId);
        }

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerViewFlights);
        filterSpinner = findViewById(R.id.filterSpinner);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        flightAdapter = new FlightAdapter(flightList, userId); // Pass USER_ID to the adapter
        recyclerView.setAdapter(flightAdapter);

        // Load flights from Firestore
        loadFlights();

        // Set up filter dropdown
        setupFilter();
    }

    private void loadFlights() {
        CollectionReference flightsRef = db.collection("Flights");
        flightsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Firestore", "Error loading flights", e);
                    return;
                }

                flightList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    FlightModel flight = document.toObject(FlightModel.class);
                    flightList.add(flight);
                }
                flightAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupFilter() {
        // Define the filter options
        String[] filterOptions = {"All Flights", "Price: Low to High", "Price: High to Low", "Shortest Duration"};

        // Create an ArrayAdapter using the custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_item, // Custom layout for dropdown items
                filterOptions
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);

        // Apply the adapter to the Spinner
        filterSpinner.setAdapter(adapter);

        // Set up the Spinner item selection listener
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterFlights(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterFlights(int filterOption) {
        if (flightList.isEmpty()) return;

        switch (filterOption) {
            case 1: // Sort by Price Low to High
                flightList.sort((f1, f2) -> {
                    int price1 = Integer.parseInt(f1.getPrice().replace("£", "")); // Remove "£" and convert to int
                    int price2 = Integer.parseInt(f2.getPrice().replace("£", "")); // Remove "£" and convert to int
                    return Integer.compare(price1, price2);
                });
                break;
            case 2: // Sort by Price High to Low
                flightList.sort((f1, f2) -> {
                    int price1 = Integer.parseInt(f1.getPrice().replace("£", "")); // Remove "£" and convert to int
                    int price2 = Integer.parseInt(f2.getPrice().replace("£", "")); // Remove "£" and convert to int
                    return Integer.compare(price2, price1);
                });
                break;
            case 3: // Sort by Shortest Duration
                flightList.sort((f1, f2) -> f1.getDuration().compareTo(f2.getDuration()));
                break;
            default: // Reset to default (no sorting)
                loadFlights();
                return;
        }
        flightAdapter.notifyDataSetChanged();
    }
}