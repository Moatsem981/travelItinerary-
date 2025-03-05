package com.example.travelappcw;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class HotelDetailsActivity extends AppCompatActivity {

    private ViewPager2 imageSlider;
    private TabLayout tabLayout;
    private TextView hotelName, hotelLocation, hotelPrice, hotelRatings, hotelDescription;
    private TextView amenitiesList;
    private Button reserveButton;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);


        imageSlider = findViewById(R.id.imageSlider);
        tabLayout = findViewById(R.id.tabLayout);
        hotelName = findViewById(R.id.hotelName);
        hotelLocation = findViewById(R.id.hotelLocation);
        hotelPrice = findViewById(R.id.hotelPrice);
        hotelRatings = findViewById(R.id.hotelRatings);
        hotelDescription = findViewById(R.id.hotelDescription);
        amenitiesList = findViewById(R.id.amenitiesList);
        reserveButton = findViewById(R.id.reserveButton);

        loggedInUsername = getIntent().getStringExtra("USER_ID");
        Hotel hotel = getIntent().getParcelableExtra("hotel");

        Log.d("HotelDetailsActivity", "USER_ID: " + loggedInUsername);
        Log.d("HotelDetailsActivity", "Hotel: " + (hotel != null ? hotel.getName() : "null"));

        if (loggedInUsername == null) {
            Log.e("HotelDetailsActivity", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (hotel != null) {
            Log.d("HotelDetails", "Hotel Name: " + hotel.getName());
            Log.d("HotelDetails", "Image URLs: " + hotel.getImageUrls());

            hotelName.setText(hotel.getName());
            hotelLocation.setText(hotel.getLocation());
            hotelPrice.setText(hotel.getPrice());

            String ratingsText = "⭐ " + hotel.getRatings();
            SpannableString spannableString = new SpannableString(ratingsText);
            spannableString.setSpan(
                    new ForegroundColorSpan(Color.YELLOW),
                    0, 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            hotelRatings.setText(spannableString);
            hotelDescription.setText(hotel.getDescription());

            StringBuilder amenitiesText = new StringBuilder();
            for (String amenity : hotel.getAmenities()) {
                amenitiesText.append("• ").append(amenity).append("\n");
            }
            amenitiesList.setText(amenitiesText.toString());
            List<String> validImageUrls = new ArrayList<>();
            for (String url : hotel.getImageUrls()) {
                if (url != null && !url.isEmpty() && !url.equals("https://via.placeholder.com/800x600.png?text=Hotel+Image")) {
                    validImageUrls.add(url);
                }
            }

            if (!validImageUrls.isEmpty()) {
                ImageSliderAdapter adapter = new ImageSliderAdapter(validImageUrls);
                imageSlider.setAdapter(adapter);


                new TabLayoutMediator(tabLayout, imageSlider, (tab, position) -> {
                }).attach();
            } else {
                imageSlider.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
            }
        } else {
            Log.e("HotelDetails", "Hotel object is null");
            Toast.makeText(this, "Error: Hotel details not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        reserveButton.setOnClickListener(v -> {
            if (hotel != null && loggedInUsername != null) {
                Intent intent = new Intent(HotelDetailsActivity.this, BookingForm1Activity.class);
                intent.putExtra("hotel", hotel);
                intent.putExtra("USER_ID", loggedInUsername);
                startActivity(intent);
            } else {
                Log.e("HotelDetailsActivity", "Hotel or USER_ID is null");
                Toast.makeText(this, "Error: Hotel details or user not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}