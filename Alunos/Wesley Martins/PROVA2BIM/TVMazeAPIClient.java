
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Cliente para a API TVMaze Integra com https://www.tvmaze.com/api
 */
public class TVMazeAPIClient {

    private static final String BASE_URL = "https://api.tvmaze.com";
    private static final String SEARCH_ENDPOINT = "/search/shows";
    private static final String SHOW_ENDPOINT = "/shows/";
    private static final int TIMEOUT_SECONDS = 10;

    private HttpClient httpClient;
    private String token; // Token para aumentar limite de requisições

    public TVMazeAPIClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();

        // Carrega token de forma segura (arquivo ou variável de ambiente)
        this.token = ConfigManager.obterTokenTVMaze();

        if (token != null) {
            System.out.println("TVMaze API: Token configurado. Limite de requisições aumentado.");
        } else {
            System.out.println("TVMaze API: Sem token. Limite de 20 requisições por 10 segundos.");
        }
    }

    /**
     * Constrói URL com token se disponível
     */
    private String construirURL(String endpoint) {
        if (token != null && !token.isEmpty()) {
            return BASE_URL + endpoint + (endpoint.contains("?") ? "&" : "?") + "token=" + token;
        }
        return BASE_URL + endpoint;
    }

    /**
     * Busca séries pelo nome
     */
    public List<Serie> buscarSeriesPorNome(String nomeSerie) throws IOException, InterruptedException {
        if (nomeSerie == null || nomeSerie.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da série não pode ser vazio");
        }

        String urlEncodedNome = URLEncoder.encode(nomeSerie, StandardCharsets.UTF_8);
        String endpoint = SEARCH_ENDPOINT + "?q=" + urlEncodedNome;
        String url = construirURL(endpoint);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .timeout(java.time.Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Erro na API TVMaze: Status " + response.statusCode());
        }

        return parseSeriesFromSearchResponse(response.body());
    }

    /**
     * Obtém detalhes de uma série pelo ID
     */
    public Serie obterSerieDetalhes(int serieId) throws IOException, InterruptedException {
        String endpoint = SHOW_ENDPOINT + serieId + "?embed=cast";
        String url = construirURL(endpoint);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .timeout(java.time.Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Série não encontrada: ID " + serieId);
        }

        return parseSerieFromDetailResponse(response.body());
    }

    private List<Serie> parseSeriesFromSearchResponse(String jsonResponse) {
        List<Serie> series = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            JSONObject show = item.getJSONObject("show");
            Serie serie = parseShowToSerie(show);
            series.add(serie);
        }

        return series;
    }

    private Serie parseSerieFromDetailResponse(String jsonResponse) {
        JSONObject show = new JSONObject(jsonResponse);
        return parseShowToSerie(show);
    }

    private Serie parseShowToSerie(JSONObject show) {
        int id = show.getInt("id");
        String nome = show.optString("name", "Desconhecido");
        String idioma = show.optString("language", "Desconhecido");

        String[] generos = new String[0];
        if (show.has("genres")) {
            JSONArray generosArray = show.getJSONArray("genres");
            generos = new String[generosArray.length()];
            for (int i = 0; i < generosArray.length(); i++) {
                generos[i] = generosArray.getString(i);
            }
        }

        double nota = 0.0;
        if (show.has("rating") && show.getJSONObject("rating").has("average")) {
            nota = show.getJSONObject("rating").optDouble("average", 0.0);
        }

        String estado = show.optString("status", "Desconhecido");

        LocalDate dataEstreia = null;
        if (show.has("premiered") && !show.isNull("premiered")) {
            try {
                dataEstreia = LocalDate.parse(show.getString("premiered"));
            } catch (Exception e) {
                // Data inválida, deixa null
            }
        }

        LocalDate dataTermino = null;
        if (show.has("ended") && !show.isNull("ended")) {
            try {
                dataTermino = LocalDate.parse(show.getString("ended"));
            } catch (Exception e) {
                // Data inválida, deixa null
            }
        }

        String emissora = "Desconhecido";
        if (show.has("network") && !show.isNull("network")) {
            emissora = show.getJSONObject("network").optString("name", "Desconhecido");
        } else if (show.has("webChannel") && !show.isNull("webChannel")) {
            emissora = show.getJSONObject("webChannel").optString("name", "Desconhecido");
        }

        String resumo = show.optString("summary", "Sem descrição disponível");
        // Remove tags HTML do resumo
        resumo = resumo.replaceAll("<[^>]*>", "");

        String imagemUrl = "";
        if (show.has("image") && !show.isNull("image")) {
            imagemUrl = show.getJSONObject("image").optString("original", "");
        }

        return new Serie(id, nome, idioma, generos, nota, estado, dataEstreia,
                dataTermino, emissora, resumo, imagemUrl);
    }
}
