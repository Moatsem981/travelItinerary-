package com.example.travelappcw;

public class FlightModel {
    private String airline, flightNumber, departure, arrival, duration, departureTime, arrivalTime, baggageAllowance, flightClass;
    private int layovers;
    private String price; // Changed to String to match Firestore data

    // Default constructor (required for Firestore)
    public FlightModel() {}

    // Getters
    public String getAirline() { return airline; }
    public String getFlightNumber() { return flightNumber; }
    public String getDeparture() { return departure; }
    public String getArrival() { return arrival; }
    public String getDuration() { return duration; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getBaggageAllowance() { return baggageAllowance; }
    public String getFlightClass() { return flightClass; }
    public int getLayovers() { return layovers; }
    public String getPrice() { return price; } // Changed to String

    // Setters (required for Firestore deserialization)
    public void setAirline(String airline) { this.airline = airline; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setDeparture(String departure) { this.departure = departure; }
    public void setArrival(String arrival) { this.arrival = arrival; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setBaggageAllowance(String baggageAllowance) { this.baggageAllowance = baggageAllowance; }
    public void setFlightClass(String flightClass) { this.flightClass = flightClass; }
    public void setLayovers(int layovers) { this.layovers = layovers; }
    public void setPrice(String price) { this.price = price; } // Changed to String
}