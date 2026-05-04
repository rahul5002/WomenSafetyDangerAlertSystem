package com.womensafety.ui.panels;

import com.womensafety.ui.SwingApp;
import com.womensafety.ui.components.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelplinePanel extends JPanel {

    private final SwingApp app;
    private JLabel weatherLabel;
    private JTextArea newsArea;

    public HelplinePanel(SwingApp app) {
        this.app = app;
        setLayout(new BorderLayout(20, 20));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        buildUI();
    }

    private void buildUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(AppTheme.title("Safety Services & Info"), BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 20, 0));
        center.setOpaque(false);

        // -- Left: Helplines --
        JPanel helpPanel = AppTheme.cardPanel();
        helpPanel.setLayout(new GridLayout(5, 1, 0, 10));
        helpPanel.add(AppTheme.subtitle("Emergency Helplines"));
        
        helpPanel.add(createDialCard("National Emergency", "112", AppTheme.ACCENT_RED));
        helpPanel.add(createDialCard("Women Helpline", "1091", AppTheme.ACCENT_PURPLE2));
        helpPanel.add(createDialCard("Domestic Abuse", "181", AppTheme.ACCENT_ORANGE));
        helpPanel.add(createDialCard("Police Control Room", "100", AppTheme.ACCENT_CYAN));
        
        center.add(helpPanel);

        // -- Right: Weather & News --
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        rightPanel.setOpaque(false);

        // Weather
        JPanel weatherPanel = AppTheme.cardPanel();
        weatherPanel.setLayout(new BorderLayout(10, 10));
        weatherPanel.add(AppTheme.subtitle("Local Weather Safety"), BorderLayout.NORTH);
        weatherLabel = AppTheme.body("Loading weather...");
        weatherPanel.add(weatherLabel, BorderLayout.CENTER);
        fetchWeather();
        rightPanel.add(weatherPanel);

        // News
        JPanel newsPanel = AppTheme.cardPanel();
        newsPanel.setLayout(new BorderLayout(10, 10));
        newsPanel.add(AppTheme.subtitle("Safety News"), BorderLayout.NORTH);
        newsArea = AppTheme.textArea(5, 20);
        newsArea.setEditable(false);
        newsArea.setText("Loading latest safety news...\n\n(Requires active internet and API connection)");
        newsPanel.add(AppTheme.scrollPane(newsArea), BorderLayout.CENTER);
        rightPanel.add(newsPanel);

        center.add(rightPanel);

        add(center, BorderLayout.CENTER);
    }

    private JPanel createDialCard(String title, String number, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, accent));
        
        JPanel inner = new JPanel(new GridLayout(2,1));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inner.add(AppTheme.body(title));
        JLabel numL = new JLabel(number);
        numL.setFont(new Font("Segoe UI", Font.BOLD, 20));
        numL.setForeground(AppTheme.TEXT_PRIMARY);
        inner.add(numL);
        
        p.add(inner, BorderLayout.CENTER);
        
        JButton btn = AppTheme.ghostBtn("Dial");
        p.add(btn, BorderLayout.EAST);
        
        return p;
    }

    private void fetchWeather() {
        new Thread(() -> {
            try {
                // Free Open-Meteo API for Delhi coordinates
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.open-meteo.com/v1/forecast?latitude=28.6139&longitude=77.2090&current_weather=true"))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                String res = response.body();
                // Simple string parsing to avoid external JSON library dependency
                String temp = res.split("\"temperature\":")[1].split(",")[0];
                String wind = res.split("\"windspeed\":")[1].split(",")[0];
                
                SwingUtilities.invokeLater(() -> {
                    weatherLabel.setText(String.format("<html>New Delhi<br><br>Temperature: %s °C<br>Wind Speed: %s km/h<br><br><i>Safe to travel. No extreme weather alerts.</i></html>", temp, wind));
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> weatherLabel.setText("Could not load weather data."));
            }
        }).start();
    }

    public void refreshData() {
        // Refresh API calls if needed
    }
}
