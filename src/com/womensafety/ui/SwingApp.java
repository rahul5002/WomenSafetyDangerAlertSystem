package com.womensafety.ui;

import com.womensafety.alert.AlertService;
import com.womensafety.database.DatabaseManager;
import com.womensafety.model.Location;
import com.womensafety.model.User;
import com.womensafety.service.CrimeDataService;
import com.womensafety.service.PoliceStationService;
import com.womensafety.service.UserService;
import com.womensafety.ui.components.AppTheme;
import com.womensafety.ui.components.ToastNotification;
import com.womensafety.ui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * SwingApp — Main entry point for the GUI application.
 * Manages the JFrame, CardLayout navigation, and service references.
 */
public class SwingApp {

    private final UserService userService;
    private final CrimeDataService crimeDataService;
    private final AlertService alertService;
    private final DatabaseManager db;

    private JFrame frame;
    private JPanel mainCardPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;

    private User currentUser;

    // Panels
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    private MapPanel mapPanel;
    private CrimePanel crimePanel;
    private AlertPanel alertPanel;
    private ContactsPanel contactsPanel;
    private ProfilePanel profilePanel;
    private HelplinePanel helplinePanel;

    public SwingApp(UserService userService, CrimeDataService crimeDataService, AlertService alertService, DatabaseManager db) {
        this.userService = userService;
        this.crimeDataService = crimeDataService;
        this.alertService = alertService;
        this.db = db;
        
        AppTheme.applyGlobalTheme();
    }

    public void start() {
        SwingUtilities.invokeLater(this::initUI);
    }

    private void initUI() {
        frame = new JFrame("Women Safety Danger Alert System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Card Layout for main content area
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);

        // Initialize panels
        loginPanel = new LoginPanel(this);
        dashboardPanel = new DashboardPanel(this);
        mapPanel = new MapPanel(this);
        crimePanel = new CrimePanel(this);
        alertPanel = new AlertPanel(this);
        contactsPanel = new ContactsPanel(this);
        profilePanel = new ProfilePanel(this);
        helplinePanel = new HelplinePanel(this);

        mainCardPanel.add(loginPanel, "LOGIN");
        mainCardPanel.add(dashboardPanel, "DASHBOARD");
        mainCardPanel.add(mapPanel, "MAP");
        mainCardPanel.add(crimePanel, "CRIME");
        mainCardPanel.add(alertPanel, "ALERTS");
        mainCardPanel.add(contactsPanel, "CONTACTS");
        mainCardPanel.add(profilePanel, "PROFILE");
        mainCardPanel.add(helplinePanel, "HELPLINE");

        // Sidebar (initially hidden)
        sidebarPanel = buildSidebar();
        sidebarPanel.setVisible(false);

        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(mainCardPanel, BorderLayout.CENTER);

        frame.setVisible(true);
        cardLayout.show(mainCardPanel, "LOGIN");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(AppTheme.BG_SIDEBAR);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER));

        // Logo
        JLabel logo = AppTheme.label("🛡 Women Safety", AppTheme.FONT_TITLE, AppTheme.ACCENT_PURPLE2);
        logo.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        sidebar.add(logo, BorderLayout.NORTH);

        // Nav buttons
        JPanel navMenu = new JPanel();
        navMenu.setLayout(new BoxLayout(navMenu, BoxLayout.Y_AXIS));
        navMenu.setOpaque(false);

        navMenu.add(createNavBtn("🏠 Dashboard", "DASHBOARD"));
        navMenu.add(createNavBtn("🗺 Crime Map", "MAP"));
        navMenu.add(createNavBtn("📄 Crime Reports", "CRIME"));
        navMenu.add(createNavBtn("🚨 Alerts History", "ALERTS"));
        navMenu.add(createNavBtn("👥 Contacts", "CONTACTS"));
        navMenu.add(createNavBtn("👤 Profile", "PROFILE"));
        navMenu.add(createNavBtn("☎ Helplines", "HELPLINE"));

        sidebar.add(navMenu, BorderLayout.CENTER);

        // Logout
        JButton logoutBtn = createNavBtn("🚪 Logout", "LOGIN");
        logoutBtn.setForeground(AppTheme.ACCENT_RED2);
        logoutBtn.addActionListener(e -> doLogout());
        
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        bottom.add(logoutBtn);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createNavBtn(String text, String cardName) {
        JButton btn = AppTheme.ghostBtn(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.addActionListener(e -> {
            cardLayout.show(mainCardPanel, cardName);
            refreshAll();
        });
        return btn;
    }

    // --- Actions ---

    public void onLogin(String phone, String password) {
        this.currentUser = userService.login(phone, password);
        ToastNotification.success(frame, "Welcome back, " + currentUser.getName() + "!");
        sidebarPanel.setVisible(true);
        cardLayout.show(mainCardPanel, "DASHBOARD");
        refreshAll();
    }

    public void onRegister(String name, String phone, String email, String pass) {
        userService.register(name, phone, email, pass);
    }

    private void doLogout() {
        currentUser = null;
        sidebarPanel.setVisible(false);
        cardLayout.show(mainCardPanel, "LOGIN");
        ToastNotification.info(frame, "Logged out successfully.");
    }

    public void triggerSOS() {
        if (currentUser == null) return;
        alertService.triggerSOS(currentUser, "GUI_SOS_BUTTON");
        ToastNotification.error(frame, "SOS EMERGENCY TRIGGERED!");
        refreshAll();
    }

    public void updateLocation(Location loc) {
        if (currentUser == null) return;
        userService.updateLocation(currentUser, loc);
        ToastNotification.info(frame, "Location updated: " + loc.getCity());
        refreshAll();
    }

    public void refreshAll() {
        dashboardPanel.refreshData();
        mapPanel.refreshData();
        crimePanel.refreshData();
        alertPanel.refreshData();
        contactsPanel.refreshData();
        profilePanel.refreshData();
        helplinePanel.refreshData();
    }

    // --- Getters ---
    public JFrame getFrame() { return frame; }
    public User getCurrentUser() { return currentUser; }
    public UserService getUserService() { return userService; }
    public CrimeDataService getCrimeDataService() { return crimeDataService; }
    public AlertService getAlertService() { return alertService; }
    public Map<String, Integer> getSystemStats() { return db.getSystemStats(); }
}
