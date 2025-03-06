package com.example.travelappcw;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ItineraryFragment extends Fragment {

    private TextInputEditText inputDay, inputTime, inputActivity;
    private MaterialButton addButton;
    private RecyclerView recyclerView;
    private ItineraryAdapter adapter;
    private List<ItineraryItem> itineraryList = new ArrayList<>();
    private FirebaseFirestore db;
    private CollectionReference itineraryRef;
    private String loggedInUsername;
    private ExtendedFloatingActionButton fabAddItinerary;
    private View formContainer;

    public ItineraryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itinerary, container, false);

        if (getArguments() != null) {
            loggedInUsername = getArguments().getString("USER_ID");
        }

        if (loggedInUsername == null) {
            Log.e("FirestoreDebug", "No user logged in");
            Toast.makeText(getContext(), "Error: No user logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        Log.d("FirestoreDebug", "ItineraryFragment loaded for user: " + loggedInUsername);

        db = FirebaseFirestore.getInstance();
        itineraryRef = db.collection("Users").document(loggedInUsername).collection("itineraries");

        // Bind UI elements
        inputDay = view.findViewById(R.id.inputDay);
        inputTime = view.findViewById(R.id.inputTime);
        inputActivity = view.findViewById(R.id.inputActivity);
        addButton = view.findViewById(R.id.addButton);
        recyclerView = view.findViewById(R.id.recyclerViewItinerary);
        formContainer = view.findViewById(R.id.formContainer);
        fabAddItinerary = view.findViewById(R.id.fabAddItinerary);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItineraryAdapter(itineraryList, itineraryRef, getContext());
        recyclerView.setAdapter(adapter);

        // Load Itineraries from Firestore
        loadItineraryRealTime();

        // Handle FAB click to toggle form visibility
        fabAddItinerary.setOnClickListener(v -> {
            if (formContainer.getVisibility() == View.GONE) {
                formContainer.setVisibility(View.VISIBLE);
                fabAddItinerary.setText("Close Form");
            } else {
                formContainer.setVisibility(View.GONE);
                fabAddItinerary.setText("Add Itinerary");
            }
        });

        // Handle adding new itinerary
        addButton.setOnClickListener(v -> addItinerary());

        return view;
    }

    private void addItinerary() {
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
                    item.setId(documentReference.getId());
                    Log.d("FirestoreDebug", "Itinerary added: " + documentReference.getId());
                    clearFields();
                    formContainer.setVisibility(View.GONE);
                    fabAddItinerary.setText("Add Itinerary");
                    Toast.makeText(getContext(), "Itinerary added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreDebug", "Error adding itinerary", e);
                    Toast.makeText(getContext(), "Failed to add itinerary", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadItineraryRealTime() {
        itineraryRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (error != null) {
                Log.e("FirestoreDebug", "Failed to load itineraries", error);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load itineraries", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            itineraryList.clear();
            if (queryDocumentSnapshots != null) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ItineraryItem item = document.toObject(ItineraryItem.class);
                    item.setId(document.getId());
                    itineraryList.add(item);
                }
            }

            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }

            Log.d("FirestoreDebug", "RecyclerView updated with " + itineraryList.size() + " items");
        });
    }

    private void clearFields() {
        inputDay.setText("");
        inputTime.setText("");
        inputActivity.setText("");
    }
}
