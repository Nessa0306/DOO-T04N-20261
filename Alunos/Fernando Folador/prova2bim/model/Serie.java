package model;

import java.util.List;

public class Serie {

    private String nome;
    private String idioma;
    private List<String> generos;
    private double notaGeral;
    private String estado;
    private String dataEstreia;
    private String dataTermino;
    private String emissora;

    // Construtor responsável por armazenar os dados da série
    public Serie(String nome, String idioma, List<String> generos,
                 double notaGeral, String estado, String dataEstreia, String dataTermino,
                 String emissora) {

        this.nome = nome;
        this.idioma = idioma;
        this.generos = generos;
        this.notaGeral = notaGeral;
        this.estado = estado;
        this.dataEstreia = dataEstreia;
        this.dataTermino = dataTermino;
        this.emissora = emissora;
    }

    // Métodos de acesso aos atributos da série
    public String getNome() {
        return nome;
    }

    public String getIdioma() {
        return idioma;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public double getNotaGeral() {
        return notaGeral;
    }

    public String getEstado() {
        return estado;
    }

    public String getDataEstreia() {
        return dataEstreia;
    }

    public String getDataTermino() {
        return dataTermino;
    }

    public String getEmissora() {
        return emissora;
    }

    // Considera duas séries iguais quando possuem o mesmo nome
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Serie)) {
            return false;
        }

        Serie outraSerie = (Serie) obj;

        return nome.equalsIgnoreCase(outraSerie.getNome());
    }
}