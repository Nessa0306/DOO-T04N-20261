
import java.io.*;
import java.util.Properties;

/**
 * Gerenciador de configurações e credenciais de forma segura Lê de variáveis de
 * ambiente ou arquivo config.properties (não versionado)
 */
public class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";
    private static final String ENV_TVMAZE_TOKEN = "TVMAZE_TOKEN";
    private static Properties properties = new Properties();

    static {
        carregarConfiguracoes();
    }

    /**
     * Carrega configurações de arquivo ou variáveis de ambiente
     */
    private static void carregarConfiguracoes() {
        // Tenta ler do arquivo config.properties
        try {
            File file = new File(CONFIG_FILE);
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    properties.load(reader);
                    System.out.println("Configurações carregadas de: " + CONFIG_FILE);
                }
            }
        } catch (IOException e) {
            System.out.println("Arquivo config.properties não encontrado. Usando variáveis de ambiente.");
        }
    }

    /**
     * Obtém o token TVMaze de forma segura Prioridade: Variável de ambiente >
     * Arquivo config.properties
     */
    public static String obterTokenTVMaze() {
        // Primeiro tenta variável de ambiente
        String token = System.getenv(ENV_TVMAZE_TOKEN);
        if (token != null && !token.isEmpty()) {
            return token;
        }

        // Depois tenta arquivo config.properties
        token = properties.getProperty("tvmaze.token");
        if (token != null && !token.isEmpty()) {
            return token;
        }

        // Se não encontrar, retorna null (API funciona sem token, mas com limite)
        return null;
    }

    /**
     * Obtém qualquer configuração
     */
    public static String obter(String chave) {
        String valor = System.getenv(chave);
        if (valor != null) {
            return valor;
        }
        return properties.getProperty(chave);
    }

    /**
     * Verifica se há token configurado
     */
    public static boolean temTokenTVMaze() {
        return obterTokenTVMaze() != null;
    }

    /**
     * Cria arquivo de exemplo config.properties
     */
    public static void criarConfiguracaoExemplo() {
        File exampleFile = new File("config.properties.example");
        if (!exampleFile.exists()) {
            try {
                String exemplo = "# Configurações da Aplicação\n"
                        + "# Copie este arquivo para config.properties e preencha com seus dados\n"
                        + "# NÃO COMMITE config.properties no Git!\n\n"
                        + "# Token da API TVMaze (opcional, mas aumenta limite de requisições)\n"
                        + "tvmaze.token=SEU_TOKEN_AQUI\n";

                try (FileWriter writer = new FileWriter(exampleFile)) {
                    writer.write(exemplo);
                    System.out.println("Arquivo de exemplo criado: config.properties.example");
                }
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo de exemplo: " + e.getMessage());
            }
        }
    }
}
