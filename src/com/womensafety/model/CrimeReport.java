package com.womensafety.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a reported crime incident related to women's safety.
 * Contains crime type, location, severity, and report status.
 */
public class CrimeReport {

    private String reportId;
    private CrimeType crimeType;
    private Location location;
    private Severity severity;
    private String description;
    private ReportStatus status;
    private String reportedByUserId;    // null if anonymous
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
    private String assignedPoliceStationId;
    private int verificationCount;      // upvotes from other users
    private boolean isVerified;

    public enum CrimeType {
        HARASSMENT("Harassment"),
        STALKING("Stalking"),
        ASSAULT("Assault"),
        MOLESTATION("Molestation"),
        THEFT("Theft/Robbery"),
        ACID_ATTACK("Acid Attack"),
        KIDNAPPING("Kidnapping"),
        DOMESTIC_VIOLENCE("Domestic Violence"),
        RAPE("Rape/Sexual Assault"),
        EVE_TEASING("Eve Teasing"),
        CYBER_CRIME("Cyber Crime"),
        OTHER("Other");

        private final String displayName;
        CrimeType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum Severity {
        LOW(1, "Low - Minor incident"),
        MEDIUM(2, "Medium - Moderate threat"),
        HIGH(3, "High - Serious threat"),
        CRITICAL(4, "Critical - Life threatening");

        private final int level;
        private final String description;

        Severity(int level, String description) {
            this.level = level;
            this.description = description;
        }
        public int getLevel() { return level; }
        public String getDescription() { return description; }
    }

    public enum ReportStatus {
        PENDING, VERIFIED, UNDER_INVESTIGATION, RESOLVED, REJECTED
    }

    public CrimeReport(CrimeType crimeType, Location location, Severity severity,
                       String description, String reportedByUserId) {
        this.reportId = "RPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.crimeType = crimeType;
        this.location = location;
        this.severity = severity;
        this.description = description;
        this.reportedByUserId = reportedByUserId;
        this.status = ReportStatus.PENDING;
        this.reportedAt = LocalDateTime.now();
        this.verificationCount = 0;
        this.isVerified = false;
    }

    // Getters
    public String getReportId() { return reportId; }
    public CrimeType getCrimeType() { return crimeType; }
    public Location getLocation() { return location; }
    public Severity getSeverity() { return severity; }
    public String getDescription() { return description; }
    public ReportStatus getStatus() { return status; }
    public String getReportedByUserId() { return reportedByUserId; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public String getAssignedPoliceStationId() { return assignedPoliceStationId; }
    public int getVerificationCount() { return verificationCount; }
    public boolean isVerified() { return isVerified; }

    // Setters
    public void setStatus(ReportStatus status) {
        this.status = status;
        if (status == ReportStatus.RESOLVED) {
            this.resolvedAt = LocalDateTime.now();
        }
    }
    public void setAssignedPoliceStation(String stationId) {
        this.assignedPoliceStationId = stationId;
    }

    public void incrementVerification() {
        this.verificationCount++;
        if (this.verificationCount >= 3) {
            this.isVerified = true;
            this.status = ReportStatus.VERIFIED;
        }
    }

    public String getDangerZoneLabel() {
        return String.format("[%s] %s at %s", severity.name(),
                crimeType.getDisplayName(), location.getCity());
    }

    @Override
    public String toString() {
        return String.format("CrimeReport[id=%s, type=%s, severity=%s, location=%s, status=%s]",
                reportId, crimeType.getDisplayName(), severity.name(),
                location.getCity(), status);
    }
}
