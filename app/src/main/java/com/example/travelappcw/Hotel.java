package com.example.travelappcw;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Hotel implements Parcelable {
    private String name;
    private String location;
    private String price;
    private String ratings;
    private String description;
    private List<String> amenities;
    private List<String> imageUrls;

    public Hotel() {
    }

    public Hotel(String name, String location, String price, String ratings, String description, List<String> amenities, List<String> imageUrls) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.ratings = ratings;
        this.description = description;
        this.amenities = amenities;
        this.imageUrls = imageUrls;
    }

    protected Hotel(Parcel in) {
        name = in.readString();
        location = in.readString();
        price = in.readString();
        ratings = in.readString();
        description = in.readString();
        amenities = in.createStringArrayList();
        imageUrls = in.createStringArrayList();
    }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(price);
        dest.writeString(ratings);
        dest.writeString(description);
        dest.writeStringList(amenities);
        dest.writeStringList(imageUrls);
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

    public String getDescription() {
        return description;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
