package com.womensafety.ui.panels;

import com.womensafety.model.CrimeReport;
import com.womensafety.model.Location;
import com.womensafety.ui.SwingApp;
import com.womensafety.ui.components.AppTheme;
import com.womensafety.ui.components.ToastNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CrimePanel extends JPanel {

    private final SwingApp app;
    private DefaultTableModel tableModel;
    private JTable table;

    public CrimePanel(SwingApp app) {
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
        header.add(AppTheme.title("Crime Reports"), BorderLayout.WEST);

        JButton reportBtn = AppTheme.dangerBtn(" + Submit Report ");
        reportBtn.setPreferredSize(new Dimension(160, 35));
        reportBtn.addActionListener(e -> showReportDialog());
        header.add(reportBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        String[] cols = {"ID", "Date", "Type", "City", "Severity", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(AppTheme.FONT_BODY);
        
        JScrollPane scroll = AppTheme.scrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<CrimeReport> reports = app.getCrimeDataService().getAllReports();
        for (CrimeReport r : reports) {
            tableModel.addRow(new Object[]{
                r.getReportId(),
                r.getReportedAt().toLocalDate().toString(),
                r.getCrimeType().getDisplayName(),
                r.getLocation().getCity(),
                r.getSeverity().name(),
                r.getStatus().name()
            });
        }
    }

    private void showReportDialog() {
        JDialog d = new JDialog(app.getFrame(), "Submit Crime Report", true);
        d.setSize(400, 400);
        d.setLocationRelativeTo(app.getFrame());
        
        JPanel p = AppTheme.cardPanel();
        p.setLayout(new GridLayout(5, 2, 10, 15));

        JComboBox<CrimeReport.CrimeType> typeCombo = AppTheme.comboBox(CrimeReport.CrimeType.values());
        JComboBox<CrimeReport.Severity> sevCombo = AppTheme.comboBox(CrimeReport.Severity.values());
        JTextField descField = AppTheme.textField();

        p.add(AppTheme.body("Crime Type:")); p.add(typeCombo);
        p.add(AppTheme.body("Severity:"));   p.add(sevCombo);
        p.add(AppTheme.body("City:"));       
        JTextField cityField = AppTheme.textField(); cityField.setText("New Delhi");
        p.add(cityField);
        p.add(AppTheme.body("Description:")); p.add(descField);

        JButton submit = AppTheme.dangerBtn("Submit Report");
        submit.addActionListener(e -> {
            app.getCrimeDataService().submitReport(
                (CrimeReport.CrimeType) typeCombo.getSelectedItem(),
                new Location(28.61, 77.21, "Reported", cityField.getText(), "Dist", "State"),
                (CrimeReport.Severity) sevCombo.getSelectedItem(),
                descField.getText(),
                app.getCurrentUser() != null ? app.getCurrentUser().getUserId() : "Anonymous"
            );
            ToastNotification.success(app.getFrame(), "Report submitted successfully");
            d.dispose();
            app.refreshAll();
        });
        
        p.add(new JLabel("")); p.add(submit);
        
        d.add(p);
        d.setVisible(true);
    }
}
