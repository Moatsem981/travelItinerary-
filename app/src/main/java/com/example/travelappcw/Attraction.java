package com.example.travelappcw;

public class Attraction {
    private String name, description, location, imageUrl;
    private double latitude, longitude;

    public Attraction() {}  // Needed for Firestore

    public Attraction(String name, String description, String location, double latitude, double longitude, String imageUrl) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getImageUrl() { return imageUrl; }
}
