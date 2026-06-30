package view;

import controller.TvMazeController;
import controller.UsuarioController;
import model.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaBusca extends JFrame {
    private final UsuarioController usuarioController;
    private final TvMazeController tvMazeController;

    private JTextField campoEntradaBusca;
    private JList<Serie> listaResultados;
    private DefaultListModel<Serie> modeloListaResultados;
    private JLabel labelStatusBusca;

    public TelaBusca(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        this.tvMazeController = new TvMazeController();
        configurarJanela();
        construirInterfaceGrafica();
    }

    private void configurarJanela() {
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void construirInterfaceGrafica() {
        JPanel painelPrincipal = TelaPrincipal.criarPainelBase();
        painelPrincipal.setLayout(new BorderLayout(0, 0));

        JPanel cardPrincipal = TelaPrincipal.criarCardPrincipal();
        cardPrincipal.setLayout(new BorderLayout(0, 22));

        cardPrincipal.add(criarCabecalhoBusca(), BorderLayout.NORTH);
        cardPrincipal.add(criarCorpoBusca(), BorderLayout.CENTER);

        painelPrincipal.add(cardPrincipal, BorderLayout.CENTER);
        add(painelPrincipal);
    }

    private JPanel criarCabecalhoBusca() {
        JPanel painelCabecalho = new JPanel(new BorderLayout(18, 0));
        painelCabecalho.setOpaque(false);

        JPanel painelTexto = new JPanel();
        painelTexto.setOpaque(false);
        painelTexto.setLayout(new BoxLayout(painelTexto, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Buscar filmes e séries");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel instrucao = new JLabel("Digite o nome, selecione um resultado e escolha uma ação no menu lateral.");
        instrucao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instrucao.setForeground(TelaPrincipal.COR_TEXTO_MUTED);
        instrucao.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelTexto.add(titulo);
        painelTexto.add(Box.createVerticalStrut(5));
        painelTexto.add(instrucao);

        JButton botaoVoltar = TelaPrincipal.criarBotaoClaro("Menu", TelaPrincipal.COR_CINZA);
        botaoVoltar.addActionListener(e -> voltarAoMenuPrincipal());

        painelCabecalho.add(painelTexto, BorderLayout.CENTER);
        painelCabecalho.add(botaoVoltar, BorderLayout.EAST);
        return painelCabecalho;
    }

    private JPanel criarCorpoBusca() {
        JPanel painel = new JPanel(new BorderLayout(16, 0));
        painel.setOpaque(false);

        JPanel areaResultados = new JPanel(new BorderLayout(0, 14));
        areaResultados.setOpaque(false);
        areaResultados.add(criarBarraPesquisa(), BorderLayout.NORTH);
        areaResultados.add(criarListaResultados(), BorderLayout.CENTER);

        painel.add(areaResultados, BorderLayout.CENTER);
        painel.add(criarPainelAcoes(), BorderLayout.EAST);
        return painel;
    }

    private JPanel criarBarraPesquisa() {
        JPanel barra = TelaPrincipal.criarPainelArredondado(TelaPrincipal.COR_CARD_SUAVE, 10, new Insets(12, 12, 12, 12));
        barra.setLayout(new BorderLayout(10, 0));

        campoEntradaBusca = new JTextField();
        campoEntradaBusca.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoEntradaBusca.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
        campoEntradaBusca.setBackground(Color.WHITE);
        campoEntradaBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TelaPrincipal.COR_BORDA),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        campoEntradaBusca.setCaretColor(TelaPrincipal.COR_ROXO);
        campoEntradaBusca.addActionListener(e -> executarBuscaApi());

        JButton botaoPesquisar = TelaPrincipal.criarBotaoEstilizado("Pesquisar", TelaPrincipal.COR_ROXO);
        botaoPesquisar.addActionListener(e -> executarBuscaApi());

        barra.add(campoEntradaBusca, BorderLayout.CENTER);
        barra.add(botaoPesquisar, BorderLayout.EAST);
        return barra;
    }

    private JPanel criarListaResultados() {
        JPanel painelLista = TelaPrincipal.criarPainelArredondado(Color.WHITE, 10, new Insets(16, 16, 16, 16));
        painelLista.setLayout(new BorderLayout(0, 12));

        JPanel topoLista = new JPanel(new BorderLayout());
        topoLista.setOpaque(false);

        JLabel labelLista = new JLabel("Resultados encontrados");
        labelLista.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelLista.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        labelStatusBusca = new JLabel("Digite o nome de uma produção para iniciar.");
        labelStatusBusca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelStatusBusca.setForeground(TelaPrincipal.COR_TEXTO_MUTED);

        topoLista.add(labelLista, BorderLayout.WEST);
        topoLista.add(labelStatusBusca, BorderLayout.EAST);

        modeloListaResultados = new DefaultListModel<>();
        listaResultados = new JList<>(modeloListaResultados);
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TelaPrincipal.configurarListaPadrao(listaResultados);
        listaResultados.setCellRenderer(new ResultadoRenderer());

        painelLista.add(topoLista, BorderLayout.NORTH);
        painelLista.add(TelaPrincipal.criarScrollPadrao(listaResultados), BorderLayout.CENTER);
        return painelLista;
    }

    private JPanel criarPainelAcoes() {
        JPanel painelAcoes = TelaPrincipal.criarPainelArredondado(TelaPrincipal.COR_CARD_SUAVE, 10, new Insets(14, 14, 14, 14));
        painelAcoes.setLayout(new BorderLayout(0, 12));
        painelAcoes.setPreferredSize(new Dimension(165, 0));

        JLabel titulo = new JLabel("Ações");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        JPanel botoes = new JPanel(new GridLayout(5, 1, 0, 10));
        botoes.setOpaque(false);

        JButton botaoVerDetalhes = TelaPrincipal.criarBotaoEstilizado("Ver detalhes", TelaPrincipal.COR_ROXO);
        JButton botaoAdicionarFav = TelaPrincipal.criarBotaoEstilizado("Favoritar", TelaPrincipal.COR_ROSA);
        JButton botaoAdicionarAssistido = TelaPrincipal.criarBotaoEstilizado("Assistido", TelaPrincipal.COR_VERDE);
        JButton botaoAdicionarQuero = TelaPrincipal.criarBotaoEstilizado("Quero ver", TelaPrincipal.COR_AMARELO);
        JButton botaoVoltarMenu = TelaPrincipal.criarBotaoEstilizado("Menu", TelaPrincipal.COR_CINZA);

        botaoVerDetalhes.addActionListener(e -> DetalhesSerieDialog.exibir(this, obterSerieSelecionada()));
        botaoAdicionarFav.addActionListener(e -> gerenciarInclusaoLista("favoritos"));
        botaoAdicionarAssistido.addActionListener(e -> gerenciarInclusaoLista("assistidas"));
        botaoAdicionarQuero.addActionListener(e -> gerenciarInclusaoLista("queroAssistir"));
        botaoVoltarMenu.addActionListener(e -> voltarAoMenuPrincipal());

        botoes.add(botaoVerDetalhes);
        botoes.add(botaoAdicionarFav);
        botoes.add(botaoAdicionarAssistido);
        botoes.add(botaoAdicionarQuero);
        botoes.add(botaoVoltarMenu);

        painelAcoes.add(titulo, BorderLayout.NORTH);
        painelAcoes.add(botoes, BorderLayout.CENTER);
        return painelAcoes;
    }

    private void executarBuscaApi() {
        String texto = campoEntradaBusca.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome de uma série ou filme para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloListaResultados.clear();
        labelStatusBusca.setText("Buscando na API...");

        SwingWorker<List<Serie>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Serie> doInBackground() {
                return tvMazeController.consultarSeriesPorNome(texto);
            }

            @Override
            protected void done() {
                try {
                    List<Serie> resultado = get();
                    if (resultado.isEmpty()) {
                        labelStatusBusca.setText("Nenhum resultado encontrado.");
                        return;
                    }

                    for (Serie serie : resultado) {
                        modeloListaResultados.addElement(serie);
                    }
                    labelStatusBusca.setText(resultado.size() + " resultado(s).");
                } catch (Exception e) {
                    labelStatusBusca.setText("Erro ao consultar a API.");
                }
            }
        };
        worker.execute();
    }

    private void gerenciarInclusaoLista(String tipoLista) {
        Serie serieSelecionada = obterSerieSelecionada();
        if (serieSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma série da lista primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        switch (tipoLista) {
            case "favoritos" -> usuarioController.favoritarSerie(serieSelecionada);
            case "assistidas" -> usuarioController.marcarComoAssistida(serieSelecionada);
            case "queroAssistir" -> usuarioController.adicionarListaQueroAssistir(serieSelecionada);
        }

        JOptionPane.showMessageDialog(this, "Item adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private Serie obterSerieSelecionada() {
        return listaResultados.getSelectedValue();
    }

    private String valorOuPadrao(String valor) {
        return (valor == null || valor.isBlank()) ? "Não informado" : valor;
    }

    private void voltarAoMenuPrincipal() {
        new TelaPrincipal(usuarioController).setVisible(true);
        dispose();
    }

    private class ResultadoRenderer extends JPanel implements ListCellRenderer<Serie> {
        private final JLabel titulo = new JLabel();
        private final JLabel meta = new JLabel();
        private final JLabel nota = new JLabel();

        ResultadoRenderer() {
            setLayout(new BorderLayout(12, 0));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            setOpaque(true);

            JPanel textos = new JPanel();
            textos.setOpaque(false);
            textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

            titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
            meta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            nota.setFont(new Font("Segoe UI", Font.BOLD, 13));
            nota.setHorizontalAlignment(SwingConstants.CENTER);
            nota.setPreferredSize(new Dimension(74, 36));
            nota.setOpaque(true);
            nota.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

            textos.add(titulo);
            textos.add(Box.createVerticalStrut(5));
            textos.add(meta);

            add(textos, BorderLayout.CENTER);
            add(nota, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Serie> list, Serie serie, int index, boolean isSelected, boolean cellHasFocus) {
            if (serie != null) {
                titulo.setText(serie.getNome());
                meta.setText(valorOuPadrao(serie.getGeneros()) + "  •  " + valorOuPadrao(serie.getDataEstreia()) + "  •  " + valorOuPadrao(serie.getStatus()));
                nota.setText(" " + String.format("%.1f", serie.getNota()));
            }

            if (isSelected) {
                setBackground(TelaPrincipal.COR_LILAS_CLARO);
                titulo.setForeground(TelaPrincipal.COR_ROXO);
                meta.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
                nota.setBackground(TelaPrincipal.COR_ROXO);
                nota.setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
                meta.setForeground(TelaPrincipal.COR_TEXTO_MUTED);
                nota.setBackground(TelaPrincipal.COR_LILAS_CLARO);
                nota.setForeground(TelaPrincipal.COR_ROXO);
            }

            return this;
        }
    }
}
