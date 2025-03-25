package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView usernameText;
    private Button logoutButton;
    private String loggedInUsername;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameText = view.findViewById(R.id.usernameText);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Retrieve the username passed from AppMainPage
        if (getArguments() != null) {
            loggedInUsername = getArguments().getString("USER_ID", "Guest");
        } else {
            loggedInUsername = "Guest";
        }

        usernameText.setText(loggedInUsername);

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginSignUpPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
