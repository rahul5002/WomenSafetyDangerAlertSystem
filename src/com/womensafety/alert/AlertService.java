package com.womensafety.alert;

import com.womensafety.model.*;
import com.womensafety.service.PoliceStationService;
import com.womensafety.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * AlertService — central hub for creating, dispatching, and managing all alerts.
 * Implements the Service Layer pattern with delegation to specialized handlers.
 */
public class AlertService {

    private final PoliceStationService policeStationService;
    private final NotificationDispatcher dispatcher;
    private final List<Alert> alertHistory;
    private final Logger logger;

    public AlertService(PoliceStationService policeStationService) {
        this.policeStationService = policeStationService;
        this.dispatcher = new NotificationDispatcher();
        this.alertHistory = new ArrayList<>();
        this.logger = Logger.getInstance();
    }

    /**
     * Triggers a full SOS emergency alert for a user in immediate danger.
     */
    public SOSAlert triggerSOS(User user, String triggerMethod) {
        logger.log("CRITICAL", "SOS triggered by: " + user.getName());

        Location userLocation = user.getCurrentLocation();
        if (userLocation == null) {
            userLocation = new Location(28.6139, 77.2090, "Last Known Location", "Delhi", "Delhi", "Delhi");
        }

        PoliceStation nearestStation = policeStationService.findNearestStation(userLocation);

        SOSAlert alert = new SOSAlert(
                user, userLocation,
                user.getEmergencyContacts(),
                nearestStation,
                triggerMethod
        );

        user.setStatus(User.UserStatus.DANGER);
        alert.dispatch();
        alertHistory.add(alert);

        logger.log("INFO", "SOS Alert created: " + alert.getAlertId());
        return alert;
    }

    /**
     * Creates a danger zone warning for a user approaching a high-crime area.
     */
    public DangerZoneAlert createDangerZoneAlert(User user, CrimeReport crime, double distanceKm) {
        Location userLocation = user.getCurrentLocation();
        if (userLocation == null) return null;

        DangerZoneAlert alert = new DangerZoneAlert(user, userLocation, crime, distanceKm, 2.0);
        alert.dispatch();
        alertHistory.add(alert);

        logger.log("WARNING", "Danger Zone Alert for " + user.getName() +
                " | Crime: " + crime.getCrimeType().getDisplayName());
        return alert;
    }

    /**
     * Sends a direct notification to police station about an incident.
     */
    public PoliceAlert notifyPolice(User user, String incidentDescription,
                                   PoliceAlert.PoliceAlertType type, boolean anonymous) {
        Location userLocation = user.getCurrentLocation();
        if (userLocation == null) {
            userLocation = new Location(28.6139, 77.2090, "Unknown", "Delhi", "Delhi", "Delhi");
        }

        PoliceStation nearestStation = policeStationService.findNearestStation(userLocation);
        if (nearestStation == null) {
            logger.log("ERROR", "No police station found near user location.");
            return null;
        }

        PoliceAlert alert = new PoliceAlert(user, userLocation, nearestStation,
                incidentDescription, type, anonymous);

        alert.dispatch();
        alertHistory.add(alert);

        logger.log("CRITICAL", "Police notified. Case#: " + alert.getCaseNumber());
        return alert;
    }

    /**
     * Sends crime zone notifications to all users within the warning radius.
     */
    public int broadcastDangerZoneToNearbyUsers(List<User> allUsers, CrimeReport report,
                                                 double warningRadiusKm) {
        int notified = 0;
        for (User user : allUsers) {
            if (user.getCurrentLocation() == null) continue;
            double distance = user.getCurrentLocation().distanceTo(report.getLocation());
            if (distance <= warningRadiusKm) {
                DangerZoneAlert alert = createDangerZoneAlert(user, report, distance);
                if (alert != null) notified++;
            }
        }
        logger.log("INFO", "Broadcast danger zone alert to " + notified + " users.");
        return notified;
    }

    public List<Alert> getAlertHistory() { return new ArrayList<>(alertHistory); }

    public List<Alert> getAlertsByUser(String userId) {
        List<Alert> result = new ArrayList<>();
        for (Alert a : alertHistory) {
            if (a.getSender() != null && a.getSender().getUserId().equals(userId)) {
                result.add(a);
            }
        }
        return result;
    }
}
