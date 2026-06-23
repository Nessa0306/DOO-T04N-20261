import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Serie {

    private String nome;
    private String idioma;
    private String generos;
    private double nota;
    private String estado;
    private String estreia;
    private String termino;
    private String emissora;

    public Serie(String nome, String idioma, String generos, double nota, 
                 String estado, String estreia, String termino, String emissora) {
        this.nome = nome;
        this.idioma = idioma;
        this.generos = generos;
        this.nota = nota;
        this.estado = estado;
        this.estreia = estreia;
        this.termino = termino;
        this.emissora = emissora;
    }

    public String getNome() { return nome; }
    public String getIdioma() { return idioma; }
    public String getGeneros() { return generos; }
    public double getNota() { return nota; }
    public String getEstado() { return estado; }
    public String getEstreia() { return estreia; }
    public String getTermino() { return termino; }
    public String getEmissora() { return emissora; }

    // Converte o objeto para um formato JSON seguro e limpo
    public String toJson() {
        return String.format(
            "{\"nome\":\"%s\",\"idioma\":\"%s\",\"generos\":\"%s\",\"nota\":%s,\"estado\":\"%s\",\"estreia\":\"%s\",\"termino\":\"%s\",\"emissora\":\"%s\"}",
            nome.replace("\"", "\\\""), idioma, generos, nota, estado, estreia, termino, emissora
        );
    }

    // Cria um objeto Serie a partir de uma linha JSON
    public static Serie fromJson(String json) {
        String nome = extrairRegex(json, "\"nome\":\"(.*?)\"");
        String idioma = extrairRegex(json, "\"idioma\":\"(.*?)\"");
        String generos = extrairRegex(json, "\"generos\":\"(.*?)\"");
        String notaStr = extrairRegex(json, "\"nota\":([\\d.]+)");
        double nota = notaStr.equals("Não informado") ? 0.0 : Double.parseDouble(notaStr);
        String estado = extrairRegex(json, "\"estado\":\"(.*?)\"");
        String estreia = extrairRegex(json, "\"estreia\":\"(.*?)\"");
        String termino = extrairRegex(json, "\"termino\":\"(.*?)\"");
        String emissora = extrairRegex(json, "\"emissora\":\"(.*?)\"");
        
        return new Serie(nome, idioma, generos, nota, estado, estreia, termino, emissora);
    }

    private static String extrairRegex(String texto, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(texto);
        if (matcher.find()) return matcher.group(1);
        return "Não informado";
    }

    public String exibirDetalhes() {
        return "Nome: " + nome + "\nIdioma: " + idioma + 
               "\nGêneros: " + generos + "\nNota Geral: " + nota +
               "\nEstado: " + estado + "\nEstreia: " + estreia + 
               "\nTérmino: " + termino + "\nEmissora: " + emissora;
    }

    @Override
    public String toString() {
        return nome + " | Nota: " + nota + " | " + estado;
    }
}