
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Gerenciador de persistência de dados em JSON
 */
public class PersistenceManager {

    private static final String DATA_DIR = "dados";
    private static final String USER_FILE = "user.json";
    private static final String SERIES_FILE = "series.json";

    static {
        // Cria o diretório de dados se não existir
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Salva as informações do usuário
     */
    public static void salvarUsuario(Usuario usuario) throws IOException {
        JSONObject json = new JSONObject();
        json.put("nome", usuario.getNomeOuApelido());

        Files.write(
                Paths.get(DATA_DIR, USER_FILE),
                json.toString(2).getBytes()
        );
    }

    /**
     * Carrega as informações do usuário
     */
    public static Usuario carregarUsuario() throws IOException {
        File file = new File(DATA_DIR, USER_FILE);
        if (!file.exists()) {
            return new Usuario();
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(content);
            String nome = json.getString("nome");
            return new Usuario(nome);
        } catch (Exception e) {
            return new Usuario();
        }
    }

    /**
     * Salva as listas de séries
     */
    public static void salvarSeries(ListasSeries listas) throws IOException {
        JSONObject json = new JSONObject();

        json.put("favoritos", seriesToJSON(listas.getFavoritos()));
        json.put("jaAssistidas", seriesToJSON(listas.getJaAssistidas()));
        json.put("desejamAssistir", seriesToJSON(listas.getDesejamAssistir()));

        Files.write(
                Paths.get(DATA_DIR, SERIES_FILE),
                json.toString(2).getBytes()
        );
    }

    /**
     * Carrega as listas de séries
     */
    public static ListasSeries carregarSeries() throws IOException {
        ListasSeries listas = new ListasSeries();
        File file = new File(DATA_DIR, SERIES_FILE);

        if (!file.exists()) {
            return listas;
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(content);

            if (json.has("favoritos")) {
                jsonToSeries(json.getJSONArray("favoritos"))
                        .forEach(listas::adicionarFavorito);
            }

            if (json.has("jaAssistidas")) {
                jsonToSeries(json.getJSONArray("jaAssistidas"))
                        .forEach(listas::adicionarJaAssistida);
            }

            if (json.has("desejamAssistir")) {
                jsonToSeries(json.getJSONArray("desejamAssistir"))
                        .forEach(listas::adicionarDesejamAssistir);
            }

            return listas;
        } catch (Exception e) {
            return listas;
        }
    }

    private static JSONArray seriesToJSON(List<Serie> series) {
        JSONArray array = new JSONArray();
        for (Serie serie : series) {
            array.put(serieToJSON(serie));
        }
        return array;
    }

    private static JSONObject serieToJSON(Serie serie) {
        JSONObject json = new JSONObject();
        json.put("id", serie.getId());
        json.put("nome", serie.getNome());
        json.put("idioma", serie.getIdioma());
        json.put("generos", new JSONArray(Arrays.asList(serie.getGeneros())));
        json.put("nota", serie.getNota());
        json.put("estado", serie.getEstado());
        json.put("dataEstreia", serie.getDataEstreia() != null ? serie.getDataEstreia().toString() : null);
        json.put("dataTermino", serie.getDataTermino() != null ? serie.getDataTermino().toString() : null);
        json.put("emissora", serie.getEmissora());
        json.put("resumo", serie.getResumo());
        json.put("imagemUrl", serie.getImagemUrl());
        return json;
    }

    private static List<Serie> jsonToSeries(JSONArray array) {
        List<Serie> series = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            series.add(jsonToSerie(array.getJSONObject(i)));
        }
        return series;
    }

    private static Serie jsonToSerie(JSONObject json) {
        Serie serie = new Serie();
        serie.setId(json.getInt("id"));
        serie.setNome(json.getString("nome"));
        serie.setIdioma(json.getString("idioma"));

        JSONArray generosArray = json.getJSONArray("generos");
        String[] generos = new String[generosArray.length()];
        for (int i = 0; i < generosArray.length(); i++) {
            generos[i] = generosArray.getString(i);
        }
        serie.setGeneros(generos);

        serie.setNota(json.getDouble("nota"));
        serie.setEstado(json.getString("estado"));

        if (json.has("dataEstreia") && !json.isNull("dataEstreia")) {
            try {
                serie.setDataEstreia(LocalDate.parse(json.getString("dataEstreia")));
            } catch (Exception ignored) {
            }
        }

        if (json.has("dataTermino") && !json.isNull("dataTermino")) {
            try {
                serie.setDataTermino(LocalDate.parse(json.getString("dataTermino")));
            } catch (Exception ignored) {
            }
        }

        serie.setEmissora(json.getString("emissora"));
        serie.setResumo(json.getString("resumo"));
        serie.setImagemUrl(json.getString("imagemUrl"));

        return serie;
    }
}
