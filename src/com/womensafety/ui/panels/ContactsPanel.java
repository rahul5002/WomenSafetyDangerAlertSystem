package com.womensafety.ui.panels;

import com.womensafety.model.EmergencyContact;
import com.womensafety.model.User;
import com.womensafety.ui.SwingApp;
import com.womensafety.ui.components.AppTheme;
import com.womensafety.ui.components.ToastNotification;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ContactsPanel extends JPanel {

    private final SwingApp app;
    private JPanel contactsList;

    public ContactsPanel(SwingApp app) {
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
        header.add(AppTheme.title("Emergency Contacts"), BorderLayout.WEST);

        JButton addBtn = AppTheme.primaryBtn(" + Add Contact ");
        addBtn.addActionListener(e -> showAddDialog());
        header.add(addBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        contactsList = new JPanel();
        contactsList.setLayout(new BoxLayout(contactsList, BoxLayout.Y_AXIS));
        contactsList.setBackground(AppTheme.BG_DARK);

        JScrollPane scroll = AppTheme.scrollPane(contactsList);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    public void refreshData() {
        contactsList.removeAll();
        User u = app.getCurrentUser();
        if (u != null) {
            List<EmergencyContact> contacts = u.getEmergencyContacts();
            if (contacts.isEmpty()) {
                JLabel empty = AppTheme.muted("No emergency contacts added yet.");
                empty.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 0));
                contactsList.add(empty);
            } else {
                for (EmergencyContact ec : contacts) {
                    contactsList.add(createContactCard(ec));
                    contactsList.add(Box.createVerticalStrut(10));
                }
            }
        }
        contactsList.revalidate();
        contactsList.repaint();
    }

    private JPanel createContactCard(EmergencyContact ec) {
        JPanel p = AppTheme.cardPanel();
        p.setLayout(new BorderLayout());
        p.setMaximumSize(new Dimension(800, 80));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        info.add(AppTheme.body(ec.getName() + " (" + ec.getRelationship() + ")"));
        info.add(AppTheme.muted("📞 " + ec.getPhone() + "  |  ✉ " + ec.getEmail()));
        
        JButton removeBtn = AppTheme.ghostBtn("Remove");
        removeBtn.setForeground(AppTheme.ACCENT_RED);
        removeBtn.addActionListener(e -> {
            app.getUserService().removeEmergencyContact(app.getCurrentUser(), ec.getContactId());
            ToastNotification.info(app.getFrame(), "Contact removed.");
            app.refreshAll();
        });

        p.add(info, BorderLayout.CENTER);
        p.add(removeBtn, BorderLayout.EAST);
        return p;
    }

    private void showAddDialog() {
        if (app.getCurrentUser() == null) return;
        if (app.getCurrentUser().getEmergencyContacts().size() >= 5) {
            ToastNotification.warning(app.getFrame(), "Maximum 5 contacts allowed.");
            return;
        }

        JDialog d = new JDialog(app.getFrame(), "Add Contact", true);
        d.setSize(350, 300);
        d.setLocationRelativeTo(app.getFrame());

        JPanel p = AppTheme.cardPanel();
        p.setLayout(new GridLayout(5, 2, 5, 10));

        JTextField nameF = AppTheme.textField();
        JTextField phoneF = AppTheme.textField();
        JTextField emailF = AppTheme.textField();
        JTextField relF = AppTheme.textField();

        p.add(AppTheme.body("Name:")); p.add(nameF);
        p.add(AppTheme.body("Phone:")); p.add(phoneF);
        p.add(AppTheme.body("Email:")); p.add(emailF);
        p.add(AppTheme.body("Relation:")); p.add(relF);

        JButton save = AppTheme.primaryBtn("Save");
        save.addActionListener(e -> {
            try {
                app.getUserService().addEmergencyContact(app.getCurrentUser(), 
                    nameF.getText(), phoneF.getText(), emailF.getText(), relF.getText());
                ToastNotification.success(app.getFrame(), "Contact added.");
                d.dispose();
                app.refreshAll();
            } catch (Exception ex) {
                ToastNotification.error(app.getFrame(), ex.getMessage());
            }
        });

        p.add(new JLabel("")); p.add(save);
        d.add(p);
        d.setVisible(true);
    }
}
