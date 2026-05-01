package com.womensafety.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract base class for all alert types in the Women Safety System.
 * Follows OOP inheritance principles with concrete subclasses for different alert types.
 */
public abstract class Alert {

    protected String alertId;
    protected AlertType alertType;
    protected User sender;
    protected Location alertLocation;
    protected String message;
    protected AlertStatus status;
    protected LocalDateTime createdAt;
    protected LocalDateTime acknowledgedAt;
    protected AlertPriority priority;

    public enum AlertType {
        SOS_EMERGENCY("SOS Emergency"),
        DANGER_ZONE_WARNING("Danger Zone Warning"),
        CRIME_REPORT_NOTIFICATION("Crime Report Notification"),
        POLICE_NOTIFICATION("Police Notification"),
        SAFETY_CHECK("Safety Check"),
        ROUTE_ALERT("Route Alert");

        private final String displayName;
        AlertType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum AlertStatus {
        CREATED, SENT, DELIVERED, ACKNOWLEDGED, RESOLVED, FAILED
    }

    public enum AlertPriority {
        LOW(1), MEDIUM(2), HIGH(3), CRITICAL(4);
        private final int level;
        AlertPriority(int level) { this.level = level; }
        public int getLevel() { return level; }
    }

    protected Alert(AlertType alertType, User sender, Location location,
                    String message, AlertPriority priority) {
        this.alertId = "ALT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.alertType = alertType;
        this.sender = sender;
        this.alertLocation = location;
        this.message = message;
        this.status = AlertStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.priority = priority;
    }

    // Getters
    public String getAlertId() { return alertId; }
    public AlertType getAlertType() { return alertType; }
    public User getSender() { return sender; }
    public Location getAlertLocation() { return alertLocation; }
    public String getMessage() { return message; }
    public AlertStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getAcknowledgedAt() { return acknowledgedAt; }
    public AlertPriority getPriority() { return priority; }

    public void setStatus(AlertStatus status) { this.status = status; }
    public void acknowledge() {
        this.status = AlertStatus.ACKNOWLEDGED;
        this.acknowledgedAt = LocalDateTime.now();
    }

    /**
     * Abstract method — each subclass defines how it is dispatched.
     */
    public abstract void dispatch();

    /**
     * Abstract method — each subclass defines its notification payload.
     */
    public abstract String buildNotificationPayload();

    @Override
    public String toString() {
        return String.format("Alert[id=%s, type=%s, priority=%s, status=%s]",
                alertId, alertType.getDisplayName(), priority, status);
    }
}
