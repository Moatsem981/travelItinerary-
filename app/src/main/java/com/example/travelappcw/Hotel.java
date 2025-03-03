package com.example.travelappcw;

public class Hotel {
    private String name;
    private String location;
    private String imageUrl; // Firestore stores image links

    public Hotel() {
        // Default constructor required for Firestore
    }

    public Hotel(String name, String location, String imageUrl) {
        this.name = name;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
