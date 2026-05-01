package com.womensafety.service;

import com.womensafety.database.DatabaseManager;
import com.womensafety.model.Location;
import com.womensafety.model.PoliceStation;
import com.womensafety.util.Logger;

import java.util.List;

/**
 * PoliceStationService — manages police station data and nearest-station lookup.
 */
public class PoliceStationService {

    private final DatabaseManager db;
    private final Logger logger;

    public PoliceStationService(DatabaseManager db) {
        this.db = db;
        this.logger = Logger.getInstance();
    }

    public void seedStations() {
        addStation("PS001", "Connaught Place Police Station", "CP-01",
                new Location(28.6315, 77.2167, "Connaught Place", "New Delhi", "Central Delhi", "Delhi"),
                "01123341234", "cp.station@delhi.police.gov.in", "Central Delhi");

        addStation("PS002", "Lodhi Colony Police Station", "LC-02",
                new Location(28.5921, 77.2270, "Lodhi Colony", "New Delhi", "South Delhi", "Delhi"),
                "01124601234", "lodhi.station@delhi.police.gov.in", "South Delhi");

        addStation("PS003", "Karol Bagh Police Station", "KB-03",
                new Location(28.6514, 77.1907, "Karol Bagh", "New Delhi", "West Delhi", "Delhi"),
                "01125781234", "karolbagh.station@delhi.police.gov.in", "West Delhi");

        addStation("PS004", "Saket Police Station", "SK-04",
                new Location(28.5244, 77.2066, "Saket", "New Delhi", "South Delhi", "Delhi"),
                "01129561234", "saket.station@delhi.police.gov.in", "South-West Delhi");

        addStation("PS005", "Rohini Police Station", "RH-05",
                new Location(28.7495, 77.1167, "Rohini", "New Delhi", "North-West Delhi", "Delhi"),
                "01127891234", "rohini.station@delhi.police.gov.in", "North-West Delhi");

        logger.log("INFO", "Police stations seeded: " + db.getAllPoliceStations().size());
    }

    private void addStation(String id, String name, String code, Location loc,
                             String phone, String email, String jurisdiction) {
        db.savePoliceStation(new PoliceStation(id, name, code, loc, phone, email, jurisdiction));
    }

    /**
     * Finds the nearest police station to the given location using Haversine distance.
     */
    public PoliceStation findNearestStation(Location userLocation) {
        List<PoliceStation> stations = db.getAllPoliceStations();
        PoliceStation nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (PoliceStation station : stations) {
            if (!station.isActive()) continue;
            double distance = station.distanceFrom(userLocation);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = station;
            }
        }

        if (nearest != null) {
            logger.log("INFO", String.format("Nearest station: %s (%.2f km away)",
                    nearest.getStationName(), minDistance));
        }
        return nearest;
    }

    public List<PoliceStation> getAllStations() { return db.getAllPoliceStations(); }
}
