package main;

import javax.swing.SwingUtilities;
import view.TelaPrincipal;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new TelaPrincipal());

    }

}