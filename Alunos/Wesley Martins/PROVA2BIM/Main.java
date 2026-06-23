
public class Main {

    public static void main(String[] args) {
        try {
            // Carrega configurações de segurança
            System.out.println("Carregando configurações...");
            ConfigManager.criarConfiguracaoExemplo();

            String token = ConfigManager.obterTokenTVMaze();
            if (token != null) {
                System.out.println("✓ Token TVMaze carregado com sucesso");
            } else {
                System.out.println("ℹ Nenhum token configurado. Para aumentar limite de requisições,");
                System.out.println("  configure TVMAZE_TOKEN em variável de ambiente ou em config.properties");
            }

            System.out.println();

            javax.swing.SwingUtilities.invokeLater(() -> {
                TelaPrincipal frame = new TelaPrincipal();
                frame.setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Erro ao inicializar a aplicação:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
