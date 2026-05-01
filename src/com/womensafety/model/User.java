package com.womensafety.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered user (woman) in the safety system.
 * Stores personal details, emergency contacts, and current location.
 */
public class User {

    private String userId;
    private String name;
    private String phone;
    private String email;
    private String passwordHash;
    private Location currentLocation;
    private List<EmergencyContact> emergencyContacts;
    private UserStatus status;
    private LocalDateTime registeredAt;
    private LocalDateTime lastActive;
    private boolean isVerified;

    public enum UserStatus {
        SAFE, ALERT, DANGER, UNKNOWN
    }

    public User(String userId, String name, String phone, String email, String passwordHash) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.emergencyContacts = new ArrayList<>();
        this.status = UserStatus.SAFE;
        this.registeredAt = LocalDateTime.now();
        this.lastActive = LocalDateTime.now();
        this.isVerified = false;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Location getCurrentLocation() { return currentLocation; }
    public List<EmergencyContact> getEmergencyContacts() { return new ArrayList<>(emergencyContacts); }
    public UserStatus getStatus() { return status; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public LocalDateTime getLastActive() { return lastActive; }
    public boolean isVerified() { return isVerified; }

    // Setters
    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
        this.lastActive = LocalDateTime.now();
    }
    public void setStatus(UserStatus status) { this.status = status; }
    public void setVerified(boolean verified) { this.isVerified = verified; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    public void addEmergencyContact(EmergencyContact contact) {
        if (emergencyContacts.size() < 5) {
            emergencyContacts.add(contact);
        } else {
            throw new IllegalStateException("Maximum 5 emergency contacts allowed.");
        }
    }

    public void removeEmergencyContact(String contactId) {
        emergencyContacts.removeIf(c -> c.getContactId().equals(contactId));
    }

    public void updateLastActive() {
        this.lastActive = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("User[id=%s, name=%s, phone=%s, status=%s]",
                userId, name, phone, status);
    }
}
