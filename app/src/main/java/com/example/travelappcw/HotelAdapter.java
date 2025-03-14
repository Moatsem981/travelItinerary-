package com.example.travelappcw;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private Context context;
    private List<Hotel> hotelList;
    private OnReserveButtonClickListener reserveButtonClickListener;
    private boolean hideReserveButton;

    public interface OnReserveButtonClickListener {
        void onReserveButtonClick(Hotel hotel);
    }

    // Constructor with an additional flag to hide the reserve button
    public HotelAdapter(Context context, List<Hotel> hotelList, OnReserveButtonClickListener listener, boolean hideReserveButton) {
        this.context = context;
        this.hotelList = hotelList;
        this.reserveButtonClickListener = listener;
        this.hideReserveButton = hideReserveButton; // Determines if the button should be hidden
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

        holder.hotelName.setText(hotel.getName() != null ? hotel.getName() : "Hotel Name Not Available");
        holder.hotelLocation.setText(hotel.getLocation() != null ? hotel.getLocation() : "Location Unknown");
        holder.hotelPrice.setText(hotel.getPrice() != null ? "Price: " + hotel.getPrice() : "Price: N/A");
        holder.hotelRatings.setText(hotel.getRatings() != null ? "⭐ " + hotel.getRatings() : "⭐ N/A");

        if (hotel.getImageUrls() != null && !hotel.getImageUrls().isEmpty()) {
            String firstImageUrl = hotel.getImageUrls().get(0);
            Glide.with(context)
                    .load(firstImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.default_hotel)
                    .error(R.drawable.default_hotel)
                    .into(holder.hotelImage);
        } else {
            holder.hotelImage.setImageResource(R.drawable.default_hotel);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HotelDetailsActivity.class);
            intent.putExtra("hotel", hotel);
            context.startActivity(intent);
        });

        // Hide reserve button if hideReserveButton is true (for MyCurrentBookings)
        if (hideReserveButton) {
            holder.reserveButton.setVisibility(View.GONE);
        } else {
            holder.reserveButton.setVisibility(View.VISIBLE);
            holder.reserveButton.setOnClickListener(v -> {
                if (reserveButtonClickListener != null) {
                    reserveButtonClickListener.onReserveButtonClick(hotel);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView hotelName, hotelLocation, hotelPrice, hotelRatings;
        ImageView hotelImage;
        Button reserveButton;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelLocation = itemView.findViewById(R.id.hotelLocation);
            hotelPrice = itemView.findViewById(R.id.hotelPrice);
            hotelRatings = itemView.findViewById(R.id.hotelRatings);
            hotelImage = itemView.findViewById(R.id.hotelImage);
            reserveButton = itemView.findViewById(R.id.reserveButton);
        }
    }
}
