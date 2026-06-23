package com.tvtracker.ui;

import com.tvtracker.model.Show;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ShowDetailDialog extends JDialog {

    public ShowDetailDialog(Frame parent, Show show) {
        super(parent, "Detalhes da Série — " + show.getName(), true);
        setSize(500, 460);
        setLocationRelativeTo(parent);
        setResizable(false);
        buildUI(show);
    }

    private void buildUI(Show show) {
        Color bg      = new Color(30, 27, 25);
        Color surface = new Color(28, 27, 25);
        Color border  = new Color(57, 56, 54);
        Color accent  = new Color(79, 152, 163);
        Color text    = new Color(205, 204, 202);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(bg);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(show.getName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(text);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        main.add(title, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(surface);
        info.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(border),
            new EmptyBorder(14, 16, 14, 16)
        ));

        addRow(info, "🌐 Idioma",       show.getLanguage(),          accent, text, surface);
        addRow(info, "🎭 Gêneros",      show.getGenresString(),      accent, text, surface);
        addRow(info, "⭐ Nota",
               show.getRatingAverage() > 0
                   ? String.format("%.1f / 10", show.getRatingAverage()) : "N/A",
               accent, text, surface);
        addRow(info, "📡 Estado",       translateStatus(show.getStatus()), accent, text, surface);
        addRow(info, "📅 Estreia",
               show.getPremiered() != null ? show.getPremiered() : "N/A",
               accent, text, surface);
        addRow(info, "🏁 Encerramento",
               show.getEnded() != null    ? show.getEnded()      : "Em andamento",
               accent, text, surface);
        addRow(info, "📺 Emissora",     show.getNetworkName(),       accent, text, surface);

        main.add(info, BorderLayout.CENTER);

        JButton btn = styledButton("Fechar", new Color(1, 105, 111));
        btn.addActionListener(e -> dispose());
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bot.setBackground(bg);
        bot.add(btn);
        main.add(bot, BorderLayout.SOUTH);

        getContentPane().setBackground(bg);
        add(main);
    }

    private void addRow(JPanel p, String lbl, String val,
                        Color accent, Color text, Color bg) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(bg);
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel l = new JLabel(lbl + ":");
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(accent);
        l.setPreferredSize(new Dimension(148, 22));

        JLabel v = new JLabel(val != null ? val : "N/A");
        v.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        v.setForeground(text);

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        p.add(row);
    }

    private String translateStatus(String s) {
        if (s == null) return "N/A";
        return switch (s) {
            case "Running"          -> "✅ Em andamento";
            case "Ended"            -> "🔚 Encerrada";
            case "To Be Determined" -> "⏳ A definir";
            default                 -> s;
        };
    }

    static JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(130, 34));
        return b;
    }
}
