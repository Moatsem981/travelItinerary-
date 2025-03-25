package com.example.travelappcw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class FlightDetails extends AppCompatActivity {

    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);

        // Initialize views
        TextView airlineText = findViewById(R.id.airlineText);
        TextView flightNumberText = findViewById(R.id.flightNumberText);
        TextView departureText = findViewById(R.id.departureText);
        TextView arrivalText = findViewById(R.id.arrivalText);
        TextView priceText = findViewById(R.id.priceText);
        TextView durationText = findViewById(R.id.durationText);
        TextView departureTimeText = findViewById(R.id.departureTimeText);
        TextView arrivalTimeText = findViewById(R.id.arrivalTimeText);
        TextView baggageAllowanceText = findViewById(R.id.baggageAllowanceText);
        TextView flightClassText = findViewById(R.id.flightClassText);
        TextView layoversText = findViewById(R.id.layoversText);
        Button bookNowButton = findViewById(R.id.bookNowButton);

        loggedInUsername = getIntent().getStringExtra("USER_ID");

        if (loggedInUsername == null) {
            Log.e("FlightDetails", "ERROR: USER_ID is NULL!");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.d("FlightDetails", "USER_ID: " + loggedInUsername);
        }

        Intent intent = getIntent();
        String airline = intent.getStringExtra("airline");
        String flightNumber = intent.getStringExtra("flightNumber");
        String departure = intent.getStringExtra("departure");
        String arrival = intent.getStringExtra("arrival");
        String price = intent.getStringExtra("price");
        String duration = intent.getStringExtra("duration");
        String departureTime = intent.getStringExtra("departureTime");
        String arrivalTime = intent.getStringExtra("arrivalTime");
        String baggageAllowance = intent.getStringExtra("baggageAllowance");
        String flightClass = intent.getStringExtra("flightClass");
        int layovers = intent.getIntExtra("layovers", 0);

        airlineText.setText("Airline: " + airline);
        flightNumberText.setText("Flight Number: " + flightNumber);
        departureText.setText("Departure: " + departure);
        arrivalText.setText("Arrival: " + arrival);
        priceText.setText("Price: " + price);
        durationText.setText("Duration: " + duration);
        departureTimeText.setText("Departure Time: " + departureTime);
        arrivalTimeText.setText("Arrival Time: " + arrivalTime);
        baggageAllowanceText.setText("Baggage Allowance: " + baggageAllowance);
        flightClassText.setText("Class: " + flightClass);
        layoversText.setText("Layovers: " + layovers);

        bookNowButton.setOnClickListener(v -> {
            Intent bookIntent = new Intent(FlightDetails.this, FlightBookingForm.class);
            bookIntent.putExtra("USER_ID", loggedInUsername);
            bookIntent.putExtra("airline", airline);
            bookIntent.putExtra("flightNumber", flightNumber);
            bookIntent.putExtra("departure", departure);
            bookIntent.putExtra("arrival", arrival);
            bookIntent.putExtra("price", price);
            startActivity(bookIntent);
        });

        MaterialButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

    }
}
