package com.example.travelappcw;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class ItineraryFragment extends Fragment {

    private EditText inputDay, inputTime, inputActivity;
    private Button addButton;
    private RecyclerView recyclerView;
    private ItineraryAdapter adapter;
    private List<ItineraryItem> itineraryList = new ArrayList<>();
    private FirebaseFirestore db;
    private CollectionReference itineraryRef;
    private String loggedInUsername;

    public ItineraryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itinerary, container, false);

        // Retrieve USER_ID from arguments (passed from AppMainPage)
        if (getArguments() != null) {
            loggedInUsername = getArguments().getString("USER_ID");
        }

        if (loggedInUsername == null) {
            Log.e("FirestoreDebug", "No user logged in");
            Toast.makeText(getContext(), "Error: No user logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        Log.d("FirestoreDebug", "ItineraryFragment loaded for user: " + loggedInUsername);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        itineraryRef = db.collection("Users").document(loggedInUsername).collection("itineraries");

        // Initialize UI elements
        inputDay = view.findViewById(R.id.inputDay);
        inputTime = view.findViewById(R.id.inputTime);
        inputActivity = view.findViewById(R.id.inputActivity);
        addButton = view.findViewById(R.id.addButton);
        recyclerView = view.findViewById(R.id.recyclerViewItinerary);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItineraryAdapter(itineraryList, itineraryRef, getContext());
        recyclerView.setAdapter(adapter);

        // Load existing itinerary from Firestore in real-time
        loadItineraryRealTime();

        // Add itinerary button click listener
        addButton.setOnClickListener(v -> {
            String day = inputDay.getText().toString().trim();
            String time = inputTime.getText().toString().trim();
            String activity = inputActivity.getText().toString().trim();

            if (day.isEmpty() || time.isEmpty() || activity.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            ItineraryItem item = new ItineraryItem(day, time, activity);

            itineraryRef.add(item)
                    .addOnSuccessListener(documentReference -> {
                        item.setId(documentReference.getId()); // Store Firestore ID
                        Log.d("FirestoreDebug", "Itinerary added: " + documentReference.getId());

                        // Clear input fields
                        clearFields();

                        Toast.makeText(getContext(), "Itinerary added successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreDebug", "Error adding itinerary", e);
                        Toast.makeText(getContext(), "Failed to add itinerary", Toast.LENGTH_SHORT).show();
                    });
        });

        return view;
    }

    /**
     * Loads user-specific itineraries from Firestore in real-time.
     */
    private void loadItineraryRealTime() {
        itineraryRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FirestoreDebug", "Failed to load itineraries", error);
                    Toast.makeText(getContext(), "Failed to load itineraries", Toast.LENGTH_SHORT).show();
                    return;
                }

                itineraryList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ItineraryItem item = document.toObject(ItineraryItem.class);
                    item.setId(document.getId()); // Store Firestore document ID
                    itineraryList.add(item);
                }

                // Refresh RecyclerView on the UI thread
                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

                Log.d("FirestoreDebug", "RecyclerView updated with " + itineraryList.size() + " items");
            }
        });
    }

    /**
     * Clears input fields after adding an itinerary.
     */
    private void clearFields() {
        inputDay.setText("");
        inputTime.setText("");
        inputActivity.setText("");
    }
}
