package com.womensafety.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a geographic danger zone derived from crime report density.
 * A zone is declared when multiple crimes occur within a radius threshold.
 */
public class DangerZone {

    private String zoneId;
    private String zoneName;
    private Location centerLocation;
    private double radiusKm;
    private DangerLevel dangerLevel;
    private List<CrimeReport> associatedReports;
    private LocalDateTime declaredAt;
    private LocalDateTime lastUpdated;
    private boolean isActive;
    private int totalReportsCount;

    public enum DangerLevel {
        WATCH(1, "🟡 WATCH", "Some incidents reported. Be cautious."),
        WARNING(2, "🟠 WARNING", "Multiple incidents. Avoid if possible."),
        DANGER(3, "🔴 DANGER", "High crime area. Stay away."),
        EXTREME(4, "🚨 EXTREME", "Critical crime zone. Do NOT enter.");

        private final int level;
        private final String label;
        private final String advice;

        DangerLevel(int level, String label, String advice) {
            this.level = level;
            this.label = label;
            this.advice = advice;
        }
        public int getLevel() { return level; }
        public String getLabel() { return label; }
        public String getAdvice() { return advice; }
    }

    public DangerZone(String zoneId, String zoneName, Location center, double radiusKm) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.centerLocation = center;
        this.radiusKm = radiusKm;
        this.associatedReports = new ArrayList<>();
        this.declaredAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.isActive = true;
        this.dangerLevel = DangerLevel.WATCH;
        this.totalReportsCount = 0;
    }

    public void addCrimeReport(CrimeReport report) {
        associatedReports.add(report);
        totalReportsCount++;
        lastUpdated = LocalDateTime.now();
        recalculateDangerLevel();
    }

    private void recalculateDangerLevel() {
        int criticalCount = (int) associatedReports.stream()
                .filter(r -> r.getSeverity() == CrimeReport.Severity.CRITICAL ||
                             r.getSeverity() == CrimeReport.Severity.HIGH)
                .count();

        if (criticalCount >= 5 || totalReportsCount >= 15) {
            this.dangerLevel = DangerLevel.EXTREME;
        } else if (criticalCount >= 3 || totalReportsCount >= 8) {
            this.dangerLevel = DangerLevel.DANGER;
        } else if (totalReportsCount >= 4) {
            this.dangerLevel = DangerLevel.WARNING;
        } else {
            this.dangerLevel = DangerLevel.WATCH;
        }
    }

    /**
     * Returns true if the given location falls within this danger zone's radius.
     */
    public boolean contains(Location location) {
        return centerLocation.distanceTo(location) <= radiusKm;
    }

    // Getters
    public String getZoneId() { return zoneId; }
    public String getZoneName() { return zoneName; }
    public Location getCenterLocation() { return centerLocation; }
    public double getRadiusKm() { return radiusKm; }
    public DangerLevel getDangerLevel() { return dangerLevel; }
    public List<CrimeReport> getAssociatedReports() { return new ArrayList<>(associatedReports); }
    public LocalDateTime getDeclaredAt() { return declaredAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public boolean isActive() { return isActive; }
    public int getTotalReportsCount() { return totalReportsCount; }

    public void setActive(boolean active) { this.isActive = active; }

    @Override
    public String toString() {
        return String.format("DangerZone[id=%s, name='%s', level=%s, reports=%d, radius=%.1fkm]",
                zoneId, zoneName, dangerLevel.getLabel(), totalReportsCount, radiusKm);
    }
}
