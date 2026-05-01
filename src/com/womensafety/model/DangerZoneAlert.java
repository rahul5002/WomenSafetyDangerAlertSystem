package com.womensafety.model;

import com.womensafety.util.Logger;

/**
 * DangerZoneAlert — notifies a user when she enters or is near a zone
 * with reported crime incidents within a configurable radius.
 */
public class DangerZoneAlert extends Alert {

    private CrimeReport relatedCrimeReport;
    private double distanceToIncidentKm;
    private double warningRadiusKm;
    private String safeRouteAdvice;

    public DangerZoneAlert(User user, Location userLocation, CrimeReport crime,
                           double distanceKm, double warningRadiusKm) {
        super(AlertType.DANGER_ZONE_WARNING, user, userLocation,
                buildWarningMessage(user, crime, distanceKm), resolvePriority(crime));
        this.relatedCrimeReport = crime;
        this.distanceToIncidentKm = distanceKm;
        this.warningRadiusKm = warningRadiusKm;
        this.safeRouteAdvice = generateSafeRouteAdvice(crime);
    }

    private static String buildWarningMessage(User user, CrimeReport crime, double distance) {
        return String.format(
                "⚠️ WARNING %s! A %s was reported %.2f km from your location at %s. " +
                "Severity: %s. Please be cautious and avoid this area.",
                user.getName(), crime.getCrimeType().getDisplayName(),
                distance, crime.getLocation().getCity(),
                crime.getSeverity().name()
        );
    }

    private static AlertPriority resolvePriority(CrimeReport crime) {
        switch (crime.getSeverity()) {
            case CRITICAL: return AlertPriority.CRITICAL;
            case HIGH: return AlertPriority.HIGH;
            case MEDIUM: return AlertPriority.MEDIUM;
            default: return AlertPriority.LOW;
        }
    }

    private String generateSafeRouteAdvice(CrimeReport crime) {
        return String.format(
                "Avoid %s area. Stay in well-lit, crowded places. " +
                "Share your location with a trusted contact. " +
                "Call 112 or use SOS if you feel threatened.",
                crime.getLocation().getCity()
        );
    }

    @Override
    public void dispatch() {
        Logger logger = Logger.getInstance();
        logger.log("WARNING", "Dispatching Danger Zone Alert: " + alertId);

        logger.log("WARNING", String.format(
                "[DANGER ZONE ALERT] → User: %s | %s",
                sender.getName(), message));

        logger.log("INFO", "Safe Route Advice: " + safeRouteAdvice);
        this.status = AlertStatus.SENT;
    }

    @Override
    public String buildNotificationPayload() {
        return String.format(
                "{\n  \"alertId\": \"%s\",\n  \"type\": \"DANGER_ZONE_WARNING\",\n" +
                "  \"priority\": \"%s\",\n  \"user\": \"%s\",\n" +
                "  \"crimeType\": \"%s\",\n  \"severity\": \"%s\",\n" +
                "  \"distanceKm\": %.2f,\n  \"warningRadiusKm\": %.2f,\n" +
                "  \"crimeLocation\": \"%s\",\n  \"safeRouteAdvice\": \"%s\"\n}",
                alertId, priority, sender.getName(),
                relatedCrimeReport.getCrimeType().getDisplayName(),
                relatedCrimeReport.getSeverity().name(),
                distanceToIncidentKm, warningRadiusKm,
                relatedCrimeReport.getLocation().toShortString(),
                safeRouteAdvice
        );
    }

    // Getters
    public CrimeReport getRelatedCrimeReport() { return relatedCrimeReport; }
    public double getDistanceToIncidentKm() { return distanceToIncidentKm; }
    public String getSafeRouteAdvice() { return safeRouteAdvice; }
}
