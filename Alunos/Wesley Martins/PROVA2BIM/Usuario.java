
/**
 * Classe que representa um usuário do sistema
 */
public class Usuario {

    private String nomeOuApelido;

    public Usuario() {
        this.nomeOuApelido = "Usu\u00E1rio";
    }

    public Usuario(String nomeOuApelido) {
        this.nomeOuApelido = nomeOuApelido;
    }

    public String getNomeOuApelido() {
        return nomeOuApelido;
    }

    public void setNomeOuApelido(String nomeOuApelido) {
        if (nomeOuApelido != null && !nomeOuApelido.trim().isEmpty()) {
            this.nomeOuApelido = nomeOuApelido;
        }
    }

    @Override
    public String toString() {
        return nomeOuApelido;
    }
}
