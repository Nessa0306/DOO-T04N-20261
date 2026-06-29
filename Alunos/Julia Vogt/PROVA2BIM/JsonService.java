package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Usuario;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonService {

    private static final String ARQUIVO_USUARIO = "usuario.json";

    private final Gson gson;

    public JsonService() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void salvarUsuario(Usuario usuario) {

        if (usuario == null) {
            return;
        }

        try (FileWriter writer = new FileWriter(ARQUIVO_USUARIO)) {

            gson.toJson(usuario, writer);

        } catch (Exception e) {

            System.out.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    public Usuario carregarUsuario() {

        try {

            File arquivo = new File(ARQUIVO_USUARIO);

            if (!arquivo.exists()) {
                return new Usuario();
            }

            try (FileReader reader = new FileReader(arquivo)) {

                Usuario usuario = gson.fromJson(reader, Usuario.class);

                if (usuario == null) {
                    return new Usuario();
                }

                usuario.getFavoritos();
                usuario.getAssistidas();
                usuario.getDesejaAssistir();

                return usuario;
            }

        } catch (Exception e) {

            System.out.println("Erro ao carregar os dados: " + e.getMessage());

            return new Usuario();
        }
    }
}