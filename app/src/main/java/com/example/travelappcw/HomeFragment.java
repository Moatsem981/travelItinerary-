package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // ✅ Fix: Start ViewHotelsActivity when clicking "View Hotels"
        view.findViewById(R.id.viewHotelsCard).setOnClickListener(v -> {
            Log.d("NavigationDebug", "Navigating to ViewHotelsActivity");
            Intent intent = new Intent(getActivity(), ViewHotelsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
