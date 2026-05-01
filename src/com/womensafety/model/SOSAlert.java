package com.womensafety.model;

import com.womensafety.util.Logger;
import java.util.List;

/**
 * SOS Emergency Alert — highest priority alert sent when a user is in immediate danger.
 * Notifies emergency contacts AND nearest police station.
 */
public class SOSAlert extends Alert {

    private List<EmergencyContact> contactsNotified;
    private PoliceStation nearestStation;
    private String triggerMethod;   // BUTTON, VOICE, SHAKE, AUTO
    private boolean policeNotified;
    private boolean contactsAlerted;

    public SOSAlert(User sender, Location location, List<EmergencyContact> contacts,
                    PoliceStation nearestStation, String triggerMethod) {
        super(AlertType.SOS_EMERGENCY, sender, location,
                buildSOSMessage(sender, location), AlertPriority.CRITICAL);
        this.contactsNotified = contacts;
        this.nearestStation = nearestStation;
        this.triggerMethod = triggerMethod;
        this.policeNotified = false;
        this.contactsAlerted = false;
    }

    private static String buildSOSMessage(User sender, Location location) {
        return String.format(
                "🚨 SOS EMERGENCY! %s needs immediate help at %s. Please respond NOW!",
                sender.getName(), location.toShortString()
        );
    }

    @Override
    public void dispatch() {
        Logger logger = Logger.getInstance();
        logger.log("CRITICAL", "Dispatching SOS Alert: " + alertId);

        // Notify emergency contacts
        notifyEmergencyContacts();

        // Notify nearest police station
        notifyPolice();

        this.status = AlertStatus.SENT;
        logger.log("INFO", "SOS Alert dispatched successfully: " + alertId);
    }

    private void notifyEmergencyContacts() {
        Logger logger = Logger.getInstance();
        for (EmergencyContact contact : contactsNotified) {
            if (contact.isReceivesSmsAlerts()) {
                logger.log("ALERT", String.format(
                        "[SMS] → %s (%s): %s | Location: %s",
                        contact.getName(), contact.getPhone(),
                        message, alertLocation.toShortString()));
                contact.setNotified(true);
            }
            if (contact.isReceivesEmailAlerts()) {
                logger.log("ALERT", String.format(
                        "[EMAIL] → %s (%s): SOS from %s",
                        contact.getName(), contact.getEmail(), sender.getName()));
            }
        }
        this.contactsAlerted = true;
    }

    private void notifyPolice() {
        Logger logger = Logger.getInstance();
        if (nearestStation != null) {
            logger.log("CRITICAL", String.format(
                    "[POLICE DISPATCH] → %s | Phone: %s | Incident: SOS from %s at %s",
                    nearestStation.getStationName(), nearestStation.getPhone(),
                    sender.getName(), alertLocation.toShortString()));
            nearestStation.incrementActiveCases();
            this.policeNotified = true;
        }
    }

    @Override
    public String buildNotificationPayload() {
        return String.format(
                "{\n  \"alertId\": \"%s\",\n  \"type\": \"SOS_EMERGENCY\",\n" +
                "  \"priority\": \"CRITICAL\",\n  \"user\": \"%s\",\n" +
                "  \"phone\": \"%s\",\n  \"latitude\": %.4f,\n" +
                "  \"longitude\": %.4f,\n  \"address\": \"%s\",\n" +
                "  \"message\": \"%s\",\n  \"triggerMethod\": \"%s\",\n" +
                "  \"timestamp\": \"%s\"\n}",
                alertId, sender.getName(), sender.getPhone(),
                alertLocation.getLatitude(), alertLocation.getLongitude(),
                alertLocation.getAddress(), message, triggerMethod, createdAt
        );
    }

    // Getters
    public boolean isPoliceNotified() { return policeNotified; }
    public boolean isContactsAlerted() { return contactsAlerted; }
    public PoliceStation getNearestStation() { return nearestStation; }
    public String getTriggerMethod() { return triggerMethod; }
    public List<EmergencyContact> getContactsNotified() { return contactsNotified; }
}
