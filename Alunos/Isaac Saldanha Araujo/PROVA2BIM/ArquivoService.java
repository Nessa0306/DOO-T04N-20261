import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArquivoService {

    private final String CAMINHO_ARQUIVO = "dados_usuario.json";

    public void salvarUsuario(Usuario usuario) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            writer.write("{\n");
            writer.write("\"nome\":\"" + usuario.getNome() + "\",\n");

            escreverListaJson(writer, "favoritos", usuario.getFavoritos());
            writer.write(",\n");
            escreverListaJson(writer, "assistidas", usuario.getAssistidas());
            writer.write(",\n");
            escreverListaJson(writer, "desejoAssistir", usuario.getDesejoAssistir());
            
            writer.write("\n}");
        } catch (Exception e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    private void escreverListaJson(BufferedWriter writer, String nomeLista, java.util.List<Serie> lista) throws Exception {
        writer.write("\"" + nomeLista + "\":[");
        for (int i = 0; i < lista.size(); i++) {
            writer.write(lista.get(i).toJson());
            if (i < lista.size() - 1) writer.write(",");
        }
        writer.write("]");
    }

    public Usuario carregarUsuario(String nomePadrao) {
        Usuario usuario = new Usuario(nomePadrao);
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) sb.append(linha.trim());
            String json = sb.toString();

            carregarListaDoJson(json, "favoritos", usuario.getFavoritos());
            carregarListaDoJson(json, "assistidas", usuario.getAssistidas());
            carregarListaDoJson(json, "desejoAssistir", usuario.getDesejoAssistir());
            
        } catch (Exception e) {
            System.out.println("Iniciando com perfil limpo. Erro de leitura ou primeiro acesso.");
        }
        return usuario;
    }

    private void carregarListaDoJson(String json, String arrayName, java.util.List<Serie> destino) {
        Matcher arrayMatcher = Pattern.compile("\"" + arrayName + "\":\\[(.*?)\\](,|\\})").matcher(json);
        if (arrayMatcher.find()) {
            String conteudoArray = arrayMatcher.group(1);
            Matcher objectMatcher = Pattern.compile("\\{([^\\}]+)\\}").matcher(conteudoArray);
            while (objectMatcher.find()) {
                String objJson = "{" + objectMatcher.group(1) + "}";
                destino.add(Serie.fromJson(objJson));
            }
        }
    }
}