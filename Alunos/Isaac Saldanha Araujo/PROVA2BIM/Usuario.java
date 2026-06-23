import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Usuario {

    private String nome;
    private List<Serie> favoritos;
    private List<Serie> assistidas;
    private List<Serie> desejoAssistir;

    public Usuario(String nome) {
        this.nome = nome;
        this.favoritos = new ArrayList<>();
        this.assistidas = new ArrayList<>();
        this.desejoAssistir = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public List<Serie> getFavoritos() { return favoritos; }
    public List<Serie> getAssistidas() { return assistidas; }
    public List<Serie> getDesejoAssistir() { return desejoAssistir; }

    public void adicionarFavorito(Serie serie) {
        if (favoritos.stream().noneMatch(s -> s.getNome().equals(serie.getNome()))) favoritos.add(serie);
    }
    public void removerFavorito(Serie serie) { favoritos.remove(serie); }

    public void adicionarAssistida(Serie serie) {
        if (assistidas.stream().noneMatch(s -> s.getNome().equals(serie.getNome()))) assistidas.add(serie);
    }
    public void removerAssistida(Serie serie) { assistidas.remove(serie); }

    public void adicionarDesejoAssistir(Serie serie) {
        if (desejoAssistir.stream().noneMatch(s -> s.getNome().equals(serie.getNome()))) desejoAssistir.add(serie);
    }
    public void removerDesejoAssistir(Serie serie) { desejoAssistir.remove(serie); }

    // Métodos de Ordenação (Alfabética, Nota, Estado e Estreia)
    public void ordenarPorNome(List<Serie> lista) {
        lista.sort(Comparator.comparing(Serie::getNome, String.CASE_INSENSITIVE_ORDER));
    }

    public void ordenarPorNota(List<Serie> lista) {
        lista.sort(Comparator.comparing(Serie::getNota).reversed()); // Maior nota primeiro
    }

    public void ordenarPorEstado(List<Serie> lista) {
        lista.sort(Comparator.comparing(Serie::getEstado, String.CASE_INSENSITIVE_ORDER));
    }

    public void ordenarPorEstreia(List<Serie> lista) {
        lista.sort(Comparator.comparing(Serie::getEstreia, String.CASE_INSENSITIVE_ORDER)); // Formato YYYY-MM-DD se ordena bem como string
    }
}