package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private double hotelLatitude;
    private double hotelLongitude;
    private String hotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        hotelLatitude = intent.getDoubleExtra("HOTEL_LATITUDE", 0.0);
        hotelLongitude = intent.getDoubleExtra("HOTEL_LONGITUDE", 0.0);
        hotelName = intent.getStringExtra("HOTEL_NAME");

        Log.d("MapActivity", "Received Latitude: " + hotelLatitude + ", Longitude: " + hotelLongitude);

        if (hotelLatitude == 0.0 || hotelLongitude == 0.0) {
            Toast.makeText(this, "Invalid hotel location. Cannot display map.", Toast.LENGTH_LONG).show();
            Log.e("MapActivity", "Invalid coordinates received: " + hotelLatitude + ", " + hotelLongitude);
            finish(); // Close activity if no valid coordinates
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;


        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(true);


        if (hotelLatitude != 0.0 && hotelLongitude != 0.0) {
            LatLng hotelLocation = new LatLng(hotelLatitude, hotelLongitude);

            gMap.addMarker(new MarkerOptions()
                    .position(hotelLocation)
                    .title(hotelName));

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hotelLocation, 15f));
        } else {
            Toast.makeText(this, "Hotel location is not available!", Toast.LENGTH_SHORT).show();
            Log.e("MapActivity", "Error: Latitude or Longitude is zero.");
        }
    }
}
