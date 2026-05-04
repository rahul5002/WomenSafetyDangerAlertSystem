package com.womensafety.ui.panels;

import com.womensafety.model.User;
import com.womensafety.ui.SwingApp;
import com.womensafety.ui.components.AppTheme;
import com.womensafety.ui.components.ToastNotification;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private final SwingApp app;
    private JTextField nameF, phoneF, emailF;
    private JPasswordField passF;
    private JLabel statusLabel;

    public ProfilePanel(SwingApp app) {
        this.app = app;
        setLayout(new BorderLayout(20, 20));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        buildUI();
    }

    private void buildUI() {
        JPanel center = AppTheme.cardPanel();
        center.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        
        c.gridx=0; c.gridy=0; c.gridwidth=2;
        center.add(AppTheme.title("My Profile"), c);

        c.gridwidth=1; c.gridy=1;
        center.add(AppTheme.body("Full Name:"), c);
        c.gridx=1; nameF = AppTheme.textField(); center.add(nameF, c);

        c.gridy=2; c.gridx=0;
        center.add(AppTheme.body("Phone:"), c);
        c.gridx=1; phoneF = AppTheme.textField(); phoneF.setEditable(false); center.add(phoneF, c);

        c.gridy=3; c.gridx=0;
        center.add(AppTheme.body("Email:"), c);
        c.gridx=1; emailF = AppTheme.textField(); center.add(emailF, c);
        
        c.gridy=4; c.gridx=0;
        center.add(AppTheme.body("Current Status:"), c);
        c.gridx=1; statusLabel = AppTheme.body("Unknown"); center.add(statusLabel, c);

        c.gridy=5; c.gridx=0; c.gridwidth=2;
        JButton saveBtn = AppTheme.primaryBtn("Save Changes");
        saveBtn.addActionListener(e -> saveProfile());
        center.add(saveBtn, c);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setOpaque(false);
        wrapper.add(center);

        add(wrapper, BorderLayout.CENTER);
    }

    public void refreshData() {
        User u = app.getCurrentUser();
        if (u != null) {
            nameF.setText(u.getName());
            phoneF.setText(u.getPhone());
            emailF.setText(u.getEmail());
            statusLabel.setText(u.getStatus().name());
        }
    }

    private void saveProfile() {
        User u = app.getCurrentUser();
        if (u != null) {
            u.setName(nameF.getText());
            u.setEmail(emailF.getText());
            app.getUserService().updateLocation(u, u.getCurrentLocation()); // Triggers save
            ToastNotification.success(app.getFrame(), "Profile updated.");
            app.refreshAll();
        }
    }
}
