package com.example.travelappcw;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<FlightModel> flightList;
    private String userId; // Add this field to store the USER_ID

    public FlightAdapter(List<FlightModel> flightList, String userId) {
        this.flightList = flightList;
        this.userId = userId; // Initialize USER_ID
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_ticket, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        FlightModel flight = flightList.get(position);
        holder.airlineText.setText(flight.getAirline());
        holder.flightNumberText.setText(flight.getFlightNumber());
        holder.departureText.setText(flight.getDeparture());
        holder.arrivalText.setText(flight.getArrival());
        holder.priceText.setText("£" + flight.getPrice());

        // Set click listener for the "View Details" button
        holder.viewDetailsButton.setOnClickListener(v -> {
            // Create an Intent to open the FlightDetails activity
            Intent intent = new Intent(v.getContext(), FlightDetails.class);

            // Pass the flight data to the FlightDetails activity
            intent.putExtra("airline", flight.getAirline());
            intent.putExtra("flightNumber", flight.getFlightNumber());
            intent.putExtra("departure", flight.getDeparture());
            intent.putExtra("arrival", flight.getArrival());
            intent.putExtra("price", flight.getPrice());
            intent.putExtra("duration", flight.getDuration());
            intent.putExtra("departureTime", flight.getDepartureTime());
            intent.putExtra("arrivalTime", flight.getArrivalTime());
            intent.putExtra("baggageAllowance", flight.getBaggageAllowance());
            intent.putExtra("flightClass", flight.getFlightClass());
            intent.putExtra("layovers", flight.getLayovers());

            // Pass the USER_ID to the FlightDetails activity
            intent.putExtra("USER_ID", userId);

            // Start the FlightDetails activity
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView airlineText, flightNumberText, departureText, arrivalText, priceText;
        ImageView flightImage;
        Button viewDetailsButton;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            airlineText = itemView.findViewById(R.id.airlineText);
            flightNumberText = itemView.findViewById(R.id.flightNumberText);
            departureText = itemView.findViewById(R.id.departureText);
            arrivalText = itemView.findViewById(R.id.arrivalText);
            priceText = itemView.findViewById(R.id.priceText);
            flightImage = itemView.findViewById(R.id.flightImage);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }
}