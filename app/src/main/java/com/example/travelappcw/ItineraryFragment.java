package com.example.travelappcw;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItineraryFragment extends Fragment {

    private EditText inputDay, inputTime, inputActivity;
    private Button addButton;
    private RecyclerView recyclerView;
    private ItineraryAdapter adapter;
    private List<ItineraryItem> itineraryList = new ArrayList<>();
    private FirebaseFirestore db;
    private String loggedInUsername;

    public ItineraryFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itinerary, container, false);

        // Get the logged-in username from intent (passed from LoginSignUpPage)
        loggedInUsername = getActivity().getIntent().getStringExtra("USER_ID");
        if (loggedInUsername == null) {
            Log.e("Firestore", "No user logged in");
            return view;
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        CollectionReference itineraryRef = db.collection("Users").document(loggedInUsername).collection("itineraries");

        // Initialize UI elements
        inputDay = view.findViewById(R.id.inputDay);
        inputTime = view.findViewById(R.id.inputTime);
        inputActivity = view.findViewById(R.id.inputActivity);
        addButton = view.findViewById(R.id.addButton);
        recyclerView = view.findViewById(R.id.recyclerViewItinerary);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItineraryAdapter(itineraryList, db, loggedInUsername);
        recyclerView.setAdapter(adapter);

        // Load existing itinerary from Firestore
        loadItinerary(itineraryRef);

        // Ensure the button is clickable
        addButton.setEnabled(true);
        addButton.setOnClickListener(v -> {
            String day = inputDay.getText().toString().trim();
            String time = inputTime.getText().toString().trim();
            String activity = inputActivity.getText().toString().trim();

            if (!day.isEmpty() && !time.isEmpty() && !activity.isEmpty()) {
                ItineraryItem item = new ItineraryItem(day, time, activity);
                itineraryRef.add(item)
                        .addOnSuccessListener(documentReference -> {
                            item.setId(documentReference.getId()); // Store Firestore ID
                            itineraryList.add(item);
                            adapter.notifyDataSetChanged();
                            clearFields();
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error adding itinerary", e));
            }
        });

        return view;
    }

    private void loadItinerary(CollectionReference itineraryRef) {
        itineraryRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            itineraryList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ItineraryItem item = document.toObject(ItineraryItem.class);
                item.setId(document.getId()); // Store Firestore document ID
                itineraryList.add(item);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void clearFields() {
        inputDay.setText("");
        inputTime.setText("");
        inputActivity.setText("");
    }
}
