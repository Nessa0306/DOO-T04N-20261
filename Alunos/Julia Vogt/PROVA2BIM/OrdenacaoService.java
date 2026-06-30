package service;

import model.Serie;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrdenacaoService {

    public void ordenarPorNome(List<Serie> lista) {

        if (lista == null || lista.isEmpty()) {
            return;
        }

        Collections.sort(lista, Comparator.comparing(Serie::getNome));
    }

    public void ordenarPorNota(List<Serie> lista) {

        if (lista == null || lista.isEmpty()) {
            return;
        }

        Collections.sort(lista,
                Comparator.comparingDouble(Serie::getNota).reversed());
    }

    public void ordenarPorEstado(List<Serie> lista) {

        if (lista == null || lista.isEmpty()) {
            return;
        }

        Collections.sort(lista,
                Comparator.comparing(Serie::getEstado));
    }

    public void ordenarPorDataEstreia(List<Serie> lista) {

        if (lista == null || lista.isEmpty()) {
            return;
        }

        Collections.sort(lista,
                Comparator.comparing(Serie::getDataEstreia));
    }
}