
import java.io.File;
import java.io.IOException;

/**
 * Gerenciador de inicialização do sistema
 */
public class InicializadorSistema {

    private static final String DATA_DIR = "dados";
    private static final String SERIES_FILE = "series.json";

    /**
     * Inicializa o sistema, carregando dados pré-carregados se necessário
     */
    public static ListasSeries inicializar() throws IOException {
        File dataDir = new File(DATA_DIR);
        File seriesFile = new File(dataDir, SERIES_FILE);

        ListasSeries listas;

        // Se é a primeira execução (sem arquivo de séries), carrega dados de demonstração
        if (!seriesFile.exists()) {
            System.out.println("Primeira execução detectada. Carregando dados pré-carregados...");
            listas = new ListasSeries();
            DadosPrecarga.carregarSeriesDemonstracao(listas);

            // Salva os dados para futuras execuções
            PersistenceManager.salvarSeries(listas);
        } else {
            // Carrega dados existentes
            listas = PersistenceManager.carregarSeries();
        }

        return listas;
    }

    /**
     * Reseta o sistema para os dados de demonstração
     */
    public static ListasSeries resetarParaDados() throws IOException {
        ListasSeries listas = new ListasSeries();
        DadosPrecarga.carregarSeriesDemonstracao(listas);
        PersistenceManager.salvarSeries(listas);
        return listas;
    }
}
