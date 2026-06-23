package com.tvtracker.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginDialog extends JDialog {

    private String username;
    
    // Campo customizado: Força texto visível (Preto no Branco para evitar bugs do VS Code)
    private final JTextField field = new JTextField(20) {
        @Override
        public Color getForeground() {
            return Color.BLACK;
        }
        @Override
        public Color getBackground() {
            return Color.WHITE;
        }
        @Override
        public Color getCaretColor() {
            return Color.BLACK;
        }
    };

    private static final Color BG     = new Color(23, 22, 20);
    private static final Color SURFACE = new Color(28, 27, 25);
    private static final Color TEXT   = new Color(255, 255, 255);
    private static final Color ACCENT = new Color(1, 105, 111);
    private static final Color BORDER = new Color(57, 56, 54);

    public LoginDialog(Frame parent, String existingName) {
        super(parent, "TV Tracker — Identificação", true);
        setSize(400, 270); // Ajustado levemente para acomodar as proporções
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        // CORREÇÃO: Permite fechar o programa no "X"
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        if (existingName != null) field.setText(existingName);
        buildUI();
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(24, 24, 16, 24));

        // --- Header ---
        JLabel logo = new JLabel("📺 TV Tracker");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(new Color(79, 152, 163));
        logo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel sub = new JLabel("Seu companheiro para acompanhar séries");
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        sub.setForeground(new Color(122, 121, 116));
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG);
        header.add(logo);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);
        main.add(header, BorderLayout.NORTH);

        // --- Form (CORRIGIDO: GridBagLayout impede o esmagamento vertical) ---
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(SURFACE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(16, 16, 16, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 8, 0); // Espaço abaixo do rótulo

        JLabel lbl = new JLabel("Seu nome ou apelido:");
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        form.add(lbl, gbc);

        // Configuração geométrica do campo de texto
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(260, 36)); 
        field.setMinimumSize(new Dimension(260, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8) 
        ));

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(field, gbc);
        
        main.add(form, BorderLayout.CENTER);

        // --- Buttons ---
        JButton btn = ShowDetailDialog.styledButton("Entrar →", ACCENT);
        btn.setPreferredSize(new Dimension(120, 38));
        btn.addActionListener(e -> confirm());
        field.addActionListener(e -> confirm());

        JButton btnCancel = ShowDetailDialog.styledButton("Sair", new Color(130, 40, 40));
        btnCancel.setPreferredSize(new Dimension(100, 38));
        btnCancel.addActionListener(e -> System.exit(0));

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        bot.setBackground(BG);
        bot.add(btn);
        bot.add(btnCancel);
        main.add(bot, BorderLayout.SOUTH);

        getContentPane().setBackground(BG);
        add(main);
    }

    private void confirm() {
        String name = field.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, informe seu nome ou apelido.",
                "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
            return;
        }
        username = name;
        dispose();
    }

    public String getUsername() { return username; }
}