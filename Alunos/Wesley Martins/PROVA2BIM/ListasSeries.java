
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe que gerencia as diferentes listas de séries do usuário
 */
public class ListasSeries {

    private Set<Serie> favoritos;
    private Set<Serie> jaAssistidas;
    private Set<Serie> desejamAssistir;

    public ListasSeries() {
        this.favoritos = new HashSet<>();
        this.jaAssistidas = new HashSet<>();
        this.desejamAssistir = new HashSet<>();
    }

    // Métodos para Favoritos
    public void adicionarFavorito(Serie serie) {
        if (serie != null) {
            favoritos.add(serie);
        }
    }

    public void removerFavorito(Serie serie) {
        if (serie != null) {
            favoritos.remove(serie);
        }
    }

    public boolean isFavorito(Serie serie) {
        return serie != null && favoritos.contains(serie);
    }

    public List<Serie> getFavoritos() {
        return new ArrayList<>(favoritos);
    }

    // Métodos para Já Assistidas
    public void adicionarJaAssistida(Serie serie) {
        if (serie != null) {
            jaAssistidas.add(serie);
            desejamAssistir.remove(serie);
        }
    }

    public void removerJaAssistida(Serie serie) {
        if (serie != null) {
            jaAssistidas.remove(serie);
        }
    }

    public boolean isJaAssistida(Serie serie) {
        return serie != null && jaAssistidas.contains(serie);
    }

    public List<Serie> getJaAssistidas() {
        return new ArrayList<>(jaAssistidas);
    }

    // Métodos para Deseja Assistir
    public void adicionarDesejamAssistir(Serie serie) {
        if (serie != null) {
            desejamAssistir.add(serie);
            jaAssistidas.remove(serie);
        }
    }

    public void removerDesejamAssistir(Serie serie) {
        if (serie != null) {
            desejamAssistir.remove(serie);
        }
    }

    public boolean isDesejamAssistir(Serie serie) {
        return serie != null && desejamAssistir.contains(serie);
    }

    public List<Serie> getDesejamAssistir() {
        return new ArrayList<>(desejamAssistir);
    }

    // Métodos de Ordenação
    public List<Serie> ordenarPorNome(List<Serie> series) {
        return series.stream()
                .sorted(Comparator.comparing(Serie::getNome))
                .collect(Collectors.toList());
    }

    public List<Serie> ordenarPorNota(List<Serie> series) {
        return series.stream()
                .sorted(Comparator.comparingDouble(Serie::getNota).reversed())
                .collect(Collectors.toList());
    }

    public List<Serie> ordenarPorEstado(List<Serie> series) {
        return series.stream()
                .sorted(Comparator.comparing(Serie::getEstado))
                .collect(Collectors.toList());
    }

    public List<Serie> ordenarPorDataEstreia(List<Serie> series) {
        return series.stream()
                .sorted(Comparator.comparing(s -> s.getDataEstreia() != null ? s.getDataEstreia() : LocalDate.MIN))
                .collect(Collectors.toList());
    }

    public int getTotalSeries() {
        return favoritos.size() + jaAssistidas.size() + desejamAssistir.size();
    }
}
