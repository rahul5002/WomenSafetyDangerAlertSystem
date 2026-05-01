package com.womensafety.alert;

import com.womensafety.model.Alert;
import com.womensafety.model.EmergencyContact;
import com.womensafety.model.User;
import com.womensafety.util.Logger;

import java.util.List;

/**
 * NotificationDispatcher — handles the actual sending of notifications via
 * multiple channels: SMS, Email, Push, and Police Radio.
 * Uses the Strategy pattern for channel-specific dispatch.
 */
public class NotificationDispatcher {

    private final Logger logger;

    public NotificationDispatcher() {
        this.logger = Logger.getInstance();
    }

    /**
     * Sends an SMS notification (simulated).
     */
    public boolean sendSMS(String phone, String message) {
        logger.log("DISPATCH", String.format("[SMS GATEWAY] → %s: %s", phone, message));
        // In production: integrate with Twilio, MSG91, etc.
        return true;
    }

    /**
     * Sends an email notification (simulated).
     */
    public boolean sendEmail(String email, String subject, String body) {
        logger.log("DISPATCH", String.format("[EMAIL GATEWAY] → %s | Subject: %s", email, subject));
        // In production: integrate with JavaMail / SendGrid
        return true;
    }

    /**
     * Sends a push notification (simulated).
     */
    public boolean sendPushNotification(String userId, String title, String body) {
        logger.log("DISPATCH", String.format("[PUSH NOTIFICATION] → User %s | %s: %s",
                userId, title, body));
        // In production: integrate with Firebase Cloud Messaging
        return true;
    }

    /**
     * Sends a police radio dispatch (simulated).
     */
    public boolean sendPoliceDispatch(String stationId, String caseNumber, String message) {
        logger.log("DISPATCH", String.format("[POLICE RADIO] → Station %s | Case# %s | %s",
                stationId, caseNumber, message));
        return true;
    }

    /**
     * Broadcasts to all emergency contacts of a user.
     */
    public int notifyAllContacts(User user, String message) {
        List<EmergencyContact> contacts = user.getEmergencyContacts();
        int successCount = 0;
        for (EmergencyContact contact : contacts) {
            if (contact.isReceivesSmsAlerts()) {
                if (sendSMS(contact.getPhone(), message)) successCount++;
            }
            if (contact.isReceivesEmailAlerts()) {
                sendEmail(contact.getEmail(), "🚨 Emergency Alert for " + user.getName(), message);
            }
        }
        return successCount;
    }
}
