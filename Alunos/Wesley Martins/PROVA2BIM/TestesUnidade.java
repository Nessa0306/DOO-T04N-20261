
/**
 * Testes unitários básicos para o sistema
 * Execute: javac -cp lib/json-20231013.jar TestesUnidade.java && java -cp .:lib/json-20231013.jar TestesUnidade
 */
public class TestesUnidade {

    private static int totalTestes = 0;
    private static int testesPassed = 0;

    public static void main(String[] args) {
        System.out.println("=== Testes Unitários - Sistema de Séries ===\n");

        testarSerie();
        testarUsuario();
        testarListasSeries();
        testarDadosPrecarga();

        System.out.println("\n=== Resultado Final ===");
        System.out.println("Testes passaram: " + testesPassed + "/" + totalTestes);

        if (testesPassed == totalTestes) {
            System.out.println("✓ Todos os testes passaram com sucesso!");
        } else {
            System.out.println("✗ Alguns testes falharam!");
        }
    }

    private static void testarSerie() {
        System.out.println("### Testando classe Serie");

        teste("Serie - Construtor", () -> {
            Serie s = new Serie(1, "Breaking Bad", "English",
                    new String[]{"Drama"}, 9.5, "Ended", null, null, "AMC", "Test", "");
            return s.getNome().equals("Breaking Bad") && s.getId() == 1;
        });

        teste("Serie - Getters", () -> {
            Serie s = new Serie();
            s.setNome("The Office");
            s.setNota(9.0);
            return s.getNome().equals("The Office") && s.getNota() == 9.0;
        });

        teste("Serie - Generos Formatado", () -> {
            Serie s = new Serie();
            s.setGeneros(new String[]{"Drama", "Comedy"});
            return s.getGenerosFormatado().contains("Drama");
        });

        teste("Serie - Equals", () -> {
            Serie s1 = new Serie(1, "Serie1", "EN", new String[]{}, 8.0, "R", null, null, "E", "", "");
            Serie s2 = new Serie(1, "SerieOutro", "PT", new String[]{}, 7.0, "E", null, null, "E2", "", "");
            return s1.equals(s2); // Mesmo ID = iguais
        });

        System.out.println();
    }

    private static void testarUsuario() {
        System.out.println("### Testando classe Usuario");

        teste("Usuario - Construtor Default", () -> {
            Usuario u = new Usuario();
            return u.getNomeOuApelido().equals("Usuário");
        });

        teste("Usuario - Construtor com Nome", () -> {
            Usuario u = new Usuario("João");
            return u.getNomeOuApelido().equals("João");
        });

        teste("Usuario - Setter", () -> {
            Usuario u = new Usuario("Maria");
            u.setNomeOuApelido("Maria Silva");
            return u.getNomeOuApelido().equals("Maria Silva");
        });

        teste("Usuario - Rejeita Nome Vazio", () -> {
            Usuario u = new Usuario("Original");
            u.setNomeOuApelido("");
            return u.getNomeOuApelido().equals("Original"); // Não muda
        });

        System.out.println();
    }

    private static void testarListasSeries() {
        System.out.println("### Testando classe ListasSeries");

        teste("ListasSeries - Adicionar Favorito", () -> {
            ListasSeries listas = new ListasSeries();
            Serie s = new Serie(1, "Test", "EN", new String[]{}, 8.0, "R", null, null, "E", "", "");
            listas.adicionarFavorito(s);
            return listas.getFavoritos().size() == 1 && listas.isFavorito(s);
        });

        teste("ListasSeries - Remover Favorito", () -> {
            ListasSeries listas = new ListasSeries();
            Serie s = new Serie(1, "Test", "EN", new String[]{}, 8.0, "R", null, null, "E", "", "");
            listas.adicionarFavorito(s);
            listas.removerFavorito(s);
            return listas.getFavoritos().size() == 0 && !listas.isFavorito(s);
        });

        teste("ListasSeries - Ja Assistida", () -> {
            ListasSeries listas = new ListasSeries();
            Serie s = new Serie(1, "Test", "EN", new String[]{}, 8.0, "R", null, null, "E", "", "");
            listas.adicionarJaAssistida(s);
            return listas.getJaAssistidas().size() == 1 && listas.isJaAssistida(s);
        });

        teste("ListasSeries - Desejo", () -> {
            ListasSeries listas = new ListasSeries();
            Serie s = new Serie(1, "Test", "EN", new String[]{}, 8.0, "R", null, null, "E", "", "");
            listas.adicionarDesejamAssistir(s);
            return listas.getDesejamAssistir().size() == 1 && listas.isDesejamAssistir(s);
        });

        teste("ListasSeries - Ordenar por Nome", () -> {
            ListasSeries listas = new ListasSeries();
            Serie s1 = new Serie(1, "Zebra", "EN", new String[]{}, 8.0, "R", null, null, "E", "", "");
            Serie s2 = new Serie(2, "Alpha", "EN", new String[]{}, 8.0, "R", null, null, "E", "", "");
            java.util.List<Serie> series = java.util.Arrays.asList(s1, s2);
            java.util.List<Serie> ordenadas = listas.ordenarPorNome(series);
            return ordenadas.get(0).getNome().equals("Alpha");
        });

        teste("ListasSeries - Ordenar por Nota", () -> {
            ListasSeries listas = new ListasSeries();
            Serie s1 = new Serie(1, "Test1", "EN", new String[]{}, 7.0, "R", null, null, "E", "", "");
            Serie s2 = new Serie(2, "Test2", "EN", new String[]{}, 9.0, "R", null, null, "E", "", "");
            java.util.List<Serie> series = java.util.Arrays.asList(s1, s2);
            java.util.List<Serie> ordenadas = listas.ordenarPorNota(series);
            return ordenadas.get(0).getNota() == 9.0;
        });

        System.out.println();
    }

    private static void testarDadosPrecarga() {
        System.out.println("### Testando DadosPrecarga");

        teste("DadosPrecarga - Obter Series", () -> {
            java.util.List<Serie> series = DadosPrecarga.obterSeriesDemonstacao();
            return series.size() == 10;
        });

        teste("DadosPrecarga - Séries com dados válidos", () -> {
            java.util.List<Serie> series = DadosPrecarga.obterSeriesDemonstacao();
            return series.stream().allMatch(s -> s.getNome() != null && !s.getNome().isEmpty());
        });

        teste("DadosPrecarga - Carregar em ListasSeries", () -> {
            ListasSeries listas = new ListasSeries();
            DadosPrecarga.carregarSeriesDemonstracao(listas);
            return listas.getTotalSeries() == 10;
        });

        System.out.println();
    }

    private static void teste(String nome, TesteCallback callback) {
        totalTestes++;
        try {
            if (callback.executar()) {
                System.out.println("✓ " + nome);
                testesPassed++;
            } else {
                System.out.println("✗ " + nome + " (assertiva falhou)");
            }
        } catch (Exception e) {
            System.out.println("✗ " + nome + " (exceção: " + e.getMessage() + ")");
        }
    }

    @FunctionalInterface
    interface TesteCallback {

        boolean executar() throws Exception;
    }
}
