package com.womensafety.ui.components;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * AppTheme — Central design system for the Women Safety Swing UI.
 * All colors, fonts, and UI factory methods live here.
 */
public class AppTheme {

    // ─── COLORS ──────────────────────────────────────────────────────────────
    public static final Color BG_DARK        = new Color(10, 10, 20);
    public static final Color BG_CARD        = new Color(20, 20, 40);
    public static final Color BG_SIDEBAR     = new Color(14, 14, 28);
    public static final Color BG_INPUT       = new Color(28, 28, 52);
    public static final Color BG_HOVER       = new Color(35, 35, 65);

    public static final Color ACCENT_PURPLE  = new Color(120, 50, 230);
    public static final Color ACCENT_PURPLE2 = new Color(160, 100, 255);
    public static final Color ACCENT_RED     = new Color(210, 35, 35);
    public static final Color ACCENT_RED2    = new Color(255, 80, 80);
    public static final Color ACCENT_GREEN   = new Color(30, 190, 90);
    public static final Color ACCENT_ORANGE  = new Color(230, 95, 20);
    public static final Color ACCENT_YELLOW  = new Color(230, 175, 10);
    public static final Color ACCENT_CYAN    = new Color(20, 200, 220);

    public static final Color TEXT_PRIMARY   = new Color(240, 240, 255);
    public static final Color TEXT_SECONDARY = new Color(160, 160, 200);
    public static final Color TEXT_MUTED     = new Color(100, 100, 140);
    public static final Color BORDER         = new Color(50, 50, 90);
    public static final Color BORDER_BRIGHT  = new Color(80, 80, 130);

    // ─── FONTS ───────────────────────────────────────────────────────────────
    public static final Font FONT_HUGE      = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE  = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY      = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL     = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO      = new Font("Consolas", Font.PLAIN, 12);

    // ─── GLOBAL THEME APPLY ──────────────────────────────────────────────────
    public static void applyGlobalTheme() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        UIManager.put("Panel.background",             BG_DARK);
        UIManager.put("Label.foreground",             TEXT_PRIMARY);
        UIManager.put("Button.background",            ACCENT_PURPLE);
        UIManager.put("Button.foreground",            TEXT_PRIMARY);
        UIManager.put("TextField.background",         BG_INPUT);
        UIManager.put("TextField.foreground",         TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground",    TEXT_PRIMARY);
        UIManager.put("PasswordField.background",     BG_INPUT);
        UIManager.put("PasswordField.foreground",     TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground",TEXT_PRIMARY);
        UIManager.put("ComboBox.background",          BG_INPUT);
        UIManager.put("ComboBox.foreground",          TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", ACCENT_PURPLE);
        UIManager.put("Table.background",             BG_CARD);
        UIManager.put("Table.foreground",             TEXT_PRIMARY);
        UIManager.put("Table.gridColor",              BORDER);
        UIManager.put("Table.selectionBackground",    ACCENT_PURPLE);
        UIManager.put("Table.selectionForeground",    TEXT_PRIMARY);
        UIManager.put("TableHeader.background",       BG_SIDEBAR);
        UIManager.put("TableHeader.foreground",       ACCENT_PURPLE2);
        UIManager.put("ScrollPane.background",        BG_DARK);
        UIManager.put("ScrollBar.background",         BG_CARD);
        UIManager.put("ScrollBar.thumb",              BORDER);
        UIManager.put("ScrollBar.track",              BG_CARD);
        UIManager.put("OptionPane.background",        BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("TextArea.background",          BG_INPUT);
        UIManager.put("TextArea.foreground",          TEXT_PRIMARY);
        UIManager.put("TextArea.caretForeground",     TEXT_PRIMARY);
        UIManager.put("CheckBox.background",          BG_DARK);
        UIManager.put("CheckBox.foreground",          TEXT_PRIMARY);
        UIManager.put("RadioButton.background",       BG_DARK);
        UIManager.put("RadioButton.foreground",       TEXT_PRIMARY);
        UIManager.put("TabbedPane.background",        BG_DARK);
        UIManager.put("TabbedPane.foreground",        TEXT_PRIMARY);
        UIManager.put("TabbedPane.selected",          BG_CARD);
        UIManager.put("List.background",              BG_CARD);
        UIManager.put("List.foreground",              TEXT_PRIMARY);
        UIManager.put("List.selectionBackground",     ACCENT_PURPLE);
        UIManager.put("Separator.foreground",         BORDER);
        UIManager.put("ToolTip.background",           BG_CARD);
        UIManager.put("ToolTip.foreground",           TEXT_PRIMARY);
        UIManager.put("ToolTip.border",               BorderFactory.createLineBorder(BORDER));
    }

    // ─── FACTORY: PANELS ─────────────────────────────────────────────────────
    public static JPanel darkPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_DARK);
        p.setOpaque(true);
        return p;
    }

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        return p;
    }

    // ─── FACTORY: BUTTONS ────────────────────────────────────────────────────
    public static JButton primaryBtn(String text) {
        return styledBtn(text, ACCENT_PURPLE, new Color(100, 30, 200), TEXT_PRIMARY);
    }

    public static JButton dangerBtn(String text) {
        return styledBtn(text, ACCENT_RED, new Color(180, 20, 20), TEXT_PRIMARY);
    }

    public static JButton successBtn(String text) {
        return styledBtn(text, ACCENT_GREEN, new Color(20, 150, 60), TEXT_PRIMARY);
    }

    public static JButton ghostBtn(String text) {
        return styledBtn(text, BG_HOVER, BG_CARD, TEXT_SECONDARY);
    }

    private static JButton styledBtn(String text, Color base, Color pressed, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? pressed :
                          getModel().isRollover() ? base.brighter() : base;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(fg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 38));
        return btn;
    }

    // ─── FACTORY: FIELDS ─────────────────────────────────────────────────────
    public static JTextField textField() {
        JTextField f = new JTextField();
        styleInputField(f);
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        styleInputField(f);
        return f;
    }

    public static JTextArea textArea(int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setBackground(BG_INPUT);
        ta.setForeground(TEXT_PRIMARY);
        ta.setCaretColor(TEXT_PRIMARY);
        ta.setFont(FONT_BODY);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return ta;
    }

    private static void styleInputField(JTextComponent f) {
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        f.setPreferredSize(new Dimension(220, 40));
    }

    // ─── FACTORY: LABELS ─────────────────────────────────────────────────────
    public static JLabel label(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }

    public static JLabel title(String t)    { return label(t, FONT_TITLE,    TEXT_PRIMARY);   }
    public static JLabel subtitle(String t) { return label(t, FONT_SUBTITLE, TEXT_SECONDARY); }
    public static JLabel body(String t)     { return label(t, FONT_BODY,     TEXT_PRIMARY);   }
    public static JLabel muted(String t)    { return label(t, FONT_SMALL,    TEXT_MUTED);     }

    // ─── FACTORY: COMBO ──────────────────────────────────────────────────────
    public static <T> JComboBox<T> comboBox(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setBackground(BG_INPUT);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_BODY);
        cb.setPreferredSize(new Dimension(220, 40));
        return cb;
    }

    // ─── FACTORY: SCROLL PANE ────────────────────────────────────────────────
    public static JScrollPane scrollPane(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBackground(BG_DARK);
        sp.getViewport().setBackground(BG_DARK);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        sp.getVerticalScrollBar().setBackground(BG_CARD);
        sp.getHorizontalScrollBar().setBackground(BG_CARD);
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = BORDER_BRIGHT; trackColor = BG_CARD;
            }
        });
        return sp;
    }

    // ─── FACTORY: SEPARATOR ──────────────────────────────────────────────────
    public static JSeparator separator() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER);
        s.setBackground(BORDER);
        return s;
    }

    // ─── BADGE / CHIP ────────────────────────────────────────────────────────
    public static JLabel badge(String text, Color bg) {
        JLabel lbl = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setOpaque(false);
        lbl.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        return lbl;
    }
}
