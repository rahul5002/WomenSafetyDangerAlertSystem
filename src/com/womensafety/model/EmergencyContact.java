package com.womensafety.model;

import java.util.UUID;

/**
 * Represents an emergency contact for a user.
 * Contacts receive SOS alerts and location updates.
 */
public class EmergencyContact {

    private String contactId;
    private String name;
    private String phone;
    private String email;
    private String relationship;
    private boolean receivesSmsAlerts;
    private boolean receivesEmailAlerts;
    private boolean isNotified;          // tracks if notified in current session

    public EmergencyContact(String name, String phone, String email, String relationship) {
        this.contactId = "EC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.relationship = relationship;
        this.receivesSmsAlerts = true;
        this.receivesEmailAlerts = true;
        this.isNotified = false;
    }

    // Getters
    public String getContactId() { return contactId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getRelationship() { return relationship; }
    public boolean isReceivesSmsAlerts() { return receivesSmsAlerts; }
    public boolean isReceivesEmailAlerts() { return receivesEmailAlerts; }
    public boolean isNotified() { return isNotified; }

    // Setters
    public void setReceivesSmsAlerts(boolean value) { this.receivesSmsAlerts = value; }
    public void setReceivesEmailAlerts(boolean value) { this.receivesEmailAlerts = value; }
    public void setNotified(boolean notified) { this.isNotified = notified; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("EmergencyContact[id=%s, name=%s, phone=%s, relation=%s]",
                contactId, name, phone, relationship);
    }
}
