import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private JTextField txtBusca;
    private DefaultListModel<Serie> modeloResultados;
    private JList<Serie> listaResultados;
    private JTextArea txtDetalhes;
    
    private Usuario usuario;
    private ArquivoService arquivoService;
    private ApiService apiService;
    
    // Rastreador do estado da janela
    private List<Serie> listaAtualExibida; 
    private Serie seriePesquisada;

    public TelaPrincipal() {
        setTitle("Sistema de Séries de TV");
        setSize(850, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        arquivoService = new ArquivoService();
        apiService = new ApiService();
        usuario = arquivoService.carregarUsuario("Japa");

        // ---------- LINHA 1: BUSCA ----------
        JLabel lblTitulo = new JLabel("Buscar Série:");
        lblTitulo.setBounds(20, 20, 100, 30);
        add(lblTitulo);

        txtBusca = new JTextField();
        txtBusca.setBounds(110, 20, 400, 30);
        add(txtBusca);

        JButton btnBuscar = new JButton("Buscar na API");
        btnBuscar.setBounds(520, 20, 150, 30);
        add(btnBuscar);

        // ---------- PAINÉIS CENTRAIS ----------
        modeloResultados = new DefaultListModel<>();
        listaResultados = new JList<>(modeloResultados);
        JScrollPane scrollLista = new JScrollPane(listaResultados);
        scrollLista.setBounds(20, 70, 400, 250);
        add(scrollLista);

        txtDetalhes = new JTextArea();
        txtDetalhes.setEditable(false);
        JScrollPane scrollDetalhes = new JScrollPane(txtDetalhes);
        scrollDetalhes.setBounds(440, 70, 370, 250);
        add(scrollDetalhes);

        // ---------- LINHA 2: ADIÇÃO ----------
        JButton btnAddFav = new JButton("Add Favoritos");
        btnAddFav.setBounds(20, 330, 130, 30);
        add(btnAddFav);

        JButton btnAddAss = new JButton("Add Assistidas");
        btnAddAss.setBounds(155, 330, 130, 30);
        add(btnAddAss);

        JButton btnAddDes = new JButton("Add Desejos");
        btnAddDes.setBounds(290, 330, 130, 30);
        add(btnAddDes);

        // ---------- LINHA 3: NAVEGAÇÃO ----------
        JButton btnVerFav = new JButton("Ver Favoritos");
        btnVerFav.setBounds(20, 370, 130, 30);
        add(btnVerFav);

        JButton btnVerAss = new JButton("Ver Assistidas");
        btnVerAss.setBounds(155, 370, 130, 30);
        add(btnVerAss);

        JButton btnVerDes = new JButton("Ver Desejos");
        btnVerDes.setBounds(290, 370, 130, 30);
        add(btnVerDes);

        // ---------- LINHA 4: ORDENAÇÃO ----------
        JLabel lblOrd = new JLabel("Ordenar por:");
        lblOrd.setBounds(20, 410, 80, 30);
        add(lblOrd);

        JButton btnOrdNome = new JButton("Nome");
        btnOrdNome.setBounds(100, 410, 80, 30);
        add(btnOrdNome);

        JButton btnOrdNota = new JButton("Nota");
        btnOrdNota.setBounds(185, 410, 80, 30);
        add(btnOrdNota);

        JButton btnOrdEstado = new JButton("Estado");
        btnOrdEstado.setBounds(270, 410, 80, 30);
        add(btnOrdEstado);

        JButton btnOrdEstreia = new JButton("Estreia");
        btnOrdEstreia.setBounds(355, 410, 80, 30);
        add(btnOrdEstreia);

        // ---------- LINHA 5: REMOÇÃO ----------
        JButton btnRemover = new JButton("Remover Selecionada");
        btnRemover.setBounds(20, 460, 200, 30);
        add(btnRemover);

        // ================= AÇÕES E EVENTOS =================

        // Ao clicar em um item da lista, exibe os detalhes imediatamente
        listaResultados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                Serie selecionada = listaResultados.getSelectedValue();
                if (selecionada != null) {
                    txtDetalhes.setText(selecionada.exibirDetalhes());
                }
            }
        });

        // Buscar na API
        btnBuscar.addActionListener(e -> {
            try {
                String busca = txtBusca.getText().trim();
                if (busca.isEmpty()) return;
                
                txtDetalhes.setText("Buscando...");
                seriePesquisada = apiService.buscarSerieCompleta(busca);
                
                modeloResultados.clear();
                modeloResultados.addElement(seriePesquisada);
                listaAtualExibida = null; // Zera a visualização de listas salvas
                
                txtDetalhes.setText(seriePesquisada.exibirDetalhes());
            } catch (Exception ex) {
                txtDetalhes.setText("Erro: " + ex.getMessage());
            }
        });

        // Adições
        btnAddFav.addActionListener(e -> acaoAdicionar("favorito"));
        btnAddAss.addActionListener(e -> acaoAdicionar("assistida"));
        btnAddDes.addActionListener(e -> acaoAdicionar("desejo"));

        // Visualizações
        btnVerFav.addActionListener(e -> atualizarListaVisual(usuario.getFavoritos(), "Favoritos"));
        btnVerAss.addActionListener(e -> atualizarListaVisual(usuario.getAssistidas(), "Assistidas"));
        btnVerDes.addActionListener(e -> atualizarListaVisual(usuario.getDesejoAssistir(), "Desejos"));

        // Ordenações
        btnOrdNome.addActionListener(e -> acaoOrdenar("nome"));
        btnOrdNota.addActionListener(e -> acaoOrdenar("nota"));
        btnOrdEstado.addActionListener(e -> acaoOrdenar("estado"));
        btnOrdEstreia.addActionListener(e -> acaoOrdenar("estreia"));

        // Remoção
        btnRemover.addActionListener(e -> {
            Serie selecionada = listaResultados.getSelectedValue();
            if (selecionada != null && listaAtualExibida != null) {
                listaAtualExibida.remove(selecionada);
                arquivoService.salvarUsuario(usuario);
                atualizarListaVisual(listaAtualExibida, "Lista atualizada");
                txtDetalhes.setText("Série removida.");
            }
        });

        setVisible(true);
    }

    private void acaoAdicionar(String tipo) {
        Serie serie = listaResultados.getSelectedValue();
        if (serie == null) serie = seriePesquisada;

        if (serie != null) {
            switch (tipo) {
                case "favorito": usuario.adicionarFavorito(serie); break;
                case "assistida": usuario.adicionarAssistida(serie); break;
                case "desejo": usuario.adicionarDesejoAssistir(serie); break;
            }
            arquivoService.salvarUsuario(usuario);
            txtDetalhes.append("\n\n✓ Série adicionada a " + tipo);
        }
    }

    private void atualizarListaVisual(List<Serie> lista, String titulo) {
        listaAtualExibida = lista;
        modeloResultados.clear();
        for (Serie s : lista) {
            modeloResultados.addElement(s);
        }
        txtDetalhes.setText("Visualizando: " + titulo + "\n" + lista.size() + " séries encontradas.");
    }

    private void acaoOrdenar(String criterio) {
        if (listaAtualExibida != null) {
            switch (criterio) {
                case "nome": usuario.ordenarPorNome(listaAtualExibida); break;
                case "nota": usuario.ordenarPorNota(listaAtualExibida); break;
                case "estado": usuario.ordenarPorEstado(listaAtualExibida); break;
                case "estreia": usuario.ordenarPorEstreia(listaAtualExibida); break;
            }
            atualizarListaVisual(listaAtualExibida, "Lista Ordenada");
        }
    }
}