package com.womensafety.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ToastNotification — Slide-in, auto-dismiss popup notification overlay.
 */
public class ToastNotification extends JWindow {

    public enum ToastType { SUCCESS, WARNING, ERROR, INFO }

    private static final int WIDTH  = 340;
    private static final int HEIGHT = 70;
    private float opacity = 0f;

    public ToastNotification(JFrame parent, String message, ToastType type, int durationMs) {
        super(parent);
        setSize(WIDTH, HEIGHT);

        // Position: bottom-right of parent
        Point loc = parent.getLocationOnScreen();
        setLocation(loc.x + parent.getWidth()  - WIDTH  - 20,
                    loc.y + parent.getHeight() - HEIGHT - 50);

        Color bg, accent;
        String icon;
        switch (type) {
            case SUCCESS: bg = new Color(20, 60, 30); accent = AppTheme.ACCENT_GREEN;  icon = "✓"; break;
            case WARNING: bg = new Color(60, 45, 10); accent = AppTheme.ACCENT_YELLOW; icon = "⚠"; break;
            case ERROR:   bg = new Color(60, 15, 15); accent = AppTheme.ACCENT_RED;    icon = "✕"; break;
            default:      bg = new Color(20, 30, 60); accent = AppTheme.ACCENT_PURPLE; icon = "ℹ"; break;
        }

        JPanel content = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
                // Left accent bar
                g2.fillRoundRect(0, 0, 5, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        iconLabel.setForeground(accent);

        JLabel msgLabel = new JLabel("<html><body style='width:240px'>" + message + "</body></html>");
        msgLabel.setFont(AppTheme.FONT_BODY);
        msgLabel.setForeground(AppTheme.TEXT_PRIMARY);

        content.add(iconLabel, BorderLayout.WEST);
        content.add(msgLabel,  BorderLayout.CENTER);
        setContentPane(content);

        // Make window transparent
        try { setOpacity(0f); } catch (Exception ignored) {}

        // Fade-in animation
        Timer fadeIn = new Timer(20, null);
        fadeIn.addActionListener(e -> {
            opacity = Math.min(1f, opacity + 0.08f);
            try { setOpacity(opacity); } catch (Exception ignored) {}
            if (opacity >= 1f) fadeIn.stop();
        });

        // Auto-dismiss with fade-out
        Timer dismiss = new Timer(durationMs, null);
        dismiss.setRepeats(false);
        dismiss.addActionListener(e -> {
            Timer fadeOut = new Timer(20, null);
            fadeOut.addActionListener(e2 -> {
                opacity = Math.max(0f, opacity - 0.07f);
                try { setOpacity(opacity); } catch (Exception ignored) {}
                if (opacity <= 0f) { fadeOut.stop(); dispose(); }
            });
            fadeOut.start();
        });

        setVisible(true);
        fadeIn.start();
        dismiss.start();
    }

    /** Static helper to show a toast easily */
    public static void show(JFrame parent, String message, ToastType type) {
        SwingUtilities.invokeLater(() -> new ToastNotification(parent, message, type, 3500));
    }

    public static void success(JFrame parent, String msg) { show(parent, msg, ToastType.SUCCESS); }
    public static void error(JFrame parent, String msg)   { show(parent, msg, ToastType.ERROR);   }
    public static void warning(JFrame parent, String msg) { show(parent, msg, ToastType.WARNING); }
    public static void info(JFrame parent, String msg)    { show(parent, msg, ToastType.INFO);    }
}
