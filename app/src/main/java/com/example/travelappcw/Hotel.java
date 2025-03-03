package com.example.travelappcw;

public class Hotel {
    private String name;
    private String location;
    private String price;
    private String ratings;
    private String imageUrl; // Firestore stores image links

    public Hotel() {
        // Default constructor required for Firestore
    }

    public Hotel(String name, String location, String price, String ratings, String imageUrl) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.ratings = ratings;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getRatings() {
        return ratings;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
