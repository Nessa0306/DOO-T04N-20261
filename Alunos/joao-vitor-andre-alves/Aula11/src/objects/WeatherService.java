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
        String url = montarUrl(request;
            
        )
    }

}