package com.tvtracker.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvtracker.model.SearchResult;
import com.tvtracker.model.Show;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TVMazeService {
    private static final String BASE_URL = "https://api.tvmaze.com";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Show> searchShows(String query) throws IOException {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String json = fetchJson(BASE_URL + "/search/shows?q=" + encoded);
        List<SearchResult> results = mapper.readValue(json,
                new TypeReference<List<SearchResult>>() {});
        return results.stream()
                .map(SearchResult::getShow)
                .filter(s -> s != null)
                .collect(Collectors.toList());
    }

    private String fetchJson(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);
        conn.setRequestProperty("User-Agent", "TVTracker/1.0");
        int code = conn.getResponseCode();
        if (code != 200) throw new IOException("HTTP erro: " + code);
        try (var in = conn.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    // CORREÇÃO: Implementação do método que a sua interface gráfica chama
    public List<Show> buscarSeries(String query) {
        try {
            return searchShows(query);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList(); // Retorna lista vazia em caso de falha na rede
        }
    }
}