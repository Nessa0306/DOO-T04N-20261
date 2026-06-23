package com.tvtracker;

import com.tvtracker.model.UserProfile;
import com.tvtracker.service.PersistenceService;
import com.tvtracker.ui.LoginDialog;
import com.tvtracker.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Dark Nimbus Look & Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                PersistenceService persistence = new PersistenceService();
                UserProfile profile = persistence.load();

                LoginDialog login = new LoginDialog(null,
                    profile != null ? profile.getUsername() : null);
                login.setVisible(true);

                String username = login.getUsername();
                if (username == null || username.isBlank()) {
                    System.exit(0);
                }

                if (profile == null) {
                    profile = new UserProfile(username);
                } else {
                    profile.setUsername(username);
                }

                final UserProfile finalProfile = profile;
                final PersistenceService finalPersistence = persistence;

                // Save updated username
                try { persistence.save(profile); }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                        "Aviso: não foi possível salvar os dados iniciais.\n" + e.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                }

                new MainFrame(finalProfile, finalPersistence);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Erro fatal ao iniciar o aplicativo:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}