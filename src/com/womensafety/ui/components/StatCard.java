package com.womensafety.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * StatCard — Dashboard stat card with animated counter and colored accent.
 */
public class StatCard extends JPanel {

    private final String title;
    private int targetValue;
    private int currentValue = 0;
    private final Color accent;
    private final String icon;
    private Timer countTimer;
    private JLabel valueLabel;

    public StatCard(String icon, String title, int value, Color accent) {
        this.icon = icon;
        this.title = title;
        this.targetValue = value;
        this.accent = accent;

        setPreferredSize(new Dimension(180, 110));
        setLayout(new BorderLayout());
        setOpaque(false);

        buildUI();
        startCountAnimation();
    }

    private void buildUI() {
        JPanel inner = new JPanel(new BorderLayout(0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Card background
                g2.setColor(AppTheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                // Top accent bar
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), 5, 4, 4);
                // Subtle glow
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                // Border
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        // Icon + title row
        JLabel iconLabel = new JLabel(icon + "  " + title);
        iconLabel.setFont(AppTheme.FONT_SMALL);
        iconLabel.setForeground(AppTheme.TEXT_SECONDARY);

        // Value
        valueLabel = new JLabel("0");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        valueLabel.setForeground(accent);

        inner.add(iconLabel,  BorderLayout.NORTH);
        inner.add(valueLabel, BorderLayout.CENTER);

        add(inner, BorderLayout.CENTER);
    }

    public void setValue(int v) {
        this.targetValue = v;
        startCountAnimation();
    }

    private void startCountAnimation() {
        if (countTimer != null) countTimer.stop();
        currentValue = 0;
        int steps = 25;
        int step  = Math.max(1, targetValue / steps);
        countTimer = new Timer(40, null);
        countTimer.addActionListener(e -> {
            currentValue = Math.min(currentValue + step, targetValue);
            valueLabel.setText(String.valueOf(currentValue));
            if (currentValue >= targetValue) countTimer.stop();
        });
        countTimer.start();
    }
}
