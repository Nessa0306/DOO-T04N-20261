package view;

import model.Serie;
import model.Usuario;
import service.JsonService;
import service.OrdenacaoService;
import service.TvMazeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private final JsonService jsonService = new JsonService();
    private final TvMazeService tvMazeService = new TvMazeService();
    private final OrdenacaoService ordenacaoService = new OrdenacaoService();

    private Usuario usuario;

    private JTextField txtApelido;
    private JTextField txtBusca;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JComboBox<String> cbOrdenacao;

    private List<Serie> listaBusca = new ArrayList<>();
    private List<Serie> listaExibida = new ArrayList<>();

    private enum TipoLista {
        BUSCA,
        FAVORITOS,
        ASSISTIDAS,
        DESEJA_ASSISTIR
    }

    private TipoLista listaAtual = TipoLista.BUSCA;

    public TelaPrincipal() {

        usuario = jsonService.carregarUsuario();

        configurarJanela();

        criarPainelSuperior();
        criarTabela();
        criarPainelInferior();

        setVisible(true);
    }

    private void configurarJanela() {

        setTitle("Minha Série TV");

        setSize(1220, 720);

        setMinimumSize(new Dimension(1100,650));

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(242,244,247));
    }

    private void criarPainelSuperior() {

        JPanel cabecalho = new JPanel(new BorderLayout());

        cabecalho.setBackground(new Color(35,58,88));

        cabecalho.setBorder(new EmptyBorder(18,25,18,25));



        //--------------------------------------
        // TITULO
        //--------------------------------------

        JPanel painelTitulo = new JPanel();

        painelTitulo.setOpaque(false);

        painelTitulo.setLayout(new BoxLayout(painelTitulo,BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("MINHA SÉRIE TV");

        titulo.setFont(new Font("Segoe UI",Font.BOLD,28));

        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Organize e acompanhe suas séries favoritas.");

        subtitulo.setFont(new Font("Segoe UI",Font.PLAIN,13));

        subtitulo.setForeground(new Color(220,225,230));

        painelTitulo.add(titulo);

        painelTitulo.add(Box.createVerticalStrut(4));

        painelTitulo.add(subtitulo);



        //--------------------------------------
        // USUÁRIO
        //--------------------------------------

        JPanel painelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));

        painelUsuario.setOpaque(false);

        JLabel lblUsuario = new JLabel("Usuário");

        lblUsuario.setForeground(Color.WHITE);

        txtApelido = new JTextField(15);

        txtApelido.setPreferredSize(new Dimension(170,30));

        if(usuario.getApelido()!=null){

            txtApelido.setText(usuario.getApelido());

        }

        JButton btnSalvarUsuario = new JButton("Salvar");

        btnSalvarUsuario.setPreferredSize(new Dimension(90,30));

        btnSalvarUsuario.setFocusPainted(false);

        btnSalvarUsuario.addActionListener(e->{

            usuario.setApelido(txtApelido.getText().trim());

            jsonService.salvarUsuario(usuario);

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário salvo com sucesso."
            );

        });

        painelUsuario.add(lblUsuario);

        painelUsuario.add(txtApelido);

        painelUsuario.add(btnSalvarUsuario);



        cabecalho.add(painelTitulo,BorderLayout.WEST);

        cabecalho.add(painelUsuario,BorderLayout.EAST);



        //--------------------------------------
        // PAINEL DE BUSCA
        //--------------------------------------

        JPanel painelBusca = new JPanel(new BorderLayout(10,0));

        painelBusca.setBackground(Color.WHITE);

        painelBusca.setBorder(new EmptyBorder(18,25,18,25));

        JLabel lblBusca = new JLabel("Buscar série");

        lblBusca.setFont(new Font("Segoe UI",Font.BOLD,14));

        txtBusca = new JTextField();

        txtBusca.setPreferredSize(new Dimension(450,34));

        JButton btnBuscar = new JButton("Buscar");

        btnBuscar.setPreferredSize(new Dimension(120,34));

        btnBuscar.setFocusPainted(false);

        btnBuscar.addActionListener(e->buscarSeries());

        txtBusca.addActionListener(e->buscarSeries());

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));

        direita.setOpaque(false);

        direita.add(txtBusca);

        direita.add(btnBuscar);

        painelBusca.add(lblBusca,BorderLayout.WEST);

        painelBusca.add(direita,BorderLayout.CENTER);



        JPanel topo = new JPanel(new BorderLayout());

        topo.setBackground(new Color(242,244,247));

        topo.add(cabecalho,BorderLayout.NORTH);

        topo.add(painelBusca,BorderLayout.CENTER);

        add(topo,BorderLayout.NORTH);

    }
    
    private void criarTabela() {

        modeloTabela = new DefaultTableModel(
                new Object[]{
                        "Nome",
                        "Idioma",
                        "Gêneros",
                        "Nota",
                        "Estado",
                        "Estreia",
                        "Término",
                        "Emissora"
                }, 0) {

            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(32);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setGridColor(new Color(235,235,235));
        tabela.setShowGrid(true);
        tabela.setSelectionBackground(new Color(220,230,245));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setFillsViewportHeight(true);

        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(35,58,88));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout());

        painelTabela.setBackground(new Color(242,244,247));

        painelTabela.setBorder(
                BorderFactory.createEmptyBorder(15,20,15,20)
        );

        JLabel tituloTabela = new JLabel("RESULTADOS DA PESQUISA");

        tituloTabela.setFont(new Font("Segoe UI", Font.BOLD, 15));

        tituloTabela.setBorder(
                BorderFactory.createEmptyBorder(0,5,10,0)
        );

        painelTabela.add(tituloTabela, BorderLayout.NORTH);

        painelTabela.add(scroll, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.CENTER);
    }

    private void criarPainelInferior() {

        JPanel rodape = new JPanel();

        rodape.setLayout(new BoxLayout(rodape, BoxLayout.Y_AXIS));

        rodape.setBackground(Color.WHITE);

        rodape.setBorder(
                BorderFactory.createEmptyBorder(15,20,15,20)
        );



        //----------------------------
        // LINHA 1
        //----------------------------

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.CENTER,15,5));

        linha1.setOpaque(false);

        JLabel lblAdicionar = new JLabel("Adicionar à lista");

        lblAdicionar.setFont(new Font("Segoe UI",Font.BOLD,13));

        JButton btnFavoritos = new JButton("Favoritos");

        JButton btnAssistidas = new JButton("Assistidas");

        JButton btnDesejos = new JButton("Deseja Assistir");

        Dimension tamanhoBotao = new Dimension(170,36);

        btnFavoritos.setPreferredSize(tamanhoBotao);
        btnAssistidas.setPreferredSize(tamanhoBotao);
        btnDesejos.setPreferredSize(tamanhoBotao);

        linha1.add(lblAdicionar);

        linha1.add(btnFavoritos);

        linha1.add(btnAssistidas);

        linha1.add(btnDesejos);



        //----------------------------
        // LINHA 2
        //----------------------------

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.CENTER,15,5));

        linha2.setOpaque(false);

        JLabel lblVisualizar = new JLabel("Visualizar");

        lblVisualizar.setFont(new Font("Segoe UI",Font.BOLD,13));

        JButton btnVerFavoritos = new JButton("Favoritos");

        JButton btnVerAssistidas = new JButton("Assistidas");

        JButton btnVerDesejos = new JButton("Desejos");

        JButton btnRemover = new JButton("Remover");

        btnVerFavoritos.setPreferredSize(new Dimension(150,34));
        btnVerAssistidas.setPreferredSize(new Dimension(150,34));
        btnVerDesejos.setPreferredSize(new Dimension(150,34));
        btnRemover.setPreferredSize(new Dimension(130,34));

        linha2.add(lblVisualizar);

        linha2.add(btnVerFavoritos);

        linha2.add(btnVerAssistidas);

        linha2.add(btnVerDesejos);

        linha2.add(btnRemover);



        //----------------------------
        // LINHA 3
        //----------------------------

        JPanel linha3 = new JPanel(new FlowLayout(FlowLayout.CENTER,15,5));

        linha3.setOpaque(false);

        JLabel lblOrdenacao = new JLabel("Ordenar por");

        lblOrdenacao.setFont(new Font("Segoe UI",Font.BOLD,13));

        cbOrdenacao = new JComboBox<>(new String[]{
                "Nome",
                "Nota",
                "Estado",
                "Data de Estreia"
        });

        cbOrdenacao.setPreferredSize(new Dimension(220,34));

        JButton btnOrdenar = new JButton("Ordenar");

        btnOrdenar.setPreferredSize(new Dimension(120,34));

        linha3.add(lblOrdenacao);

        linha3.add(cbOrdenacao);

        linha3.add(btnOrdenar);



        rodape.add(linha1);
        rodape.add(Box.createVerticalStrut(8));
        rodape.add(linha2);
        rodape.add(Box.createVerticalStrut(8));
        rodape.add(linha3);

        add(rodape, BorderLayout.SOUTH);



        //----------------------------
        // EVENTOS
        //----------------------------

        btnFavoritos.addActionListener(e ->
                adicionarSerie(usuario.getFavoritos()));

        btnAssistidas.addActionListener(e ->
                adicionarSerie(usuario.getAssistidas()));

        btnDesejos.addActionListener(e ->
                adicionarSerie(usuario.getDesejaAssistir()));

        btnVerFavoritos.addActionListener(e -> {

            listaAtual = TipoLista.FAVORITOS;
            mostrarLista(usuario.getFavoritos());

        });

        btnVerAssistidas.addActionListener(e -> {

            listaAtual = TipoLista.ASSISTIDAS;
            mostrarLista(usuario.getAssistidas());

        });

        btnVerDesejos.addActionListener(e -> {

            listaAtual = TipoLista.DESEJA_ASSISTIR;
            mostrarLista(usuario.getDesejaAssistir());

        });

        btnRemover.addActionListener(e -> removerSerie());

        btnOrdenar.addActionListener(e -> ordenarLista());

    }
    
    private void buscarSeries() {

        String nome = txtBusca.getText().trim();

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite o nome de uma série para realizar a busca."
            );

            txtBusca.requestFocus();

            return;
        }

        try {

            listaBusca = tvMazeService.buscarSeries(nome);

            listaAtual = TipoLista.BUSCA;
            listaExibida = listaBusca;

            atualizarTabela(listaExibida);

            if (listaBusca.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Nenhuma série encontrada."
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível realizar a busca."
            );
        }
    }

    private void adicionarSerie(List<Serie> listaDestino) {

        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma série."
            );

            return;
        }

        Serie serie = listaExibida.get(linhaSelecionada);

        if (listaDestino.contains(serie)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Esta série já está nesta lista."
            );

            return;
        }

        listaDestino.add(serie);

        jsonService.salvarUsuario(usuario);

        JOptionPane.showMessageDialog(
                this,
                "Série adicionada com sucesso!"
        );
    }

    private void mostrarLista(List<Serie> lista) {

        listaExibida = lista;

        atualizarTabela(listaExibida);
    }

    private void removerSerie() {

        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma série."
            );

            return;
        }

        Serie serie = listaExibida.get(linhaSelecionada);

        switch (listaAtual) {

            case FAVORITOS:

                usuario.getFavoritos().remove(serie);

                break;

            case ASSISTIDAS:

                usuario.getAssistidas().remove(serie);

                break;

            case DESEJA_ASSISTIR:

                usuario.getDesejaAssistir().remove(serie);

                break;

            default:

                JOptionPane.showMessageDialog(
                        this,
                        "Abra uma das listas antes de remover uma série."
                );

                return;
        }

        jsonService.salvarUsuario(usuario);

        atualizarTabela(listaExibida);

        JOptionPane.showMessageDialog(
                this,
                "Série removida com sucesso!"
        );
    }

    private void ordenarLista() {

        if (listaExibida == null || listaExibida.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não há séries para ordenar."
            );

            return;
        }

        String criterio = cbOrdenacao.getSelectedItem().toString();

        switch (criterio) {

            case "Nome":

                ordenacaoService.ordenarPorNome(listaExibida);

                break;

            case "Nota":

                ordenacaoService.ordenarPorNota(listaExibida);

                break;

            case "Estado":

                ordenacaoService.ordenarPorEstado(listaExibida);

                break;

            default:

                ordenacaoService.ordenarPorDataEstreia(listaExibida);

                break;
        }

        atualizarTabela(listaExibida);
    }

    private void atualizarTabela(List<Serie> lista) {

        modeloTabela.setRowCount(0);

        for (Serie serie : lista) {

            modeloTabela.addRow(new Object[]{

                    serie.getNome(),
                    serie.getIdioma(),
                    String.join(", ", serie.getGeneros()),
                    serie.getNota(),
                    serie.getEstado(),
                    serie.getDataEstreia(),
                    serie.getDataTermino(),
                    serie.getEmissora()

            });
        }
    }
}
        
    