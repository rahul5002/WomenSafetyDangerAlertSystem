package com.womensafety.ui.panels;

import com.womensafety.model.Alert;
import com.womensafety.ui.SwingApp;
import com.womensafety.ui.components.AppTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AlertPanel extends JPanel {

    private final SwingApp app;
    private DefaultTableModel tableModel;

    public AlertPanel(SwingApp app) {
        this.app = app;
        setLayout(new BorderLayout(20, 20));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        buildHeader();
        buildContent();
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(AppTheme.title("Alert History"), BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        String[] cols = {"Alert ID", "Date", "Type", "Priority", "Status", "Message"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(AppTheme.FONT_BODY);
        // Message column wider
        table.getColumnModel().getColumn(5).setPreferredWidth(300);
        
        add(AppTheme.scrollPane(table), BorderLayout.CENTER);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Alert> alerts = app.getAlertService().getAlertHistory();
        for (int i = alerts.size() - 1; i >= 0; i--) { // Reverse chronological
            Alert a = alerts.get(i);
            tableModel.addRow(new Object[]{
                a.getAlertId(),
                a.getCreatedAt().toLocalDate().toString() + " " + a.getCreatedAt().toLocalTime().withNano(0).toString(),
                a.getAlertType().getDisplayName(),
                a.getPriority().name(),
                a.getStatus().name(),
                a.getMessage()
            });
        }
    }
}
