package com.womensafety;

import com.womensafety.alert.AlertService;
import com.womensafety.database.DatabaseManager;
import com.womensafety.service.CrimeDataService;
import com.womensafety.service.PoliceStationService;
import com.womensafety.service.UserService;
import com.womensafety.ui.SwingApp;
import com.womensafety.util.Logger;

/**
 * Main entry point for the Women Safety Danger Alert System (GUI Version).
 * Initializes core services and launches the Java Swing GUI.
 */
public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.log("INFO", "Starting Women Safety Danger Alert System (GUI Mode)...");

        // Initialize database
        DatabaseManager db = DatabaseManager.getInstance();
        db.initialize();

        // Seed initial police station data
        PoliceStationService policeStationService = new PoliceStationService(db);
        policeStationService.seedStations();

        // Seed initial crime data
        CrimeDataService crimeService = new CrimeDataService(db);
        crimeService.seedInitialData();

        // Initialize user & alert services
        UserService userService = new UserService(db);
        AlertService alertService = new AlertService(policeStationService);

        // Launch Java Swing GUI
        SwingApp app = new SwingApp(userService, crimeService, alertService, db);
        app.start();

        logger.log("INFO", "GUI Initialization dispatched.");
    }
}
