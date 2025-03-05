package com.example.travelappcw;

public class ItineraryItem {
    private String id;  // Firestore document ID
    private String day;
    private String time;
    private String activity;

    public ItineraryItem() {
    }

    public ItineraryItem(String day, String time, String activity) {
        this.day = day;
        this.time = time;
        this.activity = activity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
