package com.womensafety.database;

import com.womensafety.model.*;
import com.womensafety.util.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DatabaseManager — in-memory data store simulating a database.
 * Implements the Singleton pattern. In production, replace with JDBC/JPA.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private final Logger logger;

    // In-memory "tables"
    private final Map<String, User> users;
    private final Map<String, CrimeReport> crimeReports;
    private final Map<String, PoliceStation> policeStations;
    private final Map<String, DangerZone> dangerZones;
    private final Map<String, Alert> alerts;

    private boolean initialized = false;

    private DatabaseManager() {
        this.logger = Logger.getInstance();
        this.users = new ConcurrentHashMap<>();
        this.crimeReports = new ConcurrentHashMap<>();
        this.policeStations = new ConcurrentHashMap<>();
        this.dangerZones = new ConcurrentHashMap<>();
        this.alerts = new ConcurrentHashMap<>();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initialize() {
        if (!initialized) {
            logger.log("INFO", "DatabaseManager initialized (in-memory mode).");
            initialized = true;
        }
    }

    // ─── USER OPERATIONS ─────────────────────────────────────────────────────

    public void saveUser(User user) {
        users.put(user.getUserId(), user);
    }

    public Optional<User> findUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findUserByPhone(String phone) {
        return users.values().stream()
                .filter(u -> u.getPhone().equals(phone))
                .findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean deleteUser(String userId) {
        return users.remove(userId) != null;
    }

    // ─── CRIME REPORT OPERATIONS ─────────────────────────────────────────────

    public void saveCrimeReport(CrimeReport report) {
        crimeReports.put(report.getReportId(), report);
    }

    public Optional<CrimeReport> findCrimeReportById(String id) {
        return Optional.ofNullable(crimeReports.get(id));
    }

    public List<CrimeReport> getAllCrimeReports() {
        return new ArrayList<>(crimeReports.values());
    }

    public List<CrimeReport> getCrimeReportsByCity(String city) {
        List<CrimeReport> result = new ArrayList<>();
        for (CrimeReport r : crimeReports.values()) {
            if (r.getLocation().getCity().equalsIgnoreCase(city)) {
                result.add(r);
            }
        }
        return result;
    }

    // ─── POLICE STATION OPERATIONS ────────────────────────────────────────────

    public void savePoliceStation(PoliceStation station) {
        policeStations.put(station.getStationId(), station);
    }

    public List<PoliceStation> getAllPoliceStations() {
        return new ArrayList<>(policeStations.values());
    }

    public Optional<PoliceStation> findPoliceStationById(String id) {
        return Optional.ofNullable(policeStations.get(id));
    }

    // ─── DANGER ZONE OPERATIONS ──────────────────────────────────────────────

    public void saveDangerZone(DangerZone zone) {
        dangerZones.put(zone.getZoneId(), zone);
    }

    public List<DangerZone> getAllDangerZones() {
        return new ArrayList<>(dangerZones.values());
    }

    public List<DangerZone> getActiveDangerZones() {
        List<DangerZone> result = new ArrayList<>();
        for (DangerZone z : dangerZones.values()) {
            if (z.isActive()) result.add(z);
        }
        return result;
    }

    // ─── ALERT OPERATIONS ────────────────────────────────────────────────────

    public void saveAlert(Alert alert) {
        alerts.put(alert.getAlertId(), alert);
    }

    public List<Alert> getAllAlerts() {
        return new ArrayList<>(alerts.values());
    }

    // ─── STATS ───────────────────────────────────────────────────────────────

    public Map<String, Integer> getSystemStats() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("Total Users", users.size());
        stats.put("Total Crime Reports", crimeReports.size());
        stats.put("Active Danger Zones", (int) dangerZones.values().stream().filter(DangerZone::isActive).count());
        stats.put("Police Stations", policeStations.size());
        stats.put("Total Alerts Sent", alerts.size());
        return stats;
    }
}
