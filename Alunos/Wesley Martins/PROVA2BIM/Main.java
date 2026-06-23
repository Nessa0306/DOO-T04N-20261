
/**
 * Sistema de Acompanhamento de Séries de TV
 * Trabalho individual para a disciplina de Desenvolvimento Orientado a Objetos
 *
 * Requisitos:
 * - OOP: Boas práticas de programação orientada a objeto
 * - UI: Interface com Java Swing
 * - Exceções: Tratamento de exceções robusto
 * - Persistência: Dados em JSON
 * - Usuário: Nome/apelido
 * - Busca: Buscar séries por nome
 * - Listas: Favoritos, Já Assistidas, Desejo Assistir
 * - Ordenação: Alfabética, Nota, Estado, Data de Estreia
 * - Informações: Nome, Idioma, Gêneros, Nota, Estado, Datas, Emissora
 */
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
