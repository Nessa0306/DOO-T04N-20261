package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Serie;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TvMazeService {

    private static final String URL_API = "https://api.tvmaze.com/search/shows?q=";

    public List<Serie> buscarSeries(String nome) {

        List<Serie> lista = new ArrayList<>();

        if (nome == null || nome.trim().isEmpty()) {
            return lista;
        }

        HttpURLConnection conexao = null;

        try {

            URL url = new URL(URL_API + nome.trim().replace(" ", "%20"));

            conexao = (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(5000);
            conexao.setReadTimeout(5000);

            int codigoResposta = conexao.getResponseCode();

            if (codigoResposta != HttpURLConnection.HTTP_OK) {
                return lista;
            }

            try (BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(
                            conexao.getInputStream(),
                            StandardCharsets.UTF_8))) {

                StringBuilder resposta = new StringBuilder();
                String linha;

                while ((linha = leitor.readLine()) != null) {
                    resposta.append(linha);
                }

                JsonArray jsonArray = JsonParser
                        .parseString(resposta.toString())
                        .getAsJsonArray();

                for (JsonElement elemento : jsonArray) {

                    JsonObject show = elemento
                            .getAsJsonObject()
                            .getAsJsonObject("show");

                    Serie serie = new Serie();

                    serie.setId(show.get("id").getAsInt());

                    serie.setNome(
                            show.get("name").isJsonNull()
                                    ? ""
                                    : show.get("name").getAsString()
                    );

                    serie.setIdioma(
                            show.get("language").isJsonNull()
                                    ? ""
                                    : show.get("language").getAsString()
                    );

                    List<String> generos = new ArrayList<>();

                    if (show.has("genres") && !show.get("genres").isJsonNull()) {

                        JsonArray arrayGeneros = show.getAsJsonArray("genres");

                        for (JsonElement genero : arrayGeneros) {
                            generos.add(genero.getAsString());
                        }
                    }

                    serie.setGeneros(generos);

                    double nota = 0;

                    if (show.has("rating")
                            && !show.get("rating").isJsonNull()) {

                        JsonObject rating = show.getAsJsonObject("rating");

                        if (rating.has("average")
                                && !rating.get("average").isJsonNull()) {

                            nota = rating.get("average").getAsDouble();
                        }
                    }

                    serie.setNota(nota);

                    serie.setEstado(
                            show.get("status").isJsonNull()
                                    ? ""
                                    : show.get("status").getAsString()
                    );

                    serie.setDataEstreia(
                            show.get("premiered").isJsonNull()
                                    ? ""
                                    : show.get("premiered").getAsString()
                    );

                    serie.setDataTermino(
                            show.get("ended").isJsonNull()
                                    ? ""
                                    : show.get("ended").getAsString()
                    );

                    String emissora = "Não informada";

                    if (show.has("network")
                            && !show.get("network").isJsonNull()) {

                        JsonObject network = show.getAsJsonObject("network");

                        if (network.has("name")
                                && !network.get("name").isJsonNull()) {

                            emissora = network.get("name").getAsString();
                        }

                    } else if (show.has("webChannel")
                            && !show.get("webChannel").isJsonNull()) {

                        JsonObject webChannel = show.getAsJsonObject("webChannel");

                        if (webChannel.has("name")
                                && !webChannel.get("name").isJsonNull()) {

                            emissora = webChannel.get("name").getAsString();
                        }
                    }

                    serie.setEmissora(emissora);

                    lista.add(serie);
                }
            }

        } catch (Exception e) {

            System.out.println("Erro ao buscar séries: " + e.getMessage());

        } finally {

            if (conexao != null) {
                conexao.disconnect();
            }
        }

        return lista;
    }

}