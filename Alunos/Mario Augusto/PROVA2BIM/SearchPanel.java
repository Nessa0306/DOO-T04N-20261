package com.tvtracker.ui;

import com.tvtracker.model.Show;
import com.tvtracker.model.UserProfile;
import com.tvtracker.service.PersistenceService;
import com.tvtracker.service.TVMazeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class SearchPanel extends JPanel {

    private final UserProfile profile;
    private final PersistenceService persistence;
    private final TVMazeService tvMaze = new TVMazeService();
    private final DefaultListModel<Show> resultModel = new DefaultListModel<>();
    private final JList<Show> resultList = new JList<>(resultModel);
    private final JTextField searchField = new JTextField();
    private final JLabel statusLabel = new JLabel(" ");

    private static final Color BG      = new Color(28, 27, 25);
    private static final Color SURFACE = new Color(32, 31, 29);
    private static final Color TEXT    = new Color(205, 204, 202);
    private static final Color MUTED   = new Color(122, 121, 116);
    private static final Color ACCENT  = new Color(1, 105, 111);
    private static final Color BORDER  = new Color(57, 56, 54);

    public SearchPanel(UserProfile profile, PersistenceService persistence) {
        this.profile = profile;
        this.persistence = persistence;
        setBackground(BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(16, 16, 16, 16));
        buildUI();
    }

    private void buildUI() {
        // Search bar
        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setBackground(BG);

        searchField.setBackground(SURFACE);
        searchField.setForeground(TEXT);
        searchField.setCaretColor(TEXT);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(6, 10, 6, 10)
        ));
        searchField.addActionListener(this::performSearch);

        JButton searchBtn = ShowDetailDialog.styledButton("🔎 Buscar", ACCENT);
        searchBtn.addActionListener(this::performSearch);

        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtn, BorderLayout.EAST);
        add(searchBar, BorderLayout.NORTH);

        // Results list
        resultList.setBackground(SURFACE);
        resultList.setForeground(TEXT);
        resultList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultList.setSelectionBackground(new Color(1, 105, 111, 80));
        resultList.setFixedCellHeight(36);
        resultList.setCellRenderer(new SearchCellRenderer());

        JScrollPane scroll = new JScrollPane(resultList);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(SURFACE);
        add(scroll, BorderLayout.CENTER);

        // Action buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(BG);

        JButton detailBtn  = ShowDetailDialog.styledButton("🔍 Detalhes",           ACCENT);
        JButton favBtn     = ShowDetailDialog.styledButton("⭐ Favoritos",           new Color(160, 130, 20));
        JButton watchedBtn = ShowDetailDialog.styledButton("✅ Já assisti",          new Color(50, 120, 40));
        JButton listBtn    = ShowDetailDialog.styledButton("📋 Quero assistir",      new Color(60, 80, 160));

        detailBtn.addActionListener(e  -> showDetail());
        favBtn.addActionListener(e     -> addTo("favorites"));
        watchedBtn.addActionListener(e -> addTo("watched"));
        listBtn.addActionListener(e    -> addTo("watchlist"));

        btnPanel.add(detailBtn);
        btnPanel.add(favBtn);
        btnPanel.add(watchedBtn);
        btnPanel.add(listBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(BG);
        bottom.add(btnPanel, BorderLayout.WEST);
        statusLabel.setForeground(MUTED);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        bottom.add(statusLabel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    private void performSearch(ActionEvent e) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Digite um nome de série para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        statusLabel.setText("Buscando...");
        resultModel.clear();

        SwingWorker<List<Show>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Show> doInBackground() throws Exception {
                return tvMaze.searchShows(query);
            }

            @Override
            protected void done() {
                try {
                    List<Show> shows = get();
                    if (shows.isEmpty()) {
                        statusLabel.setText("Nenhuma série encontrada.");
                    } else {
                        shows.forEach(resultModel::addElement);
                        statusLabel.setText(shows.size() + " resultado(s).");
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Erro na busca.");
                    JOptionPane.showMessageDialog(SearchPanel.this,
                        "Erro ao buscar séries: " + ex.getMessage(),
                        "Erro de Rede", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void showDetail() {
        Show s = resultList.getSelectedValue();
        if (s == null) { warn(); return; }
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        new ShowDetailDialog(frame, s).setVisible(true);
    }

    private void addTo(String target) {
        Show s = resultList.getSelectedValue();
        if (s == null) { warn(); return; }

        boolean added = switch (target) {
            case "favorites" -> profile.addToFavorites(s);
            case "watched"   -> profile.addToWatched(s);
            default          -> profile.addToWatchlist(s);
        };

        String listName = switch (target) {
            case "favorites" -> "Favoritos";
            case "watched"   -> "Já Assistidas";
            default          -> "Quero Assistir";
        };

        if (!added) {
            JOptionPane.showMessageDialog(this,
                "\"" + s.getName() + "\" já está em " + listName + ".",
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            persistence.save(profile);
            statusLabel.setText("\"" + s.getName() + "\" adicionada a " + listName + ".");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void warn() {
        JOptionPane.showMessageDialog(this,
            "Selecione uma série nos resultados.", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private static class SearchCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            if (value instanceof Show show) {
                String year = show.getPremiered() != null
                    ? " (" + show.getPremiered().substring(0, 4) + ")" : "";
                String rating = show.getRatingAverage() > 0
                    ? String.format(" ⭐%.1f", show.getRatingAverage()) : "";
                lbl.setText("  " + show.getName() + year + rating
                    + "  " + show.getGenresString());
            }
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            if (!isSelected) {
                lbl.setBackground(index % 2 == 0
                    ? new Color(32, 31, 29) : new Color(28, 27, 25));
                lbl.setForeground(new Color(205, 204, 202));
            }
            lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
            return lbl;
        }
    }
}