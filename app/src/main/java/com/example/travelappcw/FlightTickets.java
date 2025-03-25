package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
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
    private String loggedInUsername; // Ensure consistency with HotelDetailsActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tickets);

        loggedInUsername = getIntent().getStringExtra("USER_ID");

        if (loggedInUsername == null) {
            Log.e("FlightTickets", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.d("FlightTickets", "USER_ID: " + loggedInUsername);
        }

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewFlights);
        filterSpinner = findViewById(R.id.filterSpinner);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        flightAdapter = new FlightAdapter(flightList, loggedInUsername); // Pass USER_ID to adapter
        recyclerView.setAdapter(flightAdapter);

        loadFlights();

        setupFilter();

        MaterialButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
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

        String[] filterOptions = {"All Flights", "Price: Low to High", "Price: High to Low", "Shortest Duration"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_item,
                filterOptions
        );


        adapter.setDropDownViewResource(R.layout.custom_spinner_item);

        filterSpinner.setAdapter(adapter);

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
            case 1:
                flightList.sort((f1, f2) -> {
                    int price1 = Integer.parseInt(f1.getPrice().replace("£", "")); // Remove "£" and convert to int
                    int price2 = Integer.parseInt(f2.getPrice().replace("£", "")); // Remove "£" and convert to int
                    return Integer.compare(price1, price2);
                });
                break;
            case 2:
                flightList.sort((f1, f2) -> {
                    int price1 = Integer.parseInt(f1.getPrice().replace("£", "")); // Remove "£" and convert to int
                    int price2 = Integer.parseInt(f2.getPrice().replace("£", "")); // Remove "£" and convert to int
                    return Integer.compare(price2, price1);
                });
                break;
            case 3:
                flightList.sort((f1, f2) -> f1.getDuration().compareTo(f2.getDuration()));
                break;
            default:
                loadFlights();
                return;
        }
        flightAdapter.notifyDataSetChanged();


    }

}
