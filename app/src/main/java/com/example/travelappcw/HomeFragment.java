package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private String loggedInUsername;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle args = getArguments();
        if (args != null) {
            loggedInUsername = args.getString("USER_ID");
            Log.d("HomeFragment", "USER_ID: " + loggedInUsername);
        } else {
            Log.e("HomeFragment", "ERROR: USER_ID is NULL!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.viewHotelsCard).setOnClickListener(v -> {
            Log.d("NavigationDebug", "Navigating to ViewHotelsActivity");

            if (loggedInUsername != null) {
                Intent intent = new Intent(getActivity(), ViewHotelsActivity.class);
                intent.putExtra("USER_ID", loggedInUsername); // Pass the USER_ID
                startActivity(intent);
            } else {
                Log.e("HomeFragment", "ERROR: USER_ID is NULL!");
                Toast.makeText(getActivity(), "Error: User not logged in", Toast.LENGTH_SHORT).show();
            }
        });


        view.findViewById(R.id.viewFlightsCard).setOnClickListener(v -> {
            Log.d("NavigationDebug", "Navigating to FlightTickets Activity");

            if (loggedInUsername != null) {
                Intent intent = new Intent(getActivity(), FlightTickets.class);
                intent.putExtra("USER_ID", loggedInUsername); // Pass the USER_ID if needed
                startActivity(intent);
            } else {
                Log.e("HomeFragment", "ERROR: USER_ID is NULL!");
                Toast.makeText(getActivity(), "Error: User not logged in", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}