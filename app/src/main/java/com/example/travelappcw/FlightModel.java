package com.example.travelappcw;

import com.google.firebase.firestore.PropertyName;

public class FlightModel {
    private String airline;
    private String flightNumber;
    private String departure;
    private String arrival;
    private String duration;
    private String departureTime;
    private String arrivalTime;
    private String baggageAllowance;

    @PropertyName("class")
    private String flightClass;

    private int layovers;
    private String price;

    public FlightModel() {}

    public FlightModel(String airline, String flightNumber, String departure, String arrival,
                       String duration, String departureTime, String arrivalTime,
                       String baggageAllowance, String flightClass, int layovers, String price) {
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.duration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.baggageAllowance = baggageAllowance;
        this.flightClass = flightClass;
        this.layovers = layovers;
        this.price = price;
    }

    // Getters
    public String getAirline() { return airline; }
    public String getFlightNumber() { return flightNumber; }
    public String getDeparture() { return departure; }
    public String getArrival() { return arrival; }
    public String getDuration() { return duration; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getBaggageAllowance() { return baggageAllowance; }

    @PropertyName("class") // Map Firestore field "class" to flightClass
    public String getFlightClass() { return flightClass; }

    public int getLayovers() { return layovers; }
    public String getPrice() { return price; }

    // Setters
    public void setAirline(String airline) { this.airline = airline; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setDeparture(String departure) { this.departure = departure; }
    public void setArrival(String arrival) { this.arrival = arrival; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setBaggageAllowance(String baggageAllowance) { this.baggageAllowance = baggageAllowance; }

    @PropertyName("class")
    public void setFlightClass(String flightClass) { this.flightClass = flightClass; }

    public void setLayovers(int layovers) { this.layovers = layovers; }
    public void setPrice(String price) { this.price = price; }


    @Override
    public String toString() {
        return "FlightModel{" +
                "airline='" + airline + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", duration='" + duration + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", baggageAllowance='" + baggageAllowance + '\'' +
                ", flightClass='" + flightClass + '\'' +
                ", layovers=" + layovers +
                ", price='" + price + '\'' +
                '}';
    }
}