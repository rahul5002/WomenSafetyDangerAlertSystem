package com.womensafety.ui.panels;

import com.womensafety.ui.SwingApp;
import com.womensafety.ui.components.AppTheme;
import com.womensafety.ui.components.ToastNotification;

import javax.swing.*;
import java.awt.*;

/**
 * LoginPanel — Dark-themed login and registration screen with tabs.
 */
public class LoginPanel extends JPanel {

    private final SwingApp app;

    // Login fields
    private JTextField loginPhone;
    private JPasswordField loginPass;

    // Register fields
    private JTextField regName, regPhone, regEmail;
    private JPasswordField regPass, regPass2;

    public LoginPanel(SwingApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_DARK);
        buildUI();
    }

    private void buildUI() {
        // ── Left hero panel ───────────────────────────────────────────────
        JPanel hero = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 10, 80), 0, getHeight(), new Color(10, 10, 20));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Decorative circles
                g2.setColor(new Color(120, 50, 230, 30));
                g2.fillOval(-60, -60, 300, 300);
                g2.setColor(new Color(210, 35, 35, 20));
                g2.fillOval(getWidth() - 150, getHeight() - 150, 300, 300);
                g2.dispose();
            }
        };
        hero.setPreferredSize(new Dimension(340, 0));
        hero.setLayout(new GridBagLayout());

        JPanel heroContent = new JPanel();
        heroContent.setOpaque(false);
        heroContent.setLayout(new BoxLayout(heroContent, BoxLayout.Y_AXIS));
        heroContent.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel shield = AppTheme.label("🛡", new Font("Segoe UI Emoji", Font.PLAIN, 72), Color.WHITE);
        shield.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = AppTheme.label("Women Safety", new Font("Segoe UI", Font.BOLD, 26), AppTheme.ACCENT_PURPLE2);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appSub = AppTheme.label("Danger Alert System", AppTheme.FONT_BODY, AppTheme.TEXT_SECONDARY);
        appSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        heroContent.add(shield);
        heroContent.add(Box.createVerticalStrut(16));
        heroContent.add(appName);
        heroContent.add(Box.createVerticalStrut(6));
        heroContent.add(appSub);
        heroContent.add(Box.createVerticalStrut(40));

        String[] features = {"🆘  One-tap SOS Emergency Alert", "🗺  Crime Hotspot Heatmap", "📊  Real-time Safety Stats", "🚔  Direct Police Notification", "☁  Weather Safety Advisor"};
        for (String f : features) {
            JLabel fl = AppTheme.label(f, AppTheme.FONT_BODY, AppTheme.TEXT_SECONDARY);
            fl.setAlignmentX(Component.CENTER_ALIGNMENT);
            fl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            heroContent.add(fl);
        }

        heroContent.add(Box.createVerticalStrut(30));
        JLabel emergency = AppTheme.badge("  Emergency: 112  ", new Color(180, 30, 30, 200));
        emergency.setAlignmentX(Component.CENTER_ALIGNMENT);
        emergency.setFont(AppTheme.FONT_BODY_BOLD);
        heroContent.add(emergency);

        hero.add(heroContent);

        // ── Right auth panel ──────────────────────────────────────────────
        JPanel authPanel = new JPanel(new GridBagLayout());
        authPanel.setBackground(AppTheme.BG_DARK);

        JTabbedPane tabs = buildTabbedPane();
        tabs.setPreferredSize(new Dimension(420, 480));
        authPanel.add(tabs);

        add(hero,      BorderLayout.WEST);
        add(authPanel, BorderLayout.CENTER);
    }

    private JTabbedPane buildTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(AppTheme.BG_CARD);
        tabs.setForeground(AppTheme.TEXT_PRIMARY);
        tabs.setFont(AppTheme.FONT_BODY_BOLD);
        tabs.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

        tabs.addTab("  Login  ",   buildLoginTab());
        tabs.addTab("  Register  ", buildRegisterTab());
        return tabs;
    }

    private JPanel buildLoginTab() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(AppTheme.BG_CARD);
        p.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 1;

        JLabel title = AppTheme.label("Welcome Back", AppTheme.FONT_TITLE, AppTheme.TEXT_PRIMARY);
        c.gridy = 0; p.add(title, c);

        JLabel sub = AppTheme.muted("Sign in to your safety account");
        c.gridy = 1; p.add(sub, c);

        JLabel sep = AppTheme.label("", AppTheme.FONT_SMALL, AppTheme.BORDER);
        c.gridy = 2; p.add(sep, c);

        addFormRow(p, c, 3, "📱 Phone Number", loginPhone = AppTheme.textField());
        loginPhone.setToolTipText("Enter 10-digit mobile number");

        addFormRow(p, c, 5, "🔒 Password", loginPass = AppTheme.passwordField());

        c.gridy = 7; c.insets = new Insets(20, 0, 6, 0);
        JButton loginBtn = AppTheme.primaryBtn("  Login  →");
        loginBtn.setPreferredSize(new Dimension(340, 44));
        loginBtn.addActionListener(e -> doLogin());
        p.add(loginBtn, c);

        c.gridy = 8; c.insets = new Insets(6, 0, 6, 0);
        JLabel hint = AppTheme.muted("No account? Click 'Register' tab above.");
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(hint, c);

        // Press Enter to login
        loginPass.addActionListener(e -> doLogin());
        return p;
    }

    private JPanel buildRegisterTab() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(AppTheme.BG_CARD);
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 5, 0);
        c.gridx = 0; c.gridy = 0;

        JLabel title = AppTheme.label("Create Account", AppTheme.FONT_TITLE, AppTheme.TEXT_PRIMARY);
        c.gridy = 0; p.add(title, c);
        JLabel sub = AppTheme.muted("Join the Women Safety Network");
        c.gridy = 1; p.add(sub, c);

        addFormRow(p, c, 2, "👤 Full Name",     regName  = AppTheme.textField());
        addFormRow(p, c, 4, "📱 Phone (10-digit)", regPhone = AppTheme.textField());
        addFormRow(p, c, 6, "📧 Email",          regEmail = AppTheme.textField());
        addFormRow(p, c, 8, "🔒 Password",       regPass  = AppTheme.passwordField());
        addFormRow(p, c, 10,"🔒 Confirm Password", regPass2 = AppTheme.passwordField());

        c.gridy = 12; c.insets = new Insets(16, 0, 6, 0);
        JButton regBtn = AppTheme.successBtn("  Create Account  ✓");
        regBtn.setPreferredSize(new Dimension(340, 44));
        regBtn.addActionListener(e -> doRegister());
        p.add(regBtn, c);

        c.gridy = 13; c.insets = new Insets(4, 0, 4, 0);
        JLabel note = AppTheme.muted("By registering you agree to our privacy policy.");
        note.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(note, c);

        return p;
    }

    private void addFormRow(JPanel p, GridBagConstraints c, int row, String labelText, JComponent field) {
        c.gridy = row; c.insets = new Insets(5, 0, 2, 0);
        JLabel lbl = AppTheme.label(labelText, AppTheme.FONT_SMALL, AppTheme.TEXT_SECONDARY);
        p.add(lbl, c);
        c.gridy = row + 1; c.insets = new Insets(0, 0, 5, 0);
        field.setPreferredSize(new Dimension(340, 40));
        p.add(field, c);
    }

    private void doLogin() {
        String phone = loginPhone.getText().trim();
        String pass  = new String(loginPass.getPassword());
        if (phone.isEmpty() || pass.isEmpty()) {
            ToastNotification.warning(app.getFrame(), "Please fill in phone and password.");
            return;
        }
        try {
            app.onLogin(phone, pass);
        } catch (Exception ex) {
            ToastNotification.error(app.getFrame(), ex.getMessage());
        }
    }

    private void doRegister() {
        String name  = regName.getText().trim();
        String phone = regPhone.getText().trim();
        String email = regEmail.getText().trim();
        String pass  = new String(regPass.getPassword());
        String pass2 = new String(regPass2.getPassword());

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            ToastNotification.warning(app.getFrame(), "Please fill in all fields."); return;
        }
        if (!pass.equals(pass2)) {
            ToastNotification.error(app.getFrame(), "Passwords do not match."); return;
        }
        try {
            app.onRegister(name, phone, email, pass);
            ToastNotification.success(app.getFrame(), "Account created! Please login.");
            // Clear fields
            regName.setText(""); regPhone.setText(""); regEmail.setText("");
            regPass.setText(""); regPass2.setText("");
        } catch (Exception ex) {
            ToastNotification.error(app.getFrame(), ex.getMessage());
        }
    }
}
