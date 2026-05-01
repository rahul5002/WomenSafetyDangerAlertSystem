package com.womensafety.ui;

import com.womensafety.alert.AlertService;
import com.womensafety.database.DatabaseManager;
import com.womensafety.exception.AuthenticationException;
import com.womensafety.exception.UserNotFoundException;
import com.womensafety.model.*;
import com.womensafety.service.CrimeDataService;
import com.womensafety.service.PoliceStationService;
import com.womensafety.service.UserService;
import com.womensafety.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * ApplicationMenu — the console-based user interface for the Women Safety System.
 * Drives user interaction via numbered menus.
 */
public class ApplicationMenu {

    private final UserService userService;
    private final CrimeDataService crimeService;
    private final PoliceStationService policeService;
    private final AlertService alertService;
    private final Scanner scanner;
    private final Logger logger;

    private User currentUser = null;

    public ApplicationMenu(UserService userService, CrimeDataService crimeService) {
        this.userService = userService;
        this.crimeService = crimeService;
        this.policeService = new PoliceStationService(DatabaseManager.getInstance());
        this.policeService.seedStations();
        this.alertService = new AlertService(policeService);
        this.scanner = new Scanner(System.in);
        this.logger = Logger.getInstance();
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showGuestMenu();
            } else {
                running = showUserMenu();
            }
        }
        System.out.println("\n  Stay Safe. Goodbye! 💜\n");
    }

    // ─── GUEST MENU ──────────────────────────────────────────────────────────

    private boolean showGuestMenu() {
        printSection("MAIN MENU");
        System.out.println("  1. Register New Account");
        System.out.println("  2. Login");
        System.out.println("  3. View Danger Zones (Public)");
        System.out.println("  4. View System Statistics");
        System.out.println("  0. Exit");
        System.out.print("\n  Enter choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": doRegister(); break;
            case "2": doLogin(); break;
            case "3": viewDangerZones(); break;
            case "4": viewStats(); break;
            case "0": return false;
            default:  System.out.println("  ❌ Invalid choice.");
        }
        return true;
    }

    // ─── USER MENU ───────────────────────────────────────────────────────────

    private boolean showUserMenu() {
        printSection("WELCOME, " + currentUser.getName().toUpperCase());
        System.out.println("  Status: " + getStatusEmoji(currentUser.getStatus()) + " " + currentUser.getStatus());
        System.out.println();
        System.out.println("  🚨 EMERGENCY");
        System.out.println("  1. Trigger SOS Alert");
        System.out.println("  2. Notify Police Directly");
        System.out.println();
        System.out.println("  🗺️  SAFETY INFO");
        System.out.println("  3. Check Nearby Crime Reports");
        System.out.println("  4. View All Danger Zones");
        System.out.println("  5. Update My Location");
        System.out.println();
        System.out.println("  👤 MY ACCOUNT");
        System.out.println("  6. Manage Emergency Contacts");
        System.out.println("  7. Submit Crime Report");
        System.out.println("  8. View My Alert History");
        System.out.println("  9. View System Statistics");
        System.out.println();
        System.out.println("  0. Logout");
        System.out.print("\n  Enter choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": doTriggerSOS(); break;
            case "2": doNotifyPolice(); break;
            case "3": doCheckNearbyCrimes(); break;
            case "4": viewDangerZones(); break;
            case "5": doUpdateLocation(); break;
            case "6": doManageContacts(); break;
            case "7": doSubmitCrimeReport(); break;
            case "8": doViewAlertHistory(); break;
            case "9": viewStats(); break;
            case "0": currentUser = null; System.out.println("\n  ✅ Logged out."); break;
            default:  System.out.println("  ❌ Invalid choice.");
        }
        return true;
    }

    // ─── ACTIONS ─────────────────────────────────────────────────────────────

    private void doRegister() {
        printSection("REGISTER");
        try {
            System.out.print("  Full Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("  Phone (10-digit): ");
            String phone = scanner.nextLine().trim();
            System.out.print("  Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("  Password (min 6 chars): ");
            String password = scanner.nextLine().trim();

            User user = userService.register(name, phone, email, password);
            System.out.println("\n  ✅ Registration successful! Your ID: " + user.getUserId());
            System.out.println("  Please login to continue.");
        } catch (Exception e) {
            System.out.println("  ❌ Registration failed: " + e.getMessage());
        }
    }

    private void doLogin() {
        printSection("LOGIN");
        try {
            System.out.print("  Phone: ");
            String phone = scanner.nextLine().trim();
            System.out.print("  Password: ");
            String password = scanner.nextLine().trim();
            currentUser = userService.login(phone, password);
            System.out.println("\n  ✅ Welcome back, " + currentUser.getName() + "!");
        } catch (UserNotFoundException | AuthenticationException e) {
            System.out.println("  ❌ Login failed: " + e.getMessage());
        }
    }

    private void doTriggerSOS() {
        printSection("🚨 SOS EMERGENCY ALERT");
        if (currentUser.getCurrentLocation() == null) {
            System.out.println("  ⚠️  No location set. Setting demo location (Delhi)...");
            currentUser.setCurrentLocation(
                    new Location(28.6139, 77.2090, "India Gate Area", "New Delhi", "Central Delhi", "Delhi"));
        }
        System.out.println("  Sending SOS to all emergency contacts and nearest police...");
        SOSAlert alert = alertService.triggerSOS(currentUser, "MANUAL_BUTTON");
        System.out.println("\n  ✅ SOS DISPATCHED!");
        System.out.println("  Alert ID  : " + alert.getAlertId());
        System.out.println("  Police    : " + (alert.isPoliceNotified() ? "✅ Notified" : "❌ Not notified"));
        System.out.println("  Contacts  : " + (alert.isContactsAlerted() ?
                "✅ " + currentUser.getEmergencyContacts().size() + " contact(s) notified" : "❌ No contacts"));
        if (alert.getNearestStation() != null)
            System.out.println("  Station   : " + alert.getNearestStation().getStationName() +
                    " | " + alert.getNearestStation().getPhone());
        System.out.println("\n  Payload:\n" + alert.buildNotificationPayload());
    }

    private void doNotifyPolice() {
        printSection("NOTIFY POLICE");
        if (currentUser.getCurrentLocation() == null) {
            currentUser.setCurrentLocation(
                    new Location(28.6139, 77.2090, "India Gate", "New Delhi", "Central Delhi", "Delhi"));
        }
        System.out.print("  Describe the incident: ");
        String desc = scanner.nextLine().trim();
        System.out.print("  Report anonymously? (y/n): ");
        boolean anon = scanner.nextLine().trim().equalsIgnoreCase("y");

        PoliceAlert alert = alertService.notifyPolice(currentUser, desc,
                PoliceAlert.PoliceAlertType.CRIME_REPORT, anon);

        if (alert != null) {
            System.out.println("\n  ✅ Police Notified!");
            System.out.println("  Case Number : " + alert.getCaseNumber());
            System.out.println("  Station     : " + alert.getTargetStation().getStationName());
            System.out.println("  Phone       : " + alert.getTargetStation().getPhone());
            System.out.println("  Anonymous   : " + (anon ? "Yes" : "No"));
        }
    }

    private void doCheckNearbyCrimes() {
        printSection("NEARBY CRIME REPORTS");
        if (currentUser.getCurrentLocation() == null) {
            System.out.println("  ⚠️  No location set. Using default (Connaught Place, Delhi).");
            currentUser.setCurrentLocation(
                    new Location(28.6315, 77.2167, "Connaught Place", "New Delhi", "Central Delhi", "Delhi"));
        }
        System.out.print("  Search radius in km (default 5): ");
        String radiusStr = scanner.nextLine().trim();
        double radius = radiusStr.isEmpty() ? 5.0 : Double.parseDouble(radiusStr);

        List<CrimeReport> nearby = crimeService.getNearbyCrimes(currentUser.getCurrentLocation(), radius);
        if (nearby.isEmpty()) {
            System.out.println("  ✅ No crimes reported within " + radius + " km. Stay safe!");
        } else {
            System.out.println("\n  ⚠️  " + nearby.size() + " crime(s) reported within " + radius + " km:\n");
            for (int i = 0; i < nearby.size(); i++) {
                CrimeReport r = nearby.get(i);
                double dist = currentUser.getCurrentLocation().distanceTo(r.getLocation());
                System.out.printf("  %d. [%s] %s%n", i + 1, r.getSeverity().name(), r.getCrimeType().getDisplayName());
                System.out.printf("     📍 %s (%.2f km away)%n", r.getLocation().getAddress(), dist);
                System.out.printf("     📝 %s%n", r.getDescription());
                System.out.printf("     🕐 %s%n%n", r.getReportedAt().toLocalDate());
            }
        }
    }

    private void viewDangerZones() {
        printSection("ACTIVE DANGER ZONES");
        List<DangerZone> zones = crimeService.getAllDangerZones();
        if (zones.isEmpty()) {
            System.out.println("  ✅ No danger zones declared.");
            return;
        }
        System.out.printf("  %-5s %-30s %-15s %-10s %-8s%n", "No.", "Zone Name", "Level", "Reports", "Radius");
        System.out.println("  " + "─".repeat(72));
        for (int i = 0; i < zones.size(); i++) {
            DangerZone z = zones.get(i);
            System.out.printf("  %-5d %-30s %-15s %-10d %-8.1f km%n",
                    i + 1, z.getZoneName(), z.getDangerLevel().getLabel(),
                    z.getTotalReportsCount(), z.getRadiusKm());
            System.out.println("        ℹ️  " + z.getDangerLevel().getAdvice());
            System.out.println("        📍 " + z.getCenterLocation().getCity() +
                    " | Last updated: " + z.getLastUpdated().toLocalDate());
            System.out.println();
        }
    }

    private void doUpdateLocation() {
        printSection("UPDATE LOCATION");
        System.out.println("  Select a preset location or enter custom:\n");
        System.out.println("  1. Connaught Place, New Delhi");
        System.out.println("  2. Karol Bagh, New Delhi");
        System.out.println("  3. Saket, New Delhi");
        System.out.println("  4. Rohini, New Delhi");
        System.out.println("  5. Noida Sector 18");
        System.out.println("  6. Enter custom coordinates");
        System.out.print("\n  Choice: ");

        Location loc;
        switch (scanner.nextLine().trim()) {
            case "1": loc = new Location(28.6315, 77.2167, "Connaught Place", "New Delhi", "Central Delhi", "Delhi"); break;
            case "2": loc = new Location(28.6514, 77.1907, "Karol Bagh", "New Delhi", "West Delhi", "Delhi"); break;
            case "3": loc = new Location(28.5244, 77.2066, "Saket", "New Delhi", "South Delhi", "Delhi"); break;
            case "4": loc = new Location(28.7495, 77.1167, "Rohini", "New Delhi", "North-West Delhi", "Delhi"); break;
            case "5": loc = new Location(28.5355, 77.3910, "Sector 18", "Noida", "Gautam Buddh Nagar", "UP"); break;
            case "6":
                System.out.print("  Latitude: ");
                double lat = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("  Longitude: ");
                double lon = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("  City: ");
                String city = scanner.nextLine().trim();
                loc = new Location(lat, lon, "Custom Location", city, "Unknown", "Unknown");
                break;
            default:
                System.out.println("  ❌ Invalid choice.");
                return;
        }

        userService.updateLocation(currentUser, loc);
        System.out.println("  ✅ Location updated: " + loc.toShortString());

        // Auto-check for danger zones at new location
        List<DangerZone> nearbyZones = crimeService.getDangerZonesNear(loc, 3.0);
        if (!nearbyZones.isEmpty()) {
            System.out.println("\n  ⚠️  DANGER ZONES NEAR YOUR LOCATION:");
            for (DangerZone z : nearbyZones) {
                System.out.println("  → " + z.getDangerLevel().getLabel() + " | " + z.getZoneName());
                System.out.println("    " + z.getDangerLevel().getAdvice());
            }
        } else {
            System.out.println("  ✅ No danger zones near your current location.");
        }
    }

    private void doManageContacts() {
        printSection("EMERGENCY CONTACTS");
        List<EmergencyContact> contacts = currentUser.getEmergencyContacts();

        if (contacts.isEmpty()) {
            System.out.println("  No emergency contacts added yet.");
        } else {
            System.out.printf("  %-5s %-20s %-15s %-15s%n", "No.", "Name", "Phone", "Relation");
            System.out.println("  " + "─".repeat(57));
            for (int i = 0; i < contacts.size(); i++) {
                EmergencyContact c = contacts.get(i);
                System.out.printf("  %-5d %-20s %-15s %-15s%n",
                        i + 1, c.getName(), c.getPhone(), c.getRelationship());
            }
        }

        System.out.println("\n  1. Add Contact");
        System.out.println("  2. Remove Contact");
        System.out.println("  3. Back");
        System.out.print("\n  Choice: ");

        switch (scanner.nextLine().trim()) {
            case "1":
                System.out.print("  Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("  Phone: ");
                String phone = scanner.nextLine().trim();
                System.out.print("  Email: ");
                String email = scanner.nextLine().trim();
                System.out.print("  Relationship (e.g. Mother, Friend): ");
                String rel = scanner.nextLine().trim();
                try {
                    EmergencyContact c = userService.addEmergencyContact(currentUser, name, phone, email, rel);
                    System.out.println("  ✅ Contact added: " + c.getName() + " (" + c.getContactId() + ")");
                } catch (Exception e) {
                    System.out.println("  ❌ " + e.getMessage());
                }
                break;
            case "2":
                if (contacts.isEmpty()) { System.out.println("  No contacts to remove."); break; }
                System.out.print("  Enter contact number to remove: ");
                try {
                    int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (idx >= 0 && idx < contacts.size()) {
                        String cid = contacts.get(idx).getContactId();
                        userService.removeEmergencyContact(currentUser, cid);
                        System.out.println("  ✅ Contact removed.");
                    } else System.out.println("  ❌ Invalid number.");
                } catch (NumberFormatException e) {
                    System.out.println("  ❌ Invalid input.");
                }
                break;
        }
    }

    private void doSubmitCrimeReport() {
        printSection("SUBMIT CRIME REPORT");
        System.out.println("  Crime Types:");
        CrimeReport.CrimeType[] types = CrimeReport.CrimeType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %2d. %s%n", i + 1, types[i].getDisplayName());
        }
        System.out.print("\n  Select crime type: ");
        int typeIdx;
        try {
            typeIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (typeIdx < 0 || typeIdx >= types.length) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("  ❌ Invalid selection.");
            return;
        }

        System.out.println("\n  Severity Levels:");
        System.out.println("  1. LOW    2. MEDIUM    3. HIGH    4. CRITICAL");
        System.out.print("  Select severity: ");
        CrimeReport.Severity severity;
        try {
            int sev = Integer.parseInt(scanner.nextLine().trim());
            severity = CrimeReport.Severity.values()[sev - 1];
        } catch (Exception e) {
            System.out.println("  ❌ Invalid severity.");
            return;
        }

        System.out.print("  Incident description: ");
        String desc = scanner.nextLine().trim();

        Location loc = currentUser.getCurrentLocation() != null ? currentUser.getCurrentLocation() :
                new Location(28.6139, 77.2090, "Unknown", "New Delhi", "Delhi", "Delhi");

        System.out.print("  Use current location? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("n")) {
            System.out.print("  City of incident: ");
            String city = scanner.nextLine().trim();
            loc = new Location(loc.getLatitude(), loc.getLongitude(), "Reported Location", city, "Unknown", "Unknown");
        }

        System.out.print("  Report anonymously? (y/n): ");
        String userId = scanner.nextLine().trim().equalsIgnoreCase("y") ? null : currentUser.getUserId();

        CrimeReport report = crimeService.submitReport(types[typeIdx], loc, severity, desc, userId);
        System.out.println("\n  ✅ Report submitted!");
        System.out.println("  Report ID : " + report.getReportId());
        System.out.println("  Status    : " + report.getStatus());
        System.out.println("  Thank you for helping keep women safe. 💜");
    }

    private void doViewAlertHistory() {
        printSection("MY ALERT HISTORY");
        List<Alert> alerts = alertService.getAlertsByUser(currentUser.getUserId());
        if (alerts.isEmpty()) {
            System.out.println("  No alerts triggered yet.");
            return;
        }
        for (int i = 0; i < alerts.size(); i++) {
            Alert a = alerts.get(i);
            System.out.printf("  %d. [%s] %s | Status: %s | %s%n",
                    i + 1, a.getPriority(), a.getAlertType().getDisplayName(),
                    a.getStatus(), a.getCreatedAt().toLocalDate());
        }
    }

    private void viewStats() {
        printSection("SYSTEM STATISTICS");
        Map<String, Integer> stats = DatabaseManager.getInstance().getSystemStats();
        for (Map.Entry<String, Integer> e : stats.entrySet()) {
            System.out.printf("  %-30s : %d%n", e.getKey(), e.getValue());
        }
    }

    // ─── UI HELPERS ──────────────────────────────────────────────────────────

    private void printBanner() {
        System.out.println("\u001B[35m");
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║       🛡️  WOMEN SAFETY DANGER ALERT SYSTEM  🛡️       ║");
        System.out.println("  ║         Empowering Women. Preventing Crime.          ║");
        System.out.println("  ║              Emergency Helpline: 112                 ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println("\u001B[0m");
    }

    private void printSection(String title) {
        System.out.println("\n\u001B[36m  ── " + title + " " + "─".repeat(Math.max(0, 46 - title.length())) + "\u001B[0m");
    }

    private String getStatusEmoji(User.UserStatus status) {
        switch (status) {
            case SAFE:    return "✅";
            case ALERT:   return "⚠️";
            case DANGER:  return "🚨";
            default:      return "❓";
        }
    }
}
