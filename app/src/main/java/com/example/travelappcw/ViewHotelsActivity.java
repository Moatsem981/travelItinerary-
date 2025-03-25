package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ViewHotelsActivity extends AppCompatActivity implements HotelAdapter.OnReserveButtonClickListener {

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList = new ArrayList<>();
    private String loggedInUsername; // it stores the logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_hotels_activity);

        loggedInUsername = getIntent().getStringExtra("USER_ID");
        if (loggedInUsername == null) {
            Log.e("ViewHotelsActivity", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if USER_ID is not available
        } else {
            Log.d("ViewHotelsActivity", "USER_ID: " + loggedInUsername);
        }

        recyclerView = findViewById(R.id.recyclerViewHotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // finally fixed the constructor call
        hotelAdapter = new HotelAdapter(this, hotelList, this, false);
        recyclerView.setAdapter(hotelAdapter);

        fetchHotelData();

        MaterialButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchHotelData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Hotels")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    hotelList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            String name = document.getString("name");
                            String location = document.getString("location");
                            String price = document.getString("price");
                            String ratings = document.getString("ratings");
                            String description = document.getString("description");
                            List<String> amenities = (List<String>) document.get("amenities");
                            List<String> imageUrls = (List<String>) document.get("imageUrls");

                            // here it retrieves latitude n longitude
                            double latitude = document.contains("latitude") ? document.getDouble("latitude") : 0.0;
                            double longitude = document.contains("longitude") ? document.getDouble("longitude") : 0.0;

                            // Debug log for verification
                            Log.d("FirestoreData", "Hotel: " + name + " | Lat: " + latitude + " | Lng: " + longitude);

                            Hotel hotel = new Hotel(name, location, price, ratings, description, amenities, imageUrls, latitude, longitude);
                            hotelList.add(hotel);
                        }
                    }
                    hotelAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load hotels", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error fetching hotels", e);
                });
    }

    @Override
    public void onReserveButtonClick(Hotel hotel) {
        if (loggedInUsername != null) {
            Intent intent = new Intent(this, HotelDetailsActivity.class);
            intent.putExtra("hotel", hotel);
            intent.putExtra("USER_ID", loggedInUsername);
            startActivity(intent);
        } else {
            Log.e("ViewHotelsActivity", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
