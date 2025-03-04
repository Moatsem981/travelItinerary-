package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ViewHotelsActivity extends AppCompatActivity implements HotelAdapter.OnReserveButtonClickListener {

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_hotels_activity);

        recyclerView = findViewById(R.id.recyclerViewHotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hotelAdapter = new HotelAdapter(this, hotelList, this); // Pass 'this' as the listener
        recyclerView.setAdapter(hotelAdapter);

        fetchHotelData();
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

                            Hotel hotel = new Hotel(name, location, price, ratings, description, amenities, imageUrls);
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
        // Handle Reserve Button Click
        Intent intent = new Intent(this, HotelDetailsActivity.class);
        intent.putExtra("hotel", hotel); // Pass the hotel object to HotelDetailsActivity
        startActivity(intent);
    }
}