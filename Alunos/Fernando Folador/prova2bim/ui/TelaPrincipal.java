package ui;

import model.Serie;
import model.Usuario;
import service.JsonPersistence;
import service.TvMazeService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

// Interface principal do sistema de acompanhamento de séries
public class TelaPrincipal extends JFrame {

    private static final Color COR_FUNDO = new Color(40, 45, 55);
    private static final Color COR_PAINEL = new Color(55, 63, 76);
    private static final Color COR_TEXTO = Color.WHITE;

    private Usuario usuario;

    private JPanel panelListasCards;
    private JPanel panelBuscaCards;

    private JTextField txtBusca;

    private JComboBox<String> comboListas;
    private JComboBox<String> comboOrdenacao;

    // Inicializa os dados do usuário e a interface principal
    public TelaPrincipal() {

        inicializarDados();

        configurarJanela();

        atualizarPainelListas();
    }

    // Carrega os dados do usuário ou cria um novo cadastro local
    private void inicializarDados() {

        String nome = JOptionPane.showInputDialog(
                this,
                "Qual seu nome ou apelido?"
        );

        if (nome == null || nome.trim().isEmpty()) {
            nome = "Fernando";
        }

        nome = nome.trim();

        usuario = JsonPersistence.carregar(nome);

        if (usuario == null) {

            usuario = new Usuario(nome);

            JsonPersistence.salvar(usuario);
        }
    }

    // Configura os componentes principais da janela
    private void configurarJanela() {

        setTitle("Rastreador de Séries");
        setSize(900, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {

            // Salva os dados antes de encerrar o programa
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {

                JsonPersistence.salvar(usuario);

                System.exit(0);
            }
        });

        getContentPane().setBackground(COR_FUNDO);

        JPanel painelTopo = new JPanel(new BorderLayout());

        painelTopo.setBackground(COR_PAINEL);

        painelTopo.setBorder(
                new EmptyBorder(10,10,10,10)
        );

        JLabel lblUsuario =
                new JLabel(
                        "Olá, "
                                + usuario.getNome()
                                + "! Seja bem Vindo!"
                );

        lblUsuario.setForeground(COR_TEXTO);

        painelTopo.add(
                lblUsuario,
                BorderLayout.WEST
        );

        add(
                painelTopo,
                BorderLayout.NORTH
        );

        JTabbedPane abas =
                new JTabbedPane();

        abas.addTab(
                "Minhas Listas",
                criarAbaListas()
        );

        abas.addTab(
                "Buscar Séries",
                criarAbaBusca()
        );

        add(
                abas,
                BorderLayout.CENTER
        );
    }

    // Cria a aba responsável por exibir as listas do usuário
    private JPanel criarAbaListas() {

        JPanel painel =
                new JPanel(new BorderLayout());

        painel.setBackground(COR_FUNDO);

        JPanel painelFiltros =
                new JPanel(new FlowLayout());

        comboListas =
                new JComboBox<>(
                        new String[]{
                                "Favoritos",
                                "Já Assistidas",
                                "Deseja Assistir"
                        }
                );

        comboOrdenacao =
                new JComboBox<>(
                        new String[]{
                                "Ordem Alfabética",
                                "Nota Geral",
                                "Estado",
                                "Data de Estreia"
                        }
                );

        painelFiltros.add(comboListas);

        painelFiltros.add(comboOrdenacao);

        comboListas.addActionListener(
                e -> atualizarPainelListas()
        );

        comboOrdenacao.addActionListener(
                e -> atualizarPainelListas()
        );

        painel.add(
                painelFiltros,
                BorderLayout.NORTH
        );

        panelListasCards =
                new JPanel();

        panelListasCards.setLayout(
                new BoxLayout(
                        panelListasCards,
                        BoxLayout.Y_AXIS
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        panelListasCards
                );

        painel.add(
                scroll,
                BorderLayout.CENTER
        );

        return painel;
    }

    // Cria a aba utilizada para pesquisar séries na API
    private JPanel criarAbaBusca() {

        JPanel painel =
                new JPanel(new BorderLayout());

        txtBusca =
                new JTextField();

        JButton btnBuscar =
                new JButton("Buscar");

        btnBuscar.addActionListener(e -> {

            String busca = txtBusca.getText().trim();

            if (busca.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Digite o nome de uma série."
                );

                return;
            }

            try {
                panelBuscaCards.removeAll();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            JLabel carregando =
                    new JLabel(
                            "Buscando séries..."
                    );

            panelBuscaCards.add(carregando);

            panelBuscaCards.revalidate();
            panelBuscaCards.repaint();

            // Executa a busca em segundo plano para não travar a interface
            new SwingWorker<List<Serie>, Void>() {

                @Override
                protected List<Serie> doInBackground() {

                    return TvMazeService.buscarSeries(busca);
                }

                @Override
                protected void done() {

                    try {

                        List<Serie> series = get();

                        renderizarResultadosBusca(series);

                    }

                    catch (Exception ex) {

                        JOptionPane.showMessageDialog(
                                TelaPrincipal.this,
                                "Erro ao buscar séries."
                        );
                    }
                }

            }.execute();
        });

        JPanel barra =
                new JPanel(
                        new BorderLayout()
                );

        barra.add(
                txtBusca,
                BorderLayout.CENTER
        );

        barra.add(
                btnBuscar,
                BorderLayout.EAST
        );

        painel.add(
                barra,
                BorderLayout.NORTH
        );

        panelBuscaCards =
                new JPanel();

        panelBuscaCards.setLayout(
                new BoxLayout(
                        panelBuscaCards,
                        BoxLayout.Y_AXIS
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        panelBuscaCards
                );

        painel.add(
                scroll,
                BorderLayout.CENTER
        );

        return painel;
    }

    // Exibe na tela os resultados retornados pela busca
    private void renderizarResultadosBusca(List<Serie> series) {

        panelBuscaCards.removeAll();

        if (series.isEmpty()) {

            panelBuscaCards.add(
                    new JLabel("Nenhuma série encontrada.")
            );
        }

        else {

            for (Serie serie : series) {

                JPanel card =
                        new JPanel(
                                new BorderLayout()
                        );

                card.setBorder(
                        BorderFactory.createTitledBorder(
                                serie.getNome()
                        )
                );

                JLabel info =
                        new JLabel(
                                "Idioma: "
                                        + serie.getIdioma()
                                        + " | Nota: "
                                        + serie.getNotaGeral()
                        );

                card.add(info, BorderLayout.CENTER);

                JPanel botoes = new JPanel();

                JButton btnFavorito =
                        new JButton("Favoritos");

                JButton btnAssistida =
                        new JButton("Já Assisti");

                JButton btnDesejo =
                        new JButton("Desejo Assistir");

                JButton btnDetalhes =
                        new JButton("Detalhes");

                btnFavorito.addActionListener(e -> {

                    if (!usuario.getFavoritos().contains(serie)) {

                        usuario.getFavoritos().add(serie);

                        atualizarPainelListas();

                        JsonPersistence.salvar(usuario);

                        JOptionPane.showMessageDialog(
                                this,
                                "Série adicionada aos favoritos."
                        );
                    }
                });

                btnAssistida.addActionListener(e -> {

                    if (!usuario.getJaAssistidas().contains(serie)) {

                        usuario.getJaAssistidas().add(serie);

                        atualizarPainelListas();

                        JsonPersistence.salvar(usuario);

                        JOptionPane.showMessageDialog(
                                this,
                                "Série adicionada às assistidas."
                        );
                    }
                });

                btnDesejo.addActionListener(e -> {

                    if (!usuario.getDesejaAssistir().contains(serie)) {

                        usuario.getDesejaAssistir().add(serie);

                        atualizarPainelListas();

                        JsonPersistence.salvar(usuario);

                        JOptionPane.showMessageDialog(
                                this,
                                "Série adicionada à lista de desejo."
                        );
                    }
                });

                btnDetalhes.addActionListener(e -> {

                    JOptionPane.showMessageDialog(
                            this,
                            "Nome: " + serie.getNome()
                                    + "\nIdioma: " + serie.getIdioma()
                                    + "\nGêneros: " + serie.getGeneros()
                                    + "\nNota: " + serie.getNotaGeral()
                                    + "\nEstado: " + serie.getEstado()
                                    + "\nEstreia: " + serie.getDataEstreia()
                                    + "\nTérmino: " + serie.getDataTermino()
                                    + "\nEmissora: " + serie.getEmissora()
                    );
                });

                botoes.add(btnFavorito);
                botoes.add(btnAssistida);
                botoes.add(btnDesejo);
                botoes.add(btnDetalhes);

                card.add(botoes, BorderLayout.SOUTH);

                panelBuscaCards.add(card);
            }
        }

        panelBuscaCards.revalidate();

        panelBuscaCards.repaint();
    }

    // Atualiza a lista selecionada e aplica a ordenação escolhida
    private void atualizarPainelListas() {

        panelListasCards.removeAll();

        List<Serie> lista;

        switch (comboListas.getSelectedIndex()) {

            case 0:
                lista = usuario.getFavoritos();
                break;

            case 1:
                lista = usuario.getJaAssistidas();
                break;

            default:
                lista = usuario.getDesejaAssistir();
                break;
        }

        usuario.ordenarLista(
                lista,
                comboOrdenacao.getSelectedIndex()
        );

        if (lista.isEmpty()) {

            panelListasCards.add(
                    new JLabel(
                            "Nenhuma série nesta lista."
                    )
            );
        }

        else {

            for (Serie serie : lista) {

                JPanel card =
                        new JPanel(
                                new BorderLayout()
                        );

                card.setBorder(
                        BorderFactory.createTitledBorder(
                                serie.getNome()
                        )
                );

                JLabel info =
                        new JLabel(
                                "Idioma: "
                                        + serie.getIdioma()
                                        + " | Nota: "
                                        + serie.getNotaGeral()
                        );

                card.add(
                        info,
                        BorderLayout.CENTER
                );

                JButton remover =
                        new JButton(
                                "Remover"
                        );

                remover.addActionListener(e -> {

                    lista.remove(serie);

                    JsonPersistence.salvar(usuario);

                    atualizarPainelListas();
                });

                JButton detalhes =
                        new JButton(
                                "Detalhes"
                        );

                detalhes.addActionListener(e -> {

                    JOptionPane.showMessageDialog(
                            this,
                            "Nome: " + serie.getNome()
                                    + "\nIdioma: " + serie.getIdioma()
                                    + "\nGêneros: " + serie.getGeneros()
                                    + "\nNota: " + serie.getNotaGeral()
                                    + "\nEstado: " + serie.getEstado()
                                    + "\nEstreia: " + serie.getDataEstreia()
                                    + "\nTérmino: " + serie.getDataTermino()
                                    + "\nEmissora: " + serie.getEmissora()
                    );
                });

                JPanel painelBotoes =
                        new JPanel();

                painelBotoes.add(detalhes);

                painelBotoes.add(remover);

                card.add(
                        painelBotoes,
                        BorderLayout.EAST
                );

                panelListasCards.add(card);
            }
        }

        panelListasCards.revalidate();

        panelListasCards.repaint();
    }
}