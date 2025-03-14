package com.example.travelappcw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private TextView usernameText;
    private Button logoutButton;
    private Switch darkModeSwitch;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = requireActivity().getSharedPreferences("settings", getActivity().MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameText = view.findViewById(R.id.usernameText);
        logoutButton = view.findViewById(R.id.logoutButton);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            usernameText.setText(user.getDisplayName() != null ? user.getDisplayName() : "Guest");
        }

        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(isDarkMode);
        applyDarkMode(isDarkMode);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply();
            applyDarkMode(isChecked);
        });

        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), LoginSignUpPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void applyDarkMode(boolean enable) {
        if (enable) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
