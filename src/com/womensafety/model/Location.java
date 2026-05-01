package com.womensafety.model;

import java.time.LocalDateTime;

/**
 * Represents a geographic location with coordinates and metadata.
 * Used for tracking user position and crime incident locations.
 */
public class Location {

    private double latitude;
    private double longitude;
    private String address;
    private String city;
    private String district;
    private String state;
    private LocalDateTime timestamp;
    private double accuracyMeters;

    public Location(double latitude, double longitude, String address,
                    String city, String district, String state) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.district = district;
        this.state = state;
        this.timestamp = LocalDateTime.now();
        this.accuracyMeters = 10.0;
    }

    public Location(double latitude, double longitude) {
        this(latitude, longitude, "Unknown", "Unknown", "Unknown", "Unknown");
    }

    // Getters
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getDistrict() { return district; }
    public String getState() { return state; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getAccuracyMeters() { return accuracyMeters; }

    // Setters
    public void setAddress(String address) { this.address = address; }
    public void setAccuracyMeters(double accuracy) { this.accuracyMeters = accuracy; }

    /**
     * Calculates the distance in kilometers between this location and another
     * using the Haversine formula.
     */
    public double distanceTo(Location other) {
        final int EARTH_RADIUS_KM = 6371;

        double latDiff = Math.toRadians(other.latitude - this.latitude);
        double lonDiff = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(this.latitude))
                * Math.cos(Math.toRadians(other.latitude))
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public String toString() {
        return String.format("Location[lat=%.4f, lon=%.4f, address='%s, %s']",
                latitude, longitude, address, city);
    }

    public String toShortString() {
        return String.format("%.4f, %.4f (%s)", latitude, longitude, city);
    }
}
