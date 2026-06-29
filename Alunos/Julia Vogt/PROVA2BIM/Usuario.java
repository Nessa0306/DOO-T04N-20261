package model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private String apelido;
    private List<Serie> favoritos;
    private List<Serie> assistidas;
    private List<Serie> desejaAssistir;

    public Usuario() {
        favoritos = new ArrayList<>();
        assistidas = new ArrayList<>();
        desejaAssistir = new ArrayList<>();
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido == null ? "" : apelido.trim();
    }

    public List<Serie> getFavoritos() {

        if (favoritos == null) {
            favoritos = new ArrayList<>();
        }

        return favoritos;
    }

    public void setFavoritos(List<Serie> favoritos) {

        this.favoritos = favoritos == null
                ? new ArrayList<>()
                : favoritos;
    }

    public List<Serie> getAssistidas() {

        if (assistidas == null) {
            assistidas = new ArrayList<>();
        }

        return assistidas;
    }

    public void setAssistidas(List<Serie> assistidas) {

        this.assistidas = assistidas == null
                ? new ArrayList<>()
                : assistidas;
    }

    public List<Serie> getDesejaAssistir() {

        if (desejaAssistir == null) {
            desejaAssistir = new ArrayList<>();
        }

        return desejaAssistir;
    }

    public void setDesejaAssistir(List<Serie> desejaAssistir) {

        this.desejaAssistir = desejaAssistir == null
                ? new ArrayList<>()
                : desejaAssistir;
    }
}