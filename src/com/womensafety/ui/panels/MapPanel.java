package com.womensafety.ui.panels;

import com.womensafety.model.DangerZone;
import com.womensafety.ui.SwingApp;
import com.womensafety.ui.components.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class MapPanel extends JPanel {

    private final SwingApp app;
    private final MapCanvas canvas;
    private List<DangerZone> zones = new ArrayList<>();

    public MapPanel(SwingApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_DARK);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        header.add(AppTheme.title("Crime Heatmap"), BorderLayout.WEST);

        // Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        legend.setOpaque(false);
        legend.add(createLegendItem("Watch", new Color(230, 175, 10, 150)));
        legend.add(createLegendItem("Warning", new Color(230, 95, 20, 150)));
        legend.add(createLegendItem("Danger", new Color(210, 35, 35, 150)));
        legend.add(createLegendItem("Extreme", new Color(130, 10, 10, 180)));
        header.add(legend, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        canvas = new MapCanvas();
        add(canvas, BorderLayout.CENTER);
    }

    private JPanel createLegendItem(String label, Color c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JPanel colorBox = new JPanel();
        colorBox.setBackground(c);
        colorBox.setPreferredSize(new Dimension(16, 16));
        p.add(colorBox);
        p.add(AppTheme.muted(label));
        return p;
    }

    public void refreshData() {
        this.zones = app.getCrimeDataService().getAllActiveDangerZones();
        canvas.repaint();
    }

    private class MapCanvas extends JPanel {
        private DangerZone hoveredZone = null;
        private int mouseX, mouseY;

        public MapCanvas() {
            setOpaque(false);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    mouseX = e.getX(); mouseY = e.getY();
                    checkHover();
                }
            });
        }

        private void checkHover() {
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            
            hoveredZone = null;
            // Simple mockup projection
            int idx = 0;
            for (DangerZone z : zones) {
                // Mock positions just for visual demo
                int x = cx + (idx * 50) - 100;
                int y = cy + ((idx % 2 == 0) ? -40 : 40);
                int r = (int)(z.getRadiusKm() * 30); // Mock radius scaling
                
                if (Point.distance(mouseX, mouseY, x, y) <= r) {
                    hoveredZone = z;
                    break;
                }
                idx++;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw grid (mock map background)
            g2.setColor(AppTheme.BORDER);
            for (int i = 0; i < getWidth(); i += 40) g2.drawLine(i, 0, i, getHeight());
            for (int j = 0; j < getHeight(); j += 40) g2.drawLine(0, j, getWidth(), j);

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            int idx = 0;
            for (DangerZone z : zones) {
                int x = cx + (idx * 50) - 100;
                int y = cy + ((idx % 2 == 0) ? -40 : 40);
                int r = (int)(z.getRadiusKm() * 30);

                Color zoneColor;
                switch (z.getDangerLevel()) {
                    case EXTREME: zoneColor = new Color(130, 10, 10, 150); break;
                    case DANGER:  zoneColor = new Color(210, 35, 35, 150); break;
                    case WARNING: zoneColor = new Color(230, 95, 20, 150); break;
                    default:      zoneColor = new Color(230, 175, 10, 150); break;
                }

                if (z == hoveredZone) {
                    zoneColor = new Color(zoneColor.getRed(), zoneColor.getGreen(), zoneColor.getBlue(), 200);
                    g2.setStroke(new BasicStroke(2f));
                    g2.setColor(Color.WHITE);
                    g2.drawOval(x - r, y - r, r*2, r*2);
                }

                g2.setColor(zoneColor);
                g2.fillOval(x - r, y - r, r * 2, r * 2);
                
                // Pin center
                g2.setColor(Color.WHITE);
                g2.fillOval(x - 3, y - 3, 6, 6);
                
                idx++;
            }

            // Draw tooltip if hovered
            if (hoveredZone != null) {
                g2.setColor(new Color(20, 20, 40, 230));
                g2.fillRoundRect(mouseX + 10, mouseY + 10, 200, 70, 8, 8);
                g2.setColor(AppTheme.BORDER);
                g2.drawRoundRect(mouseX + 10, mouseY + 10, 200, 70, 8, 8);
                
                g2.setColor(Color.WHITE);
                g2.setFont(AppTheme.FONT_BODY_BOLD);
                g2.drawString(hoveredZone.getZoneName(), mouseX + 20, mouseY + 30);
                
                g2.setColor(AppTheme.TEXT_SECONDARY);
                g2.setFont(AppTheme.FONT_SMALL);
                g2.drawString("Level: " + hoveredZone.getDangerLevel().name(), mouseX + 20, mouseY + 50);
                g2.drawString("Reports: " + hoveredZone.getTotalReportsCount(), mouseX + 20, mouseY + 65);
            }

            g2.dispose();
        }
    }
}
