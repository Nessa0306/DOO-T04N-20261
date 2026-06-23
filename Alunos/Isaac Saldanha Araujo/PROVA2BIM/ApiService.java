import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiService {

    public Serie buscarSerieCompleta(String nomeSerie) throws Exception {
        nomeSerie = nomeSerie.replace(" ", "%20");
        String url = "https://api.tvmaze.com/search/shows?q=" + nomeSerie;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        String json = response.body();
        if (json == null || json.equals("[]")) {
            throw new Exception("Série não encontrada.");
        }

        String nome = extrairRegex(json, "\"name\":\"([^\"]+)\"");
        String idioma = extrairRegex(json, "\"language\":\"([^\"]+)\"");
        String generos = extrairRegex(json, "\"genres\":\\[(.*?)\\]").replace("\"", "");
        
        String notaStr = extrairRegex(json, "\"rating\":\\s*\\{\\s*\"average\":\\s*([\\d.]+)");
        double nota = 0.0;
        try {
            if (!notaStr.equals("Não informado") && !notaStr.equals("null")) {
                nota = Double.parseDouble(notaStr);
            }
        } catch (Exception e) {}
        
        String estado = extrairRegex(json, "\"status\":\"([^\"]+)\"");
        String estreia = extrairRegex(json, "\"premiered\":\"([^\"]+)\"");
        String termino = extrairRegex(json, "\"ended\":\"([^\"]+)\"");
        

        String emissora = extrairRegex(json, "\"network\":\\s*\\{[^}]*?\"name\":\"([^\"]+)\"");
        if (emissora.equals("Não informado")) {
            emissora = extrairRegex(json, "\"webChannel\":\\s*\\{[^}]*?\"name\":\"([^\"]+)\""); 
        }

        return new Serie(nome, idioma, generos, nota, estado, estreia, termino, emissora);
    }

    private String extrairRegex(String texto, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(texto);
        if (matcher.find()) {
            String resultado = matcher.group(1);
            return (resultado != null && !resultado.equals("null")) ? resultado : "Não informado";
        }
        return "Não informado";
    }
}