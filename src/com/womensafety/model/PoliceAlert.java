package com.womensafety.model;

import com.womensafety.util.Logger;

/**
 * PoliceAlert — sends a formal emergency alert directly to the nearest police station.
 * Includes full user details, location, and incident description.
 */
public class PoliceAlert extends Alert {

    private PoliceStation targetStation;
    private String incidentDescription;
    private boolean isAnonymous;
    private String caseNumber;
    private PoliceAlertType policeAlertType;

    public enum PoliceAlertType {
        EMERGENCY_SOS, CRIME_REPORT, FOLLOW_UP, WELFARE_CHECK
    }

    public PoliceAlert(User sender, Location location, PoliceStation station,
                       String incidentDescription, PoliceAlertType type, boolean isAnonymous) {
        super(AlertType.POLICE_NOTIFICATION, sender, location,
                buildPoliceMessage(sender, location, incidentDescription, isAnonymous),
                AlertPriority.CRITICAL);
        this.targetStation = station;
        this.incidentDescription = incidentDescription;
        this.isAnonymous = isAnonymous;
        this.policeAlertType = type;
        this.caseNumber = generateCaseNumber();
    }

    private static String buildPoliceMessage(User sender, Location location,
                                             String incident, boolean anonymous) {
        String reporterInfo = anonymous ? "Anonymous Caller" :
                String.format("%s (Ph: %s)", sender.getName(), sender.getPhone());
        return String.format(
                "🚔 EMERGENCY POLICE ALERT | Reporter: %s | Location: %s | Incident: %s",
                reporterInfo, location.toShortString(), incident
        );
    }

    private String generateCaseNumber() {
        return String.format("CASE-%d-%s",
                System.currentTimeMillis() % 100000,
                alertLocation.getCity().toUpperCase().replaceAll("\\s", "").substring(0,
                        Math.min(3, alertLocation.getCity().length())));
    }

    @Override
    public void dispatch() {
        Logger logger = Logger.getInstance();
        logger.log("CRITICAL", "Dispatching Police Alert: " + alertId);

        logger.log("CRITICAL", String.format(
                "[POLICE DISPATCH] → %s | %s | %s | Case# %s",
                targetStation.getStationName(), targetStation.getPhone(),
                message, caseNumber));

        targetStation.incrementActiveCases();
        this.status = AlertStatus.SENT;

        logger.log("INFO", String.format(
                "Police Alert sent. Case Number: %s | Station: %s",
                caseNumber, targetStation.getStationName()));
    }

    @Override
    public String buildNotificationPayload() {
        return String.format(
                "{\n  \"caseNumber\": \"%s\",\n  \"alertId\": \"%s\",\n" +
                "  \"type\": \"POLICE_NOTIFICATION\",\n  \"priority\": \"CRITICAL\",\n" +
                "  \"station\": \"%s\",\n  \"stationPhone\": \"%s\",\n" +
                "  \"reporter\": \"%s\",\n  \"reporterPhone\": \"%s\",\n" +
                "  \"latitude\": %.4f,\n  \"longitude\": %.4f,\n" +
                "  \"incident\": \"%s\",\n  \"anonymous\": %b,\n" +
                "  \"timestamp\": \"%s\"\n}",
                caseNumber, alertId, targetStation.getStationName(),
                targetStation.getPhone(),
                isAnonymous ? "Anonymous" : sender.getName(),
                isAnonymous ? "Hidden" : sender.getPhone(),
                alertLocation.getLatitude(), alertLocation.getLongitude(),
                incidentDescription, isAnonymous, createdAt
        );
    }

    // Getters
    public PoliceStation getTargetStation() { return targetStation; }
    public String getCaseNumber() { return caseNumber; }
    public boolean isAnonymous() { return isAnonymous; }
    public PoliceAlertType getPoliceAlertType() { return policeAlertType; }
}
