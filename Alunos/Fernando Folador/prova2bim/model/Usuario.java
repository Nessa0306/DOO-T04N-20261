package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Usuario {

    private String nome;
    private List<Serie> favoritos;
    private List<Serie> jaAssistidas;
    private List<Serie> desejaAssistir;

    // Armazena os dados e listas do usuário do sistema
    public Usuario(String nome) {

        this.nome = nome;

        favoritos = new ArrayList<>();
        jaAssistidas = new ArrayList<>();
        desejaAssistir = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Serie> getFavoritos() {
        return favoritos;
    }

    public List<Serie> getJaAssistidas() {
        return jaAssistidas;
    }

    public List<Serie> getDesejaAssistir() {
        return desejaAssistir;
    }

    // Realiza a ordenação das listas conforme a opção selecionada pelo usuário
    public void ordenarLista(List<Serie> lista, int tipoOrdenacao) {

        switch (tipoOrdenacao) {

            // Ordenação alfabética pelo nome da série
            case 0:
                lista.sort(
                        Comparator.comparing(
                                Serie::getNome,
                                String.CASE_INSENSITIVE_ORDER
                        )
                );
                break;

            // Ordenação pela nota geral (maior para menor)
            case 1:
                lista.sort(
                        (s1, s2) ->
                                Double.compare(
                                        s2.getNotaGeral(),
                                        s1.getNotaGeral()
                                )
                );
                break;

            // Ordenação pelo estado da série
            case 2:
                lista.sort(
                        Comparator.comparing(
                                Serie::getEstado
                        )
                );
                break;

            // Ordenação pela data de estreia

            case 3:
                lista.sort(
                        Comparator.comparing(
                                Serie::getDataEstreia
                        )
                );
                break;
        }
    }
}