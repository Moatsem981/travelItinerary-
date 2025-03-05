package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppMainPage extends AppCompatActivity {

    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_page);

        loggedInUsername = getIntent().getStringExtra("USER_ID");

        Log.d("AppMainPage", "USER_ID: " + loggedInUsername);

        if (loggedInUsername == null) {
            Log.e("AppMainPage", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        Log.d("FragmentDebug", "AppMainPage: onCreate() called");

        if (savedInstanceState == null) {
            Log.d("FragmentDebug", "Setting default fragment: HOME_FRAGMENT");
            loadFragment(new HomeFragment(), "HOME_FRAGMENT");
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Log.d("FragmentDebug", "BottomNav Item Selected: " + item.getItemId());

            Fragment selectedFragment = null;
            String tag = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                tag = "HOME_FRAGMENT";
            } else if (item.getItemId() == R.id.nav_itinerary) {
                selectedFragment = new ItineraryFragment();
                tag = "ITINERARY_FRAGMENT";
            } else if (item.getItemId() == R.id.nav_bookings) {
                selectedFragment = new SpeakTranslateFragment();
                tag = "BOOKINGS_FRAGMENT";
            } else if (item.getItemId() == R.id.nav_map) {
                selectedFragment = new MapFragment();
                tag = "MAP_FRAGMENT";
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                tag = "PROFILE_FRAGMENT";
            }

            if (selectedFragment != null) {
                Log.d("FragmentDebug", "Switching to fragment: " + tag);
                loadFragment(selectedFragment, tag);
            } else {
                Log.e("FragmentDebug", "No fragment selected!");
            }

            return true;
        });
    }


    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (loggedInUsername != null) {
            Bundle bundle = new Bundle();
            bundle.putString("USER_ID", loggedInUsername);
            fragment.setArguments(bundle);
        } else {
            Log.e("FirestoreDebug", "ERROR: USER_ID is NULL in AppMainPage!");
        }

        transaction.replace(R.id.fragmentContainer, fragment, tag);
        transaction.commit();
    }


    public void navigateToHotelDetails(Hotel hotel) {
        if (loggedInUsername != null) {
            Intent intent = new Intent(AppMainPage.this, HotelDetailsActivity.class);
            intent.putExtra("hotel", hotel); // Pass the hotel object
            intent.putExtra("USER_ID", loggedInUsername);
            startActivity(intent);
        } else {
            Log.e("AppMainPage", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}