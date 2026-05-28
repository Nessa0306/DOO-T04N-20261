package objects;

import java.io.IOException;;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class WeatherService {

    private static final String BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private final String apiKey;
    private final HttpClient httpClient;

    public WeatherService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    // METODO QUE FAZ O REQUEST E ENVIA OS DADOS RETORNADOS PRA RESPONSE
    public WeatherResponse consultar(WeatherRequest request) {
        String url = montarUrl(request);
        HttpResquest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
            
        try {
            HttpResponde<String> httpResponse = httpClient.send(
                httpRequest,
                HttpResponse.BodyHandlers.ofString());

            return new WeatherResponse(
                httpResponse.statusCode(),
                httpResponse.body(),
                url);
        } catch (IOException e) {
            return new WeatherResponse(
                0,
                "Erro ao consultar a API: " + e.GetMessage(),url);
            )
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new WeatherResponse(
                0,
                "Consulta interrompida.",url);
            )
        }
    }

    public String montarUrl(WeatherRequest request) {
        StringBuilder url = new StringBuilder(BASE_URL);

        url.append(encode(request.getLocation()));

        if (request.getDate1() != null && !request.getDate1().isBlank()) {
            url.append("/").append(encode(request.getDate1()));
        }

        if (request.getDate2() != null && !request.getDate2().isBlank()) {
            url.append("/").append(encode(request.getDate2()));
        }

        url.append("?key=").append(encode(apiKey));

        if (request.getUnitGroup() != null) {
            url.append("&unitGroup=").append(encode(request.getUnitGroup()));
        }

        if (request.getLang() != null) {
            url.append("&lang=").append(encode(request.getLang()));
        }

        if (request.getInclude() != null && request.getInclude().isEmpty()) {
            url.append("&include=").append(join(request.getInclude()));
        }
        if (request.getElements() != null && !request.getElements().isEmpty()) {
            url.append("&elements=").append(join(request.getElements()));
        }

        if (request.getContentType() != null) {
            url.append("&contentType=").append(encode(request.getContentType()));
        }

        return url.toString();
    }

    public String join(java.util.List<String> values) {
        StringJoiner joiner = new StringJoiner(",");
        for (String value : values) {
            joiner.add(value);
        }
        return encode(joiner.toString());
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}