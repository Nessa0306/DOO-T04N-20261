package com.tvtracker.ui;

import com.tvtracker.model.Show;
import com.tvtracker.model.UserProfile;
import com.tvtracker.service.PersistenceService;
import com.tvtracker.util.ShowComparators;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShowListPanel extends JPanel {

    public enum ListType { FAVORITES, WATCHED, WATCHLIST }

    private final UserProfile profile;
    private final PersistenceService persistence;
    private final ListType listType;
    private final DefaultListModel<Show> model = new DefaultListModel<>();
    private final JList<Show> list = new JList<>(model);

    private static final Color BG      = new Color(28, 27, 25);
    private static final Color SURFACE = new Color(32, 31, 29);
    private static final Color TEXT    = new Color(205, 204, 202);
    private static final Color MUTED   = new Color(122, 121, 116);
    private static final Color ACCENT  = new Color(1, 105, 111);
    private static final Color BORDER  = new Color(57, 56, 54);

    public ShowListPanel(UserProfile profile, PersistenceService persistence, ListType type) {
        this.profile = profile;
        this.persistence = persistence;
        this.listType = type;
        setBackground(BG);
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(16, 16, 16, 16));
        buildUI();
        refresh();
    }

    private void buildUI() {
        // Sort toolbar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setBackground(BG);

        JLabel sortLabel = new JLabel("Ordenar por:");
        sortLabel.setForeground(MUTED);
        sortLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        top.add(sortLabel);

        String[] sortOptions = {"Nome", "Nota", "Estado", "Estreia"};
        
        // CORREÇÃO VISUAL: Força o ComboBox do Nimbus a renderizar textos legíveis
        JComboBox<String> sortBox = new JComboBox<>(sortOptions) {
            @Override
            public Color getForeground() {
                return Color.BLACK; // Texto preto nas opções para contraste imediato
            }
        };
        sortBox.setBackground(Color.WHITE);
        sortBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sortBox.addActionListener(e -> sortList(sortBox.getSelectedIndex()));
        top.add(sortBox);

        add(top, BorderLayout.NORTH);

        // List
        list.setBackground(SURFACE);
        list.setForeground(TEXT);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setSelectionBackground(new Color(1, 105, 111, 120)); // Aumentado opacidade para destacar melhor a seleção
        list.setSelectionForeground(Color.WHITE);
        list.setFixedCellHeight(36);
        list.setCellRenderer(new ShowCellRenderer());

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(SURFACE);
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(BG);

        JButton detailBtn = ShowDetailDialog.styledButton("🔍 Detalhes", ACCENT);
        JButton removeBtn = ShowDetailDialog.styledButton("🗑 Remover", new Color(120, 43, 51));

        detailBtn.addActionListener(e -> showDetail());
        removeBtn.addActionListener(e -> removeSelected());

        btnPanel.add(detailBtn);
        btnPanel.add(removeBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void sortList(int idx) {
        List<Show> shows = getCurrentList();
        Comparator<Show> cmp = switch (idx) {
            case 1  -> ShowComparators.byRating();
            case 2  -> ShowComparators.byStatus();
            case 3  -> ShowComparators.byPremiered();
            default -> ShowComparators.byName();
        };
        shows.sort(cmp);
        model.clear();
        shows.forEach(model::addElement);
    }

    private void showDetail() {
        Show selected = list.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma série.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        new ShowDetailDialog(frame, selected).setVisible(true);
    }

    private void removeSelected() {
        Show selected = list.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma série para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Remover \"" + selected.getName() + "\" desta lista?",
            "Confirmar remoção", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        switch (listType) {
            case FAVORITES -> profile.removeFromFavorites(selected);
            case WATCHED   -> profile.removeFromWatched(selected);
            case WATCHLIST -> profile.removeFromWatchlist(selected);
        }
        saveAndRefresh();
    }

    public void refresh() {
        model.clear();
        getCurrentList().forEach(model::addElement);
    }

    private List<Show> getCurrentList() {
        return switch (listType) {
            case FAVORITES -> new ArrayList<>(profile.getFavorites());
            case WATCHED   -> new ArrayList<>(profile.getWatched());
            case WATCHLIST -> new ArrayList<>(profile.getWatchlist());
        };
    }

    private void saveAndRefresh() {
        try {
            persistence.save(profile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        refresh();
    }

    // Custom renderer
    private static class ShowCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            if (value instanceof Show show) {
                String rating = show.getRatingAverage() > 0
                    ? String.format("⭐%.1f", show.getRatingAverage()) : "⭐N/A";
                String status = statusEmoji(show.getStatus());
                lbl.setText(String.format("  %s  %s  %s", show.getName(), rating, status));
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

        private String statusEmoji(String s) {
            if (s == null) return "";
            return switch (s) {
                case "Running"          -> "🟢";
                case "Ended"            -> "🔴";
                case "To Be Determined" -> "🟡";
                default                 -> "⚪";
            };
        }
    }
}