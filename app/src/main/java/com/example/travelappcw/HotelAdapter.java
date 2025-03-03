package com.example.travelappcw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private Context context;
    private List<Hotel> hotelList;

    public HotelAdapter(Context context, List<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);

        // Prevent crash by checking if fields are null
        holder.hotelName.setText(hotel.getName() != null ? hotel.getName() : "Hotel Name Not Available");
        holder.hotelLocation.setText(hotel.getLocation() != null ? hotel.getLocation() : "Location Unknown");
        holder.hotelPrice.setText(hotel.getPrice() != null ? "Price: " + hotel.getPrice() : "Price: N/A");
        holder.hotelRatings.setText(hotel.getRatings() != null ? "⭐ " + hotel.getRatings() : "⭐ N/A");

        // Handle Image Loading with Placeholder
        if (hotel.getImageUrl() != null && !hotel.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(hotel.getImageUrl())
                    .placeholder(R.drawable.default_hotel) // Use a valid drawable
                    .into(holder.hotelImage);
        } else {
            holder.hotelImage.setImageResource(R.drawable.default_hotel); // Fallback image
        }
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView hotelName, hotelLocation, hotelPrice, hotelRatings;
        ImageView hotelImage;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelLocation = itemView.findViewById(R.id.hotelLocation);
            hotelPrice = itemView.findViewById(R.id.hotelPrice);
            hotelRatings = itemView.findViewById(R.id.hotelRatings);
            hotelImage = itemView.findViewById(R.id.hotelImage);
        }
    }
}