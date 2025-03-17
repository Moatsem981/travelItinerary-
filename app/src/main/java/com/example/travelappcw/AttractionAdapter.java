package com.example.travelappcw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    private List<Attraction> attractionList;
    private OnViewOnMapClickListener listener;

    public interface OnViewOnMapClickListener {
        void onViewOnMapClick(double latitude, double longitude, String name);
    }

    public AttractionAdapter(List<Attraction> attractionList, OnViewOnMapClickListener listener) {
        this.attractionList = attractionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local_attraction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attraction attraction = attractionList.get(position);
        holder.attractionName.setText(attraction.getName());
        holder.attractionLocation.setText(attraction.getLocation());
        holder.attractionDescription.setText(attraction.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(attraction.getImageUrl())
                .into(holder.attractionImage);

        holder.viewOnMapButton.setOnClickListener(v ->
                listener.onViewOnMapClick(attraction.getLatitude(), attraction.getLongitude(), attraction.getName()));
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView attractionImage;
        TextView attractionName, attractionLocation, attractionDescription;
        Button viewOnMapButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            attractionImage = itemView.findViewById(R.id.attractionImage);
            attractionName = itemView.findViewById(R.id.attractionName);
            attractionLocation = itemView.findViewById(R.id.attractionLocation);
            attractionDescription = itemView.findViewById(R.id.attractionDescription);
            viewOnMapButton = itemView.findViewById(R.id.viewOnMapButton);
        }
    }
}
