package com.womensafety.ui.panels;

import com.womensafety.model.*;
import com.womensafety.ui.SwingApp;
import com.womensafety.ui.components.AppTheme;
import com.womensafety.ui.components.SOSButton;
import com.womensafety.ui.components.StatCard;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final SwingApp app;
    private JLabel statusLabel;
    private JPanel statsGrid;
    private StatCard usersCard, reportsCard, zonesCard, alertsCard;

    public DashboardPanel(SwingApp app) {
        this.app = app;
        setLayout(new BorderLayout(20, 20));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        buildHeader();
        buildCenter();
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setOpaque(false);
        titlePanel.add(AppTheme.title("Dashboard"));
        statusLabel = AppTheme.subtitle("Status: SAFE ✓");
        statusLabel.setForeground(AppTheme.ACCENT_GREEN);
        titlePanel.add(statusLabel);

        header.add(titlePanel, BorderLayout.WEST);

        // Location selector
        JPanel locPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        locPanel.setOpaque(false);
        locPanel.add(AppTheme.muted("Current Location: "));
        String[] locations = {"New Delhi", "Noida", "Gurugram"};
        JComboBox<String> locCombo = AppTheme.comboBox(locations);
        locCombo.setPreferredSize(new Dimension(150, 35));
        locCombo.addActionListener(e -> {
            String selected = (String) locCombo.getSelectedItem();
            Location newLoc = new Location(28.6139, 77.2090, selected, selected, selected, "Delhi"); // Mock coords
            app.updateLocation(newLoc);
        });
        locPanel.add(locCombo);
        header.add(locPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void buildCenter() {
        JPanel center = new JPanel(new BorderLayout(20, 20));
        center.setOpaque(false);

        // -- Stats Grid --
        statsGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        statsGrid.setOpaque(false);
        
        usersCard = new StatCard("👤", "Total Users", 0, AppTheme.ACCENT_PURPLE2);
        reportsCard = new StatCard("📄", "Crime Reports", 0, AppTheme.ACCENT_ORANGE);
        zonesCard = new StatCard("⚠️", "Danger Zones", 0, AppTheme.ACCENT_YELLOW);
        alertsCard = new StatCard("🚨", "Alerts Sent", 0, AppTheme.ACCENT_RED2);

        statsGrid.add(usersCard);
        statsGrid.add(reportsCard);
        statsGrid.add(zonesCard);
        statsGrid.add(alertsCard);

        center.add(statsGrid, BorderLayout.NORTH);

        // -- SOS Section --
        JPanel sosSection = AppTheme.cardPanel();
        sosSection.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.insets = new Insets(10, 10, 10, 10);
        
        JLabel sosTitle = AppTheme.label("EMERGENCY RESPONSE", AppTheme.FONT_TITLE, AppTheme.ACCENT_RED);
        sosSection.add(sosTitle, c);

        c.gridy = 1;
        JLabel sosSub = AppTheme.muted("Tap the button below to notify police and emergency contacts immediately.");
        sosSection.add(sosSub, c);

        c.gridy = 2; c.insets = new Insets(30, 10, 30, 10);
        SOSButton sosBtn = new SOSButton(() -> app.triggerSOS());
        sosSection.add(sosBtn, c);

        center.add(sosSection, BorderLayout.CENTER);

        // -- Recent Alerts List --
        JPanel recentAlertsPanel = AppTheme.cardPanel();
        recentAlertsPanel.setLayout(new BorderLayout(0, 10));
        recentAlertsPanel.setPreferredSize(new Dimension(0, 150));
        recentAlertsPanel.add(AppTheme.subtitle("Recent Activity"), BorderLayout.NORTH);

        JList<String> list = new JList<>(new String[]{"No recent alerts in your area."});
        list.setBackground(AppTheme.BG_CARD);
        list.setForeground(AppTheme.TEXT_SECONDARY);
        recentAlertsPanel.add(AppTheme.scrollPane(list), BorderLayout.CENTER);
        
        center.add(recentAlertsPanel, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    public void refreshData() {
        Map<String, Integer> stats = app.getSystemStats();
        usersCard.setValue(stats.getOrDefault("Total Users", 0));
        reportsCard.setValue(stats.getOrDefault("Total Crime Reports", 0));
        zonesCard.setValue(stats.getOrDefault("Active Danger Zones", 0));
        alertsCard.setValue(stats.getOrDefault("Total Alerts Sent", 0));

        User currentUser = app.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getStatus() == User.UserStatus.DANGER) {
                statusLabel.setText("Status: DANGER ⚠");
                statusLabel.setForeground(AppTheme.ACCENT_RED);
            } else {
                statusLabel.setText("Status: SAFE ✓");
                statusLabel.setForeground(AppTheme.ACCENT_GREEN);
            }
        }
    }
}
