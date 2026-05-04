package com.womensafety.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * SOSButton — Animated pulsing emergency SOS button.
 * Displays a large red circle that pulses to draw attention.
 */
public class SOSButton extends JPanel {

    private float pulseRadius = 0f;
    private float pulseAlpha  = 0f;
    private boolean pulsing   = false;
    private Timer pulseTimer;
    private Runnable onSOS;

    private static final int SIZE = 160;
    private boolean hovered = false;

    public SOSButton(Runnable onSOSAction) {
        this.onSOS = onSOSAction;
        setPreferredSize(new Dimension(SIZE + 60, SIZE + 60));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Click to send SOS Emergency Alert");

        // Start idle pulsing animation
        startIdlePulse();

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            @Override public void mouseClicked(MouseEvent e) {
                if (onSOS != null) onSOS.run();
                triggerSendAnimation();
            }
        });
    }

    private void startIdlePulse() {
        pulseTimer = new Timer(30, null);
        pulseTimer.addActionListener(e -> {
            pulseRadius += 1.5f;
            pulseAlpha = Math.max(0f, 0.6f - (pulseRadius / 60f));
            if (pulseRadius > 60f) { pulseRadius = 0f; pulseAlpha = 0.6f; }
            repaint();
        });
        pulseTimer.start();
    }

    public void triggerSendAnimation() {
        // Fast triple pulse on click
        pulsing = true;
        Timer t = new Timer(60, null);
        int[] count = {0};
        t.addActionListener(e -> {
            count[0]++;
            repaint();
            if (count[0] > 20) { t.stop(); pulsing = false; }
        });
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth()  / 2;
        int cy = getHeight() / 2;
        int r  = SIZE / 2;

        // Pulse ring
        if (pulseAlpha > 0) {
            int pr = r + (int) pulseRadius;
            g2.setColor(new Color(210, 35, 35, (int)(pulseAlpha * 255)));
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(cx - pr, cy - pr, pr * 2, pr * 2);

            // Second ring offset
            if (pulseRadius > 20) {
                int pr2 = r + (int)(pulseRadius - 20);
                float a2 = Math.max(0f, pulseAlpha - 0.2f);
                g2.setColor(new Color(210, 35, 35, (int)(a2 * 255)));
                g2.drawOval(cx - pr2, cy - pr2, pr2 * 2, pr2 * 2);
            }
        }

        // Glow effect
        for (int i = 8; i > 0; i--) {
            int glow = hovered ? 255 : 180;
            g2.setColor(new Color(210, 35, 35, glow / (i * 2)));
            g2.fillOval(cx - r - i * 2, cy - r - i * 2, (r + i * 2) * 2, (r + i * 2) * 2);
        }

        // Main circle
        Color baseColor = hovered ? new Color(230, 50, 50) : new Color(210, 35, 35);
        GradientPaint grad = new GradientPaint(cx - r, cy - r, new Color(240, 70, 70), cx + r, cy + r, baseColor);
        g2.setPaint(grad);
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);

        // Border ring
        g2.setColor(new Color(255, 120, 120, 180));
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(cx - r + 2, cy - r + 2, (r - 2) * 2, (r - 2) * 2);

        // SOS Text
        g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String sos = "SOS";
        g2.drawString(sos, cx - fm.stringWidth(sos) / 2, cy - 8);

        // Sub text
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2.setColor(new Color(255, 200, 200));
        String sub = "EMERGENCY";
        FontMetrics fm2 = g2.getFontMetrics();
        g2.drawString(sub, cx - fm2.stringWidth(sub) / 2, cy + 14);

        // Tap hint
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        g2.setColor(new Color(255, 180, 180, 180));
        String tap = "PRESS TO ALERT";
        FontMetrics fm3 = g2.getFontMetrics();
        g2.drawString(tap, cx - fm3.stringWidth(tap) / 2, cy + 30);

        g2.dispose();
    }
}
