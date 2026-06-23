
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Tela principal da aplicação
 */
public class TelaPrincipal extends JFrame {

    private Usuario usuarioAtual;
    private ListasSeries listas;
    private TVMazeAPIClient apiClient;
    private JLabel labelUsuario;
    private JPanel painelPrincipal;
    private HashMap<String, Serie> seriesCache;

    public TelaPrincipal() {
        setTitle("Sistema de Acompanhamento de Séries");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        this.apiClient = new TVMazeAPIClient();
        this.seriesCache = new HashMap<>();

        try {
            usuarioAtual = PersistenceManager.carregarUsuario();
            listas = InicializadorSistema.inicializar();
        } catch (IOException e) {
            usuarioAtual = new Usuario();
            listas = new ListasSeries();
            JOptionPane.showMessageDialog(this,
                    "Não foi possível carregar dados anteriores.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        inicializarUI();
        configurarListenersFechamento();
    }

    private void inicializarUI() {
        // Menu Bar
        JMenuBar menuBar = criarMenuBar();
        setJMenuBar(menuBar);

        // Painel de cabeçalho
        JPanel painelCabecalho = criarPainelCabecalho();

        // Painel principal (centro)
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        mostrarTelaInicial();

        // Container principal
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(painelCabecalho, BorderLayout.NORTH);
        container.add(painelPrincipal, BorderLayout.CENTER);
    }

    private JMenuBar criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> sair());
        menuArquivo.add(itemSair);
        menuBar.add(menuArquivo);

        // Menu Usuário
        JMenu menuUsuario = new JMenu("Usu\u00E1rio");
        JMenuItem itemAlterarNome = new JMenuItem("Alterar Nome/Apelido");
        itemAlterarNome.addActionListener(e -> alterarNomeUsuario());
        menuUsuario.add(itemAlterarNome);
        menuBar.add(menuUsuario);

        // Menu Séries
        JMenu menuSeries = new JMenu("Séries");
        JMenuItem itemBuscar = new JMenuItem("Buscar Série");
        itemBuscar.addActionListener(e -> mostrarTelaBusca());
        menuSeries.add(itemBuscar);
        menuSeries.addSeparator();

        JMenuItem itemFavoritos = new JMenuItem("Meus Favoritos");
        itemFavoritos.addActionListener(e -> mostrarListaFavoritos());
        menuSeries.add(itemFavoritos);

        JMenuItem itemJaAssistidas = new JMenuItem("Já Assistidas");
        itemJaAssistidas.addActionListener(e -> mostrarListaJaAssistidas());
        menuSeries.add(itemJaAssistidas);

        JMenuItem itemDesejamAssistir = new JMenuItem("Desejo Assistir");
        itemDesejamAssistir.addActionListener(e -> mostrarListaDesejamAssistir());
        menuSeries.add(itemDesejamAssistir);

        menuBar.add(menuSeries);

        return menuBar;
    }

    private JPanel criarPainelCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.setBackground(new Color(240, 240, 240));

        JLabel labelTitulo = new JLabel("Sistema de Acompanhamento de Séries");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        labelUsuario = new JLabel("Usu\u00E1rio: " + usuarioAtual.getNomeOuApelido());
        labelUsuario.setFont(new Font("Arial", Font.PLAIN, 12));

        painel.add(labelTitulo, BorderLayout.WEST);
        painel.add(labelUsuario, BorderLayout.EAST);

        return painel;
    }

    private void mostrarTelaInicial() {
        painelPrincipal.removeAll();

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título e boas-vindas
        JPanel painelBemVindo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelBemVindo = new JLabel("Bem-vindo, " + usuarioAtual.getNomeOuApelido() + "!");
        labelBemVindo.setFont(new Font("Arial", Font.BOLD, 24));
        painelBemVindo.add(labelBemVindo);

        // Estatísticas
        JPanel painelStats = new JPanel(new GridLayout(3, 1, 10, 10));
        painelStats.setBorder(BorderFactory.createTitledBorder("Suas Listas"));
        painelStats.setPreferredSize(new Dimension(400, 150));

        JLabel labelFavoritos = new JLabel("Favoritos: " + listas.getFavoritos().size() + " série(s)");
        JLabel labelJaAssistidas = new JLabel("Já Assistidas: " + listas.getJaAssistidas().size() + " série(s)");
        JLabel labelDesejam = new JLabel("Desejo Assistir: " + listas.getDesejamAssistir().size() + " série(s)");

        painelStats.add(labelFavoritos);
        painelStats.add(labelJaAssistidas);
        painelStats.add(labelDesejam);

        // Botões de ação rápida
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnBuscar = new JButton("Buscar Série");
        btnBuscar.addActionListener(e -> mostrarTelaBusca());

        JButton btnFavoritos = new JButton("Favoritos");
        btnFavoritos.addActionListener(e -> mostrarListaFavoritos());

        JButton btnJaAssistidas = new JButton("Já Assistidas");
        btnJaAssistidas.addActionListener(e -> mostrarListaJaAssistidas());

        JButton btnDesejam = new JButton("Desejo Assistir");
        btnDesejam.addActionListener(e -> mostrarListaDesejamAssistir());

        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnFavoritos);
        painelBotoes.add(btnJaAssistidas);
        painelBotoes.add(btnDesejam);

        painel.add(painelBemVindo, BorderLayout.NORTH);
        painel.add(painelStats, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(painel, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private void mostrarTelaBusca() {
        painelPrincipal.removeAll();

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel labelBusca = new JLabel("Nome da Série:");
        JTextField textoBusca = new JTextField(30);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnVoltar = new JButton("Voltar");

        painelBusca.add(labelBusca);
        painelBusca.add(textoBusca);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnVoltar);

        // Painel de resultados
        JPanel painelResultados = new JPanel(new BorderLayout());
        painelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        DefaultListModel<Serie> resultadosModel = new DefaultListModel<>();
        JList<Serie> listaResultados = new JList<>(resultadosModel);
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollResultados = new JScrollPane(listaResultados);
        painelResultados.add(scrollResultados, BorderLayout.CENTER);

        // Painel de detalhes da série selecionada
        JPanel painelDetalhes = new JPanel(new BorderLayout());
        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes da Série"));
        JEditorPane areaDetalhes = new JEditorPane("text/html", "");
        areaDetalhes.setEditable(false);
        areaDetalhes.setBackground(UIManager.getColor("Panel.background"));
        JScrollPane scrollDetalhes = new JScrollPane(areaDetalhes);
        scrollDetalhes.setPreferredSize(new Dimension(100, 220));
        painelDetalhes.add(scrollDetalhes, BorderLayout.CENTER);

        // Painel de ações
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton btnAdicionarFavorito = new JButton("Adicionar aos Favoritos");
        JButton btnAdicionarJaAssistida = new JButton("Marcar como Já Assistida");
        JButton btnAdicionarDesejo = new JButton("Adicionar ao Desejo");

        painelAcoes.add(btnAdicionarFavorito);
        painelAcoes.add(btnAdicionarJaAssistida);
        painelAcoes.add(btnAdicionarDesejo);

        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Serie serieSelecionada = listaResultados.getSelectedValue();
                areaDetalhes.setText(serieSelecionada != null ? formatarDetalhes(serieSelecionada) : "");
            }
        });

        btnBuscar.addActionListener(e -> {
            String nomeBusca = textoBusca.getText().trim();
            if (nomeBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um nome para buscar!",
                        "Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        btnBuscar.setEnabled(false);
                        btnBuscar.setText("Buscando...");
                        java.util.List<Serie> resultados = apiClient.buscarSeriesPorNome(nomeBusca);

                        resultadosModel.clear();
                        for (Serie s : resultados) {
                            resultadosModel.addElement(s);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(TelaPrincipal.this,
                                "Erro ao buscar: " + ex.getMessage(),
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        btnBuscar.setEnabled(true);
                        btnBuscar.setText("Buscar");
                    }
                    return null;
                }
            }.execute();
        });

        btnAdicionarFavorito.addActionListener(e -> {
            Serie serieSelecionada = listaResultados.getSelectedValue();
            if (serieSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma série na lista de resultados.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (listas.isFavorito(serieSelecionada)) {
                listas.removerFavorito(serieSelecionada);
                JOptionPane.showMessageDialog(this, "Série removida dos Favoritos.", "Favoritos", JOptionPane.INFORMATION_MESSAGE);
            } else {
                listas.adicionarFavorito(serieSelecionada);
                JOptionPane.showMessageDialog(this, "Série adicionada aos Favoritos.", "Favoritos", JOptionPane.INFORMATION_MESSAGE);
            }
            salvarListas();
        });

        btnAdicionarJaAssistida.addActionListener(e -> {
            Serie serieSelecionada = listaResultados.getSelectedValue();
            if (serieSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma série na lista de resultados.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (listas.isJaAssistida(serieSelecionada)) {
                listas.removerJaAssistida(serieSelecionada);
                JOptionPane.showMessageDialog(this, "Série removida das Já Assistidas.", "Já Assistidas", JOptionPane.INFORMATION_MESSAGE);
            } else {
                listas.adicionarJaAssistida(serieSelecionada);
                JOptionPane.showMessageDialog(this, "Série marcada como Já Assistida.", "Já Assistidas", JOptionPane.INFORMATION_MESSAGE);
            }
            salvarListas();
        });

        btnAdicionarDesejo.addActionListener(e -> {
            Serie serieSelecionada = listaResultados.getSelectedValue();
            if (serieSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma série na lista de resultados.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (listas.isDesejamAssistir(serieSelecionada)) {
                listas.removerDesejamAssistir(serieSelecionada);
                JOptionPane.showMessageDialog(this, "Série removida do Desejo Assistir.", "Desejo Assistir", JOptionPane.INFORMATION_MESSAGE);
            } else {
                listas.adicionarDesejamAssistir(serieSelecionada);
                JOptionPane.showMessageDialog(this, "Série adicionada ao Desejo Assistir.", "Desejo Assistir", JOptionPane.INFORMATION_MESSAGE);
            }
            salvarListas();
        });

        btnVoltar.addActionListener(e -> mostrarTelaInicial());

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelDetalhes, BorderLayout.CENTER);
        painelInferior.add(painelAcoes, BorderLayout.SOUTH);

        JSplitPane splitBusca = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelResultados, painelInferior);
        splitBusca.setResizeWeight(0.55);
        splitBusca.setOneTouchExpandable(true);
        splitBusca.setDividerLocation(0.55);

        painel.add(painelBusca, BorderLayout.NORTH);
        painel.add(splitBusca, BorderLayout.CENTER);

        painelPrincipal.add(painel, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private void mostrarListaFavoritos() {
        mostrarLista("Favoritos", listas.getFavoritos());
    }

    private void mostrarListaJaAssistidas() {
        mostrarLista("Já Assistidas", listas.getJaAssistidas());
    }

    private void mostrarListaDesejamAssistir() {
        mostrarLista("Desejo Assistir", listas.getDesejamAssistir());
    }

    private void mostrarLista(String titulo, java.util.List<Serie> series) {
        painelPrincipal.removeAll();

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Painel de ordenação
        JPanel painelOrdenacao = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel labelOrdenar = new JLabel("Ordenar por:");
        JComboBox<String> comboOrdenacao = new JComboBox<>(
                new String[]{"Nome", "Nota", "Estado", "Data de Estreia"});
        JButton btnAplicarOrdenacao = new JButton("Aplicar");
        JButton btnVoltar = new JButton("Voltar");

        painelOrdenacao.add(labelOrdenar);
        painelOrdenacao.add(comboOrdenacao);
        painelOrdenacao.add(btnAplicarOrdenacao);
        painelOrdenacao.add(btnVoltar);

        // Painel de lista
        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBorder(BorderFactory.createTitledBorder(titulo));
        DefaultListModel<Serie> listModel = new DefaultListModel<>();
        JList<Serie> listaSeries = new JList<>(listModel);
        listaSeries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaSeries);
        painelLista.add(scrollLista, BorderLayout.CENTER);

        // Painel de detalhes
        JPanel painelDetalhes = new JPanel(new BorderLayout());
        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        JEditorPane areaDetalhes = new JEditorPane("text/html", "");
        areaDetalhes.setEditable(false);
        areaDetalhes.setBackground(UIManager.getColor("Panel.background"));
        JScrollPane scrollDetalhes = new JScrollPane(areaDetalhes);
        scrollDetalhes.setPreferredSize(new Dimension(100, 220));
        painelDetalhes.add(scrollDetalhes, BorderLayout.CENTER);

        // Painel de remoção
        JPanel painelRemove = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton btnRemoverSelecionado = new JButton("Remover Selecionado");
        painelRemove.add(btnRemoverSelecionado);

        listaSeries.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Serie serieSelecionada = listaSeries.getSelectedValue();
                areaDetalhes.setText(serieSelecionada != null ? formatarDetalhes(serieSelecionada) : "");
            }
        });

        // Função para atualizar a exibição
        Runnable atualizarExibicao = () -> {
            listModel.clear();
            for (Serie s : series) {
                listModel.addElement(s);
            }
        };

        btnAplicarOrdenacao.addActionListener(e -> {
            String opcao = (String) comboOrdenacao.getSelectedItem();
            java.util.List<Serie> seriesOrdenadas = series;

            switch (opcao) {
                case "Nome":
                    seriesOrdenadas = listas.ordenarPorNome(series);
                    break;
                case "Nota":
                    seriesOrdenadas = listas.ordenarPorNota(series);
                    break;
                case "Estado":
                    seriesOrdenadas = listas.ordenarPorEstado(series);
                    break;
                case "Data de Estreia":
                    seriesOrdenadas = listas.ordenarPorDataEstreia(series);
                    break;
            }

            series.clear();
            series.addAll(seriesOrdenadas);
            atualizarExibicao.run();
        });

        btnRemoverSelecionado.addActionListener(e -> {
            Serie serieSelecionada = listaSeries.getSelectedValue();
            if (serieSelecionada == null) {
                JOptionPane.showMessageDialog(this,
                        "Selecione uma série para remover.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            switch (titulo) {
                case "Favoritos":
                    listas.removerFavorito(serieSelecionada);
                    break;
                case "Já Assistidas":
                    listas.removerJaAssistida(serieSelecionada);
                    break;
                case "Desejo Assistir":
                    listas.removerDesejamAssistir(serieSelecionada);
                    break;
            }
            series.remove(serieSelecionada);
            atualizarExibicao.run();
            areaDetalhes.setText("");
            salvarListas();
            JOptionPane.showMessageDialog(this,
                    "Série removida de " + titulo + ".",
                    "Remover", JOptionPane.INFORMATION_MESSAGE);
        });

        btnVoltar.addActionListener(e -> mostrarTelaInicial());

        atualizarExibicao.run();

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelDetalhes, BorderLayout.CENTER);
        painelInferior.add(painelRemove, BorderLayout.SOUTH);

        JSplitPane splitLista = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelLista, painelInferior);
        splitLista.setResizeWeight(0.55);
        splitLista.setOneTouchExpandable(true);
        splitLista.setDividerLocation(0.55);

        painel.add(painelOrdenacao, BorderLayout.NORTH);
        painel.add(splitLista, BorderLayout.CENTER);

        painelPrincipal.add(painel, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private String formatarDetalhes(Serie serie) {
        if (serie == null) {
            return "";
        }
        return "<html>"
                + "<h2>" + serie.getNome() + "</h2>"
                + "<p><b>Idioma:</b> " + serie.getIdioma() + "</p>"
                + "<p><b>Gêneros:</b> " + serie.getGenerosFormatado() + "</p>"
                + "<p><b>Nota geral:</b> " + serie.getNota() + "</p>"
                + "<p><b>Estado:</b> " + serie.getEstado() + "</p>"
                + "<p><b>Data de estreia:</b> " + (serie.getDataEstreia() != null ? serie.getDataEstreia() : "Não informado") + "</p>"
                + "<p><b>Data de término:</b> " + (serie.getDataTermino() != null ? serie.getDataTermino() : "Não informado") + "</p>"
                + "<p><b>Emissora:</b> " + serie.getEmissora() + "</p>"
                + "<hr>"
                + "<p>" + serie.getResumo() + "</p>"
                + "</html>";
    }

    private void salvarListas() {
        try {
            PersistenceManager.salvarSeries(listas);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar listas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarNomeUsuario() {
        String novoNome = JOptionPane.showInputDialog(this,
                "Digite seu novo nome ou apelido:",
                usuarioAtual.getNomeOuApelido());

        if (novoNome != null && !novoNome.trim().isEmpty()) {
            usuarioAtual.setNomeOuApelido(novoNome);
            labelUsuario.setText("Usu\u00E1rio: " + usuarioAtual.getNomeOuApelido());
            try {
                PersistenceManager.salvarUsuario(usuarioAtual);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar usuário: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarSobre() {
        JOptionPane.showMessageDialog(this,
                "Sistema de Acompanhamento de Séries v1.0\\n\\n"
                + "Desenvolvido para a disciplina de Desenvolvimento Orientado a Objetos\\n\\n"
                + "API: TVMaze (https://www.tvmaze.com/api)",
                "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }

    private void configurarListenersFechamento() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                sair();
            }
        });
    }

    private void sair() {
        try {
            PersistenceManager.salvarUsuario(usuarioAtual);
            PersistenceManager.salvarSeries(listas);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal frame = new TelaPrincipal();
            frame.setVisible(true);
        });
    }
}
