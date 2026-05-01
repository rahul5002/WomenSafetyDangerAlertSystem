package com.womensafety.model;

/**
 * Represents a police station in the system.
 * Police stations receive emergency alerts and handle crime reports.
 */
public class PoliceStation {

    private String stationId;
    private String stationName;
    private String stationCode;
    private Location location;
    private String phone;
    private String email;
    private String jurisdiction;
    private boolean isActive;
    private int totalCasesHandled;
    private int activeCases;

    public PoliceStation(String stationId, String stationName, String stationCode,
                         Location location, String phone, String email, String jurisdiction) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationCode = stationCode;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.jurisdiction = jurisdiction;
        this.isActive = true;
        this.totalCasesHandled = 0;
        this.activeCases = 0;
    }

    // Getters
    public String getStationId() { return stationId; }
    public String getStationName() { return stationName; }
    public String getStationCode() { return stationCode; }
    public Location getLocation() { return location; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getJurisdiction() { return jurisdiction; }
    public boolean isActive() { return isActive; }
    public int getTotalCasesHandled() { return totalCasesHandled; }
    public int getActiveCases() { return activeCases; }

    // Setters
    public void setActive(boolean active) { this.isActive = active; }

    public void incrementActiveCases() { this.activeCases++; }
    public void resolveCase() {
        if (activeCases > 0) activeCases--;
        totalCasesHandled++;
    }

    public double distanceFrom(Location userLocation) {
        return this.location.distanceTo(userLocation);
    }

    @Override
    public String toString() {
        return String.format("PoliceStation[id=%s, name='%s', jurisdiction='%s', phone=%s]",
                stationId, stationName, jurisdiction, phone);
    }
}
