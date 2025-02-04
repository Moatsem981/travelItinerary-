package com.example.travelappcw;

public class ItineraryItem {
    private String id;  // Firestore document ID
    private String day;
    private String time;
    private String activity;

    // 🔹 Required empty constructor for Firestore
    public ItineraryItem() {
    }

    // 🔹 Constructor with parameters
    public ItineraryItem(String day, String time, String activity) {
        this.day = day;
        this.time = time;
        this.activity = activity;
    }

    // 🔹 Getter and Setter for 'id' (Firestore document ID)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // 🔹 Getter and Setter for 'day'
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    // 🔹 Getter and Setter for 'time'
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // 🔹 Getter and Setter for 'activity'
    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
