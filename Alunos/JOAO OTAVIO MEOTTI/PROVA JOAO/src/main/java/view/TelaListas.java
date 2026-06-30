package view;

import controller.UsuarioController;
import model.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaListas extends JFrame {
    private final UsuarioController usuarioController;

    private JTabbedPane abasCategorias;
    private JList<Serie> listaFavoritos;
    private JList<Serie> listaAssistidas;
    private JList<Serie> listaQueroAssistir;
    private DefaultListModel<Serie> modeloFav;
    private DefaultListModel<Serie> modeloAssis;
    private DefaultListModel<Serie> modeloQuero;
    private JLabel labelStatus;

    public TelaListas(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        configurarJanela();
        construirInterfaceGrafica();
        sincronizarListasInterface();
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
        cardPrincipal.setLayout(new BorderLayout(0, 20));

        cardPrincipal.add(criarCabecalho(), BorderLayout.NORTH);
        cardPrincipal.add(criarConteudoListas(), BorderLayout.CENTER);
        cardPrincipal.add(criarRodapeAcoes(), BorderLayout.SOUTH);

        painelPrincipal.add(cardPrincipal, BorderLayout.CENTER);
        add(painelPrincipal);
    }

    private JPanel criarCabecalho() {
        JPanel painelCabecalho = new JPanel(new BorderLayout(18, 0));
        painelCabecalho.setOpaque(false);

        JPanel painelTitulo = new JPanel();
        painelTitulo.setOpaque(false);
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Minhas listas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel instrucao = new JLabel("Selecione uma aba e use os botões na lateral para ordenar ou gerenciar.");
        instrucao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instrucao.setForeground(TelaPrincipal.COR_TEXTO_MUTED);
        instrucao.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelTitulo.add(titulo);
        painelTitulo.add(Box.createVerticalStrut(5));
        painelTitulo.add(instrucao);

        JButton voltar = TelaPrincipal.criarBotaoClaro("Menu", TelaPrincipal.COR_CINZA);
        voltar.addActionListener(e -> voltarAoMenuPrincipal());

        painelCabecalho.add(painelTitulo, BorderLayout.CENTER);
        painelCabecalho.add(voltar, BorderLayout.EAST);
        return painelCabecalho;
    }

    private JPanel criarConteudoListas() {
        JPanel conteudo = new JPanel(new BorderLayout(16, 0));
        conteudo.setOpaque(false);

        conteudo.add(criarAbasListas(), BorderLayout.CENTER);
        conteudo.add(criarPainelLateral(), BorderLayout.EAST);
        return conteudo;
    }

    private JPanel criarPainelLateral() {
        JPanel painel = new JPanel(new BorderLayout(0, 14));
        painel.setOpaque(false);
        painel.setPreferredSize(new Dimension(170, 0));

        painel.add(criarPainelOrdenacao(), BorderLayout.NORTH);
        painel.add(criarPainelGerenciarLista(), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelOrdenacao() {
        JPanel painel = TelaPrincipal.criarPainelArredondado(TelaPrincipal.COR_CARD_SUAVE, 10, new Insets(14, 14, 14, 14));
        painel.setLayout(new BorderLayout(0, 10));
        painel.setPreferredSize(new Dimension(170, 245));

        JLabel titulo = new JLabel("Ordenar");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        JPanel botoes = new JPanel(new GridLayout(4, 1, 0, 8));
        botoes.setOpaque(false);

        JButton btnNome = TelaPrincipal.criarBotaoEstilizado("Nome", TelaPrincipal.COR_ROXO);
        JButton btnNota = TelaPrincipal.criarBotaoEstilizado("Nota", TelaPrincipal.COR_ROSA);
        JButton btnStatus = TelaPrincipal.criarBotaoEstilizado("Status", TelaPrincipal.COR_VERDE);
        JButton btnData = TelaPrincipal.criarBotaoEstilizado("Estreia", TelaPrincipal.COR_AMARELO);

        btnNome.addActionListener(e -> dispararOrdenacao("nome"));
        btnNota.addActionListener(e -> dispararOrdenacao("nota"));
        btnStatus.addActionListener(e -> dispararOrdenacao("status"));
        btnData.addActionListener(e -> dispararOrdenacao("data"));

        botoes.add(btnNome);
        botoes.add(btnNota);
        botoes.add(btnStatus);
        botoes.add(btnData);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(botoes, BorderLayout.CENTER);
        return painel;
    }

    private JTabbedPane criarAbasListas() {
        modeloFav = new DefaultListModel<>();
        listaFavoritos = new JList<>(modeloFav);
        configurarRenderizadorDeNome(listaFavoritos);

        modeloAssis = new DefaultListModel<>();
        listaAssistidas = new JList<>(modeloAssis);
        configurarRenderizadorDeNome(listaAssistidas);

        modeloQuero = new DefaultListModel<>();
        listaQueroAssistir = new JList<>(modeloQuero);
        configurarRenderizadorDeNome(listaQueroAssistir);

        abasCategorias = new JTabbedPane();
        abasCategorias.setFont(new Font("Segoe UI", Font.BOLD, 14));
        abasCategorias.setBackground(TelaPrincipal.COR_CARD_CLARO);
        abasCategorias.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
        abasCategorias.setBorder(BorderFactory.createEmptyBorder());
        abasCategorias.addTab("♥ Favoritos", TelaPrincipal.criarScrollPadrao(listaFavoritos));
        abasCategorias.addTab("✓ Assistidos", TelaPrincipal.criarScrollPadrao(listaAssistidas));
        abasCategorias.addTab("+ Quero ver", TelaPrincipal.criarScrollPadrao(listaQueroAssistir));

        return abasCategorias;
    }

    private JPanel criarPainelGerenciarLista() {
        JPanel painel = TelaPrincipal.criarPainelArredondado(TelaPrincipal.COR_CARD_SUAVE, 10, new Insets(14, 14, 14, 14));
        painel.setLayout(new BorderLayout(0, 10));

        JLabel titulo = new JLabel("Gerenciar");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        JPanel botoes = new JPanel(new GridLayout(3, 1, 0, 10));
        botoes.setOpaque(false);

        JButton botaoVerDetalhes = TelaPrincipal.criarBotaoEstilizado("Ver detalhes", TelaPrincipal.COR_ROXO);
        JButton botaoRemover = TelaPrincipal.criarBotaoEstilizado("Remover", TelaPrincipal.COR_VERMELHO);
        JButton botaoVoltar = TelaPrincipal.criarBotaoEstilizado("Menu", TelaPrincipal.COR_CINZA);

        botaoVerDetalhes.addActionListener(e -> DetalhesSerieDialog.exibir(this, obterSerieSelecionadaNaAbaAtual()));
        botaoRemover.addActionListener(e -> executarExclusaoItem());
        botaoVoltar.addActionListener(e -> voltarAoMenuPrincipal());

        botoes.add(botaoVerDetalhes);
        botoes.add(botaoRemover);
        botoes.add(botaoVoltar);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(botoes, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarRodapeAcoes() {
        JPanel painelRodape = new JPanel(new BorderLayout());
        painelRodape.setOpaque(false);
        painelRodape.setPreferredSize(new Dimension(0, 28));

        labelStatus = new JLabel("Escolha um item para gerenciar.");
        labelStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelStatus.setForeground(TelaPrincipal.COR_TEXTO_MUTED);

        painelRodape.add(labelStatus, BorderLayout.CENTER);
        return painelRodape;
    }

    private void configurarRenderizadorDeNome(JList<Serie> listaAlvo) {
        listaAlvo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TelaPrincipal.configurarListaPadrao(listaAlvo);
        listaAlvo.setCellRenderer(new ListaRenderer());
    }

    private void sincronizarListasInterface() {
        povoarModelo(modeloFav, usuarioController.getUsuario().getFavoritos());
        povoarModelo(modeloAssis, usuarioController.getUsuario().getAssistidos());
        povoarModelo(modeloQuero, usuarioController.getUsuario().getQueroAssistir());
    }

    private void povoarModelo(DefaultListModel<Serie> model, List<Serie> listaOriginal) {
        model.clear();
        for (Serie serie : listaOriginal) {
            model.addElement(serie);
        }
    }

    private void dispararOrdenacao(String criterio) {
        List<Serie> listaAlvo = obterListaDaAbaAtual();
        DefaultListModel<Serie> modeloAlvo = obterModeloDaAbaAtual();

        if (listaAlvo == null || listaAlvo.isEmpty()) {
            labelStatus.setText("A lista atual está vazia.");
            return;
        }

        switch (criterio) {
            case "nome" -> usuarioController.ordenarListaPorNome(listaAlvo);
            case "nota" -> usuarioController.ordenarListaPorNota(listaAlvo);
            case "status" -> usuarioController.ordenarListaPorStatus(listaAlvo);
            case "data" -> usuarioController.ordenarListaPorDataEstreia(listaAlvo);
        }

        povoarModelo(modeloAlvo, listaAlvo);
        labelStatus.setText("Lista ordenada com sucesso.");
    }

    private void executarExclusaoItem() {
        Serie serieSelecionada = obterSerieSelecionadaNaAbaAtual();
        if (serieSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione um item primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int escolha = JOptionPane.showConfirmDialog(
                this,
                "Remover '" + serieSelecionada.getNome() + "' da lista atual?",
                "Confirmar remoção",
                JOptionPane.YES_NO_OPTION
        );

        if (escolha == JOptionPane.YES_OPTION) {
            int abaSelecionada = abasCategorias.getSelectedIndex();
            if (abaSelecionada == 0) usuarioController.desfavoritarSerie(serieSelecionada);
            else if (abaSelecionada == 1) usuarioController.removerDeAssistidas(serieSelecionada);
            else if (abaSelecionada == 2) usuarioController.removerListaQueroAssistir(serieSelecionada);

            sincronizarListasInterface();
            labelStatus.setText("Item removido com sucesso.");
        }
    }

    private List<Serie> obterListaDaAbaAtual() {
        int abaSelecionada = abasCategorias.getSelectedIndex();
        if (abaSelecionada == 0) return usuarioController.getUsuario().getFavoritos();
        if (abaSelecionada == 1) return usuarioController.getUsuario().getAssistidos();
        if (abaSelecionada == 2) return usuarioController.getUsuario().getQueroAssistir();
        return null;
    }

    private DefaultListModel<Serie> obterModeloDaAbaAtual() {
        int abaSelecionada = abasCategorias.getSelectedIndex();
        if (abaSelecionada == 0) return modeloFav;
        if (abaSelecionada == 1) return modeloAssis;
        return modeloQuero;
    }

    private Serie obterSerieSelecionadaNaAbaAtual() {
        int abaSelecionada = abasCategorias.getSelectedIndex();
        if (abaSelecionada == 0) return listaFavoritos.getSelectedValue();
        if (abaSelecionada == 1) return listaAssistidas.getSelectedValue();
        if (abaSelecionada == 2) return listaQueroAssistir.getSelectedValue();
        return null;
    }

    private String valorOuPadrao(String valor) {
        return (valor == null || valor.isBlank()) ? "Não informado" : valor;
    }

    private void voltarAoMenuPrincipal() {
        new TelaPrincipal(usuarioController).setVisible(true);
        dispose();
    }

    private class ListaRenderer extends JPanel implements ListCellRenderer<Serie> {
        private final JLabel titulo = new JLabel();
        private final JLabel meta = new JLabel();
        private final JLabel indicador = new JLabel();

        ListaRenderer() {
            setLayout(new BorderLayout(12, 0));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            setOpaque(true);

            JPanel textos = new JPanel();
            textos.setOpaque(false);
            textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

            titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
            meta.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            indicador.setHorizontalAlignment(SwingConstants.CENTER);
            indicador.setFont(new Font("Segoe UI", Font.BOLD, 13));
            indicador.setOpaque(true);
            indicador.setPreferredSize(new Dimension(82, 36));
            indicador.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

            textos.add(titulo);
            textos.add(Box.createVerticalStrut(5));
            textos.add(meta);

            add(textos, BorderLayout.CENTER);
            add(indicador, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Serie> list, Serie serie, int index, boolean isSelected, boolean cellHasFocus) {
            if (serie != null) {
                titulo.setText(serie.getNome());
                meta.setText(valorOuPadrao(serie.getGeneros()) + "  •  " + valorOuPadrao(serie.getStatus()) + "  •  " + valorOuPadrao(serie.getDataEstreia()));
                indicador.setText(" " + String.format("%.1f", serie.getNota()));
            }

            if (isSelected) {
                setBackground(TelaPrincipal.COR_LILAS_CLARO);
                titulo.setForeground(TelaPrincipal.COR_ROXO);
                meta.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
                indicador.setBackground(TelaPrincipal.COR_ROXO);
                indicador.setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
                meta.setForeground(TelaPrincipal.COR_TEXTO_MUTED);
                indicador.setBackground(TelaPrincipal.COR_VERDE_CLARO);
                indicador.setForeground(TelaPrincipal.COR_VERDE);
            }
            return this;
        }
    }
}
