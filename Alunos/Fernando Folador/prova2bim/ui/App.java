package ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Classe responsável por iniciar a aplicação+
public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

            new TelaPrincipal().setVisible(true);
        });
    }
}