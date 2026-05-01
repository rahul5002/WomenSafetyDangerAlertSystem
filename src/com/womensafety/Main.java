package com.womensafety;

import com.womensafety.ui.ApplicationMenu;
import com.womensafety.database.DatabaseManager;
import com.womensafety.service.CrimeDataService;
import com.womensafety.service.UserService;
import com.womensafety.util.Logger;

/**
 * Main entry point for the Women Safety Danger Alert System.
 * Initializes core services and launches the application menu.
 */
public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.log("INFO", "Starting Women Safety Danger Alert System...");

        // Initialize database
        DatabaseManager db = DatabaseManager.getInstance();
        db.initialize();

        // Seed initial crime data
        CrimeDataService crimeService = new CrimeDataService(db);
        crimeService.seedInitialData();

        // Initialize user service
        UserService userService = new UserService(db);

        // Launch application menu
        ApplicationMenu menu = new ApplicationMenu(userService, crimeService);
        menu.start();

        logger.log("INFO", "Women Safety Danger Alert System terminated.");
    }
}
