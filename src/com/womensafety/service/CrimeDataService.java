package com.womensafety.service;

import com.womensafety.database.DatabaseManager;
import com.womensafety.model.*;
import com.womensafety.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * CrimeDataService — handles crime report submission, querying, and danger zone computation.
 */
public class CrimeDataService {

    private final DatabaseManager db;
    private final Logger logger;
    private static final double DANGER_ZONE_RADIUS_KM = 2.0;
    private static final double WARNING_RADIUS_KM = 5.0;

    public CrimeDataService(DatabaseManager db) {
        this.db = db;
        this.logger = Logger.getInstance();
    }

    public CrimeReport submitReport(CrimeReport.CrimeType type, Location location,
                                    CrimeReport.Severity severity, String description,
                                    String reportedByUserId) {
        CrimeReport report = new CrimeReport(type, location, severity, description, reportedByUserId);
        db.saveCrimeReport(report);
        updateDangerZones(report);
        logger.log("INFO", "Crime report submitted: " + report.getReportId() +
                " | " + type.getDisplayName() + " at " + location.getCity());
        return report;
    }

    private void updateDangerZones(CrimeReport report) {
        List<DangerZone> zones = db.getAllDangerZones();
        for (DangerZone zone : zones) {
            if (zone.contains(report.getLocation())) {
                zone.addCrimeReport(report);
                db.saveDangerZone(zone);
                return;
            }
        }
        // Create a new danger zone centred on this report
        String zoneId = "DZ-" + (db.getAllDangerZones().size() + 1);
        String zoneName = report.getLocation().getCity() + " - Zone " + (db.getAllDangerZones().size() + 1);
        DangerZone newZone = new DangerZone(zoneId, zoneName, report.getLocation(), DANGER_ZONE_RADIUS_KM);
        newZone.addCrimeReport(report);
        db.saveDangerZone(newZone);
        logger.log("INFO", "New danger zone created: " + zoneName);
    }

    /**
     * Returns all crime reports within a given radius of a location.
     */
    public List<CrimeReport> getNearbyCrimes(Location location, double radiusKm) {
        List<CrimeReport> result = new ArrayList<>();
        for (CrimeReport r : db.getAllCrimeReports()) {
            if (r.getLocation().distanceTo(location) <= radiusKm) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Returns active danger zones that contain or overlap with the user's location.
     */
    public List<DangerZone> getDangerZonesNear(Location location, double radiusKm) {
        List<DangerZone> result = new ArrayList<>();
        for (DangerZone zone : db.getActiveDangerZones()) {
            double dist = zone.getCenterLocation().distanceTo(location);
            if (dist <= radiusKm + zone.getRadiusKm()) {
                result.add(zone);
            }
        }
        return result;
    }

    public List<CrimeReport> getAllReports() { return db.getAllCrimeReports(); }

    public List<DangerZone> getAllDangerZones() { return db.getAllDangerZones(); }

    public List<DangerZone> getAllActiveDangerZones() { return db.getActiveDangerZones(); }

    public double getWarningRadiusKm() { return WARNING_RADIUS_KM; }

    public DatabaseManager getDb() { return db; }

    /** Alias for submitReport — used by ApplicationMenu */
    public CrimeReport createReport(CrimeReport.CrimeType type, Location location,
                                    CrimeReport.Severity severity, String description,
                                    String userId) {
        return submitReport(type, location, severity, description, userId);
    }

    /** Alias for getNearbyCrimes — used by ApplicationMenu */
    public List<CrimeReport> getReportsNearLocation(Location location, double radiusKm) {
        return getNearbyCrimes(location, radiusKm);
    }

    /** Returns crime reports for a specific city */
    public List<CrimeReport> getReportsByCity(String city) {
        return db.getCrimeReportsByCity(city);
    }

    /**
     * Seeds realistic sample crime data across Delhi for demonstration.
     */
    public void seedInitialData() {
        // Central Delhi incidents
        submitReport(CrimeReport.CrimeType.HARASSMENT,
                new Location(28.6289, 77.2065, "Rajiv Chowk Metro", "New Delhi", "Central Delhi", "Delhi"),
                CrimeReport.Severity.MEDIUM, "Harassment reported near metro gate 4", null);

        submitReport(CrimeReport.CrimeType.EVE_TEASING,
                new Location(28.6353, 77.2249, "Chandni Chowk Market", "New Delhi", "North Delhi", "Delhi"),
                CrimeReport.Severity.MEDIUM, "Eve teasing in crowded market", null);

        submitReport(CrimeReport.CrimeType.STALKING,
                new Location(28.5562, 77.1000, "Dwarka Sector 10", "New Delhi", "South-West Delhi", "Delhi"),
                CrimeReport.Severity.HIGH, "Persistent stalking near residential area", null);

        submitReport(CrimeReport.CrimeType.THEFT,
                new Location(28.7041, 77.1025, "Pitampura", "New Delhi", "North-West Delhi", "Delhi"),
                CrimeReport.Severity.LOW, "Bag snatching reported", null);

        submitReport(CrimeReport.CrimeType.ASSAULT,
                new Location(28.5355, 77.3910, "Noida Sector 18", "Noida", "Gautam Buddh Nagar", "UP"),
                CrimeReport.Severity.CRITICAL, "Physical assault on woman near mall", null);

        submitReport(CrimeReport.CrimeType.HARASSMENT,
                new Location(28.4595, 77.0266, "Gurugram Sector 29", "Gurugram", "Gurugram", "Haryana"),
                CrimeReport.Severity.HIGH, "Harassment in office parking late night", null);

        submitReport(CrimeReport.CrimeType.MOLESTATION,
                new Location(28.6517, 77.2219, "Karol Bagh Bus Stop", "New Delhi", "West Delhi", "Delhi"),
                CrimeReport.Severity.HIGH, "Molestation at public bus stop", null);

        submitReport(CrimeReport.CrimeType.CYBER_CRIME,
                new Location(28.6139, 77.2090, "Connaught Place", "New Delhi", "Central Delhi", "Delhi"),
                CrimeReport.Severity.MEDIUM, "Online fraud targeting women", null);

        logger.log("INFO", "Sample crime data seeded: " + db.getAllCrimeReports().size() + " reports.");
    }
}
