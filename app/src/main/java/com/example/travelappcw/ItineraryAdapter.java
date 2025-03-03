package com.example.travelappcw;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private List<ItineraryItem> itineraryList;
    private CollectionReference itineraryRef;
    private Context context;

    public ItineraryAdapter(List<ItineraryItem> itineraryList, CollectionReference itineraryRef, Context context) {
        this.itineraryList = itineraryList;
        this.itineraryRef = itineraryRef;
        this.context = context;
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

        // Edit itinerary
        holder.editButton.setOnClickListener(v -> editItinerary(item, position));

        // Delete itinerary with crash fix
        holder.deleteButton.setOnClickListener(v -> {
            DocumentReference docRef = itineraryRef.document(item.getId());
            docRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        if (position >= 0 && position < itineraryList.size()) { // Prevent index out of bounds
                            itineraryList.remove(position);

                            // Ensure UI updates on the main thread
                            if (context instanceof android.app.Activity) {
                                ((android.app.Activity) context).runOnUiThread(() -> {
                                    notifyItemRemoved(position);
                                    Toast.makeText(context, "Itinerary deleted", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error deleting itinerary", e);
                        Toast.makeText(context, "Failed to delete itinerary", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    /**
     * Opens a dialog for editing an itinerary.
     */
    private void editItinerary(ItineraryItem item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_itinerary, null);
        builder.setView(view);

        EditText editDay = view.findViewById(R.id.editDay);
        EditText editTime = view.findViewById(R.id.editTime);
        EditText editActivity = view.findViewById(R.id.editActivity);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        editDay.setText(item.getDay());
        editTime.setText(item.getTime());
        editActivity.setText(item.getActivity());

        AlertDialog dialog = builder.create();

        btnUpdate.setOnClickListener(v -> {
            String newDay = editDay.getText().toString().trim();
            String newTime = editTime.getText().toString().trim();
            String newActivity = editActivity.getText().toString().trim();

            if (!newDay.isEmpty() && !newTime.isEmpty() && !newActivity.isEmpty()) {
                item.setDay(newDay);
                item.setTime(newTime);
                item.setActivity(newActivity);

                DocumentReference docRef = itineraryRef.document(item.getId());
                docRef.set(item)
                        .addOnSuccessListener(aVoid -> {
                            itineraryList.set(position, item);
                            notifyItemChanged(position);
                            dialog.dismiss();
                            Toast.makeText(context, "Itinerary updated", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error updating itinerary", e));
            }
        });

        dialog.show();
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
