package com.example.travelappcw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, AttractionAdapter.OnViewOnMapClickListener {

    private MapView mapView;
    private GoogleMap gMap;
    private RecyclerView recyclerView;
    private AttractionAdapter attractionAdapter;
    private List<Attraction> attractionList = new ArrayList<>();
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        recyclerView = view.findViewById(R.id.recyclerViewAttractions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        loadAttractions();

        return view;
    }

    private void loadAttractions() {
        db.collection("LocalAttractions").get().addOnSuccessListener(queryDocumentSnapshots -> {
            attractionList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Attraction attraction = document.toObject(Attraction.class);

                // Debugging: Log data retrieval
                System.out.println("Fetched attraction: " + attraction.getName());
                System.out.println("Fetched image URL: " + attraction.getImageUrl());

                attractionList.add(attraction);
            }
            attractionAdapter = new AttractionAdapter(attractionList, this);
            recyclerView.setAdapter(attractionAdapter);
        }).addOnFailureListener(e -> System.err.println("Firestore fetch failed: " + e.getMessage()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.5074, -0.1278), 12));
    }

    @Override
    public void onViewOnMapClick(double latitude, double longitude, String name) {
        LatLng location = new LatLng(latitude, longitude);
        gMap.clear();
        gMap.addMarker(new MarkerOptions().position(location).title(name));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
