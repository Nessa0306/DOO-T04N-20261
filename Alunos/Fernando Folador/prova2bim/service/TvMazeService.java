package service;

import model.Serie;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Classe responsável pela comunicação com a API TVMaze
public class TvMazeService {

    // Cliente responsável pelas requisições HTTP
    private static final HttpClient client = HttpClient.newHttpClient();

    // Busca séries pelo nome utilizando a API do TVMaze
    public static List<Serie> buscarSeries(String nomeSerie) {

        List<Serie> resultado = new ArrayList<>();

        try {

            // Monta a URL da API com o nome informado pelo usuário
            String url =
                    "https://api.tvmaze.com/search/shows?q="
                            + URLEncoder.encode(nomeSerie, StandardCharsets.UTF_8);

            // Envia a requisição HTTP para a API e recebe a resposta em JSON
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                resultado = converterJson(response.body());
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar séries.");
            e.printStackTrace();
        }

        return resultado;
    }

    // Converte o JSON recebido da API em objetos Serie
    private static List<Serie> converterJson(String json) {

        List<Serie> lista = new ArrayList<>();

        String[] blocos = json.split("\\{\"score\":");

        for (String bloco : blocos) {

            if (!bloco.contains("\"show\":")) {
                continue;
            }

            String nome =
                    extrair(bloco,
                            "\"name\"\\s*:\\s*\"([^\"]+)\"");

            String idioma =
                    extrair(bloco,
                            "\"language\"\\s*:\\s*\"([^\"]+)\"");

            String estado =
                    extrair(bloco,
                            "\"status\"\\s*:\\s*\"([^\"]+)\"");

            String dataEstreia =
                    extrair(bloco,
                            "\"premiered\"\\s*:\\s*\"([^\"]+)\"");

            String dataTermino =
                    extrair(bloco,
                            "\"ended\"\\s*:\\s*\"([^\"]+)\"");

            String emissora =
                    extrair(bloco,
                            "\"network\"\\s*:\\s*\\{[^}]*\"name\"\\s*:\\s*\"([^\"]+)\"");

            double notaGeral = 0;

            String nota =
                    extrair(bloco,
                            "\"average\"\\s*:\\s*([0-9.]+)");

            try {

                if (!nota.equals("N/A")) {
                    notaGeral = Double.parseDouble(nota);
                }

            } catch (Exception ignored) {
            }

            List<String> generos = new ArrayList<>();

            // Extrai a lista de gêneros retornada pela API
            Pattern patternGeneros =
                    Pattern.compile("\"genres\"\\s*:\\s*\\[([^\\]]*)\\]");

            Matcher matcherGeneros =
                    patternGeneros.matcher(bloco);

            if (matcherGeneros.find()) {

                Matcher genero =
                        Pattern.compile("\"([^\"]+)\"")
                                .matcher(matcherGeneros.group(1));

                while (genero.find()) {
                    generos.add(genero.group(1));
                }
            }

            Serie serie =
                    new Serie(
                            nome,
                            idioma,
                            generos,
                            notaGeral,
                            estado,
                            dataEstreia,
                            dataTermino,
                            emissora
                    );

            lista.add(serie);
        }

        return lista;
    }

    // Extrai informações específicas utilizando Regex
    private static String extrair(String texto, String regex) {

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "N/A";
    }
}