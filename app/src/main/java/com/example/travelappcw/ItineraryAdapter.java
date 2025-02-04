package com.example.travelappcw;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private List<ItineraryItem> itineraryList;
    private FirebaseFirestore db;
    private String loggedInUsername;

    public ItineraryAdapter(List<ItineraryItem> itineraryList, FirebaseFirestore db, String loggedInUsername) {
        this.itineraryList = itineraryList;
        this.db = db;
        this.loggedInUsername = loggedInUsername;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_itinerary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItineraryItem item = itineraryList.get(position);
        holder.dayTextView.setText(item.getDay());
        holder.timeTextView.setText(item.getTime());
        holder.activityTextView.setText(item.getActivity());

        holder.editButton.setOnClickListener(v -> editItinerary(holder.itemView.getContext(), item));
        holder.deleteButton.setOnClickListener(v -> {
            db.collection("Users").document(loggedInUsername)
                    .collection("itineraries").document(item.getId()).delete();
            itineraryList.remove(position);
            notifyDataSetChanged();
        });
    }

    private void editItinerary(Context context, ItineraryItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_itinerary, null);
        builder.setView(view);

        EditText editTime = view.findViewById(R.id.editTime);
        EditText editActivity = view.findViewById(R.id.editActivity);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        editTime.setText(item.getTime());
        editActivity.setText(item.getActivity());

        btnUpdate.setOnClickListener(v -> {
            item.setTime(editTime.getText().toString());
            item.setActivity(editActivity.getText().toString());
            db.collection("Users").document(loggedInUsername)
                    .collection("itineraries").document(item.getId()).set(item);
            notifyDataSetChanged();
        });

        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView, timeTextView, activityTextView;
        Button editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.textViewDay);
            timeTextView = itemView.findViewById(R.id.textViewTime);
            activityTextView = itemView.findViewById(R.id.textViewActivity);
            editButton = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.btnDelete);
        }
    }
}
