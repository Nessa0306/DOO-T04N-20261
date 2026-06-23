package com.tvtracker.ui;

import com.tvtracker.model.UserProfile;
import com.tvtracker.service.PersistenceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private final UserProfile profile;
    private final PersistenceService persistence;

    private ShowListPanel favPanel, watchedPanel, watchlistPanel;

    private static final Color BG     = new Color(23, 22, 20);
    private static final Color SURFACE = new Color(28, 27, 25);
    private static final Color TEXT   = new Color(255, 255, 255);
    //private static final Color ACCENT = new Color(1, 105, 111);
    private static final Color MUTED  = new Color(122, 121, 116);
    private static final Color BORDER = new Color(57, 56, 54);

    public MainFrame(UserProfile profile, PersistenceService persistence) {
        this.profile = profile;
        this.persistence = persistence;
        setTitle("📺 TV Tracker — " + profile.getUsername());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 19, 17));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            new EmptyBorder(10, 18, 10, 18)
        ));

        JLabel titleLbl = new JLabel("📺 TV Tracker");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(new Color(79, 152, 163));
        header.add(titleLbl, BorderLayout.WEST);

        JLabel userLbl = new JLabel("👤 " + profile.getUsername());
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLbl.setForeground(MUTED);
        header.add(userLbl, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(SURFACE);
        tabs.setForeground(TEXT);
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Search tab
        SearchPanel searchPanel = new SearchPanel(profile, persistence);
        tabs.addTab("🔍  Buscar Séries", searchPanel);

        // Lists tabs
        favPanel      = new ShowListPanel(profile, persistence, ShowListPanel.ListType.FAVORITES);
        watchedPanel  = new ShowListPanel(profile, persistence, ShowListPanel.ListType.WATCHED);
        watchlistPanel = new ShowListPanel(profile, persistence, ShowListPanel.ListType.WATCHLIST);

        tabs.addTab("⭐  Favoritos",          favPanel);
        tabs.addTab("✅  Já Assistidas",       watchedPanel);
        tabs.addTab("📋  Quero Assistir",      watchlistPanel);

        // Refresh lists on tab switch
        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 1) favPanel.refresh();
            else if (idx == 2) watchedPanel.refresh();
            else if (idx == 3) watchlistPanel.refresh();
        });

        add(tabs, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        statusBar.setBackground(new Color(20, 19, 17));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        JLabel hint = new JLabel("Dica: busque uma série, depois adicione às suas listas com os botões.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(90, 89, 87));
        statusBar.add(hint);
        add(statusBar, BorderLayout.SOUTH);
    }
}