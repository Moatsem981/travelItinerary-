package com.example.travelappcw;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppMainPage extends AppCompatActivity {

    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_page);

        // Get the logged-in username from the intent
        loggedInUsername = getIntent().getStringExtra("USER_ID");

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // Pass USER_ID to the fragment on first login
        if (savedInstanceState == null) {
            ItineraryFragment itineraryFragment = new ItineraryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("USER_ID", loggedInUsername);
            itineraryFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, itineraryFragment)
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_itinerary) {
                selectedFragment = new ItineraryFragment();
            } else if (item.getItemId() == R.id.nav_bookings) {
                selectedFragment = new BookingsFragment();
            } else if (item.getItemId() == R.id.nav_map) {
                selectedFragment = new MapFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", loggedInUsername);
                selectedFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
