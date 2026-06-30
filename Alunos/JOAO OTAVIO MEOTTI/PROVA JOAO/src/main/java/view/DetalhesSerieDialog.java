package view;

import model.Serie;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class DetalhesSerieDialog extends JDialog {
    private final Serie serie;
    private final JLabel labelImagem;

    private DetalhesSerieDialog(Window janelaPai, Serie serie) {
        super(janelaPai, "Detalhes - " + serie.getNome(), ModalityType.APPLICATION_MODAL);
        this.serie = serie;
        this.labelImagem = new JLabel("Carregando imagem...", SwingConstants.CENTER);

        configurarJanela();
        construirInterface();
        carregarImagem();
    }

    public static void exibir(Component componentePai, Serie serie) {
        if (serie == null) {
            JOptionPane.showMessageDialog(componentePai, "Selecione uma série primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Window janelaPai = SwingUtilities.getWindowAncestor(componentePai);
        DetalhesSerieDialog dialog = new DetalhesSerieDialog(janelaPai, serie);
        dialog.setVisible(true);
    }

    private void configurarJanela() {
        setSize(760, 520);
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void construirInterface() {
        JPanel painelPrincipal = TelaPrincipal.criarPainelBase();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel card = TelaPrincipal.criarPainelArredondado(TelaPrincipal.COR_CARD_CLARO, 10, new Insets(20, 20, 20, 20));
        card.setLayout(new BorderLayout(18, 0));

        JPanel painelImagem = criarPainelImagem();
        JPanel painelInformacoes = criarPainelInformacoes();

        card.add(painelImagem, BorderLayout.WEST);
        card.add(painelInformacoes, BorderLayout.CENTER);

        painelPrincipal.add(card, BorderLayout.CENTER);
        add(painelPrincipal);
    }

    private JPanel criarPainelImagem() {
        JPanel painel = TelaPrincipal.criarPainelArredondado(TelaPrincipal.COR_CARD_SUAVE, 10, new Insets(14, 14, 14, 14));
        painel.setPreferredSize(new Dimension(230, 0));
        painel.setLayout(new BorderLayout(0, 16));

        labelImagem.setPreferredSize(new Dimension(200, 300));
        labelImagem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelImagem.setForeground(TelaPrincipal.COR_TEXTO_MUTED);
        labelImagem.setBackground(Color.WHITE);
        labelImagem.setOpaque(true);
        labelImagem.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel nota = new JLabel(" " + String.format("%.1f", serie.getNota()) + " / 10", SwingConstants.CENTER);
        nota.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nota.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        JLabel status = new JLabel(valorOuPadrao(serie.getStatus()), SwingConstants.CENTER);
        status.setFont(new Font("Segoe UI", Font.BOLD, 13));
        status.setForeground(TelaPrincipal.COR_CINZA);

        JPanel rodapeImagem = new JPanel(new GridLayout(2, 1, 0, 3));
        rodapeImagem.setOpaque(false);
        rodapeImagem.add(nota);
        rodapeImagem.add(status);

        painel.add(labelImagem, BorderLayout.CENTER);
        painel.add(rodapeImagem, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPainelInformacoes() {
        JPanel painel = new JPanel(new BorderLayout(0, 18));
        painel.setOpaque(false);

        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("<html>" + valorOuPadrao(serie.getNome()) + "</html>");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel generos = new JLabel(valorOuPadrao(serie.getGeneros()));
        generos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        generos.setForeground(TelaPrincipal.COR_TEXTO_MUTED);
        generos.setAlignmentX(Component.LEFT_ALIGNMENT);

        topo.add(titulo);
        topo.add(Box.createVerticalStrut(6));
        topo.add(generos);

        JPanel gridDados = new JPanel(new GridLayout(2, 2, 12, 12));
        gridDados.setOpaque(false);
        gridDados.add(criarMiniCard("Estreia", valorOuPadrao(serie.getDataEstreia()), TelaPrincipal.COR_ROXO));
        gridDados.add(criarMiniCard("Encerramento", valorOuPadrao(serie.getDataFim()), TelaPrincipal.COR_ROXO));
        gridDados.add(criarMiniCard("Canal", valorOuPadrao(serie.getEmissora()), TelaPrincipal.COR_ROXO));
        gridDados.add(criarMiniCard("Idioma", valorOuPadrao(serie.getIdioma()), TelaPrincipal.COR_ROXO));

        JTextArea areaDetalhes = new JTextArea("Descrição\n" + valorOuPadrao(serie.getSumario()) + "\n\nImagem\n" + valorOuPadrao(serie.getImagemUrl()));
        areaDetalhes.setEditable(false);
        areaDetalhes.setLineWrap(true);
        areaDetalhes.setWrapStyleWord(true);
        areaDetalhes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaDetalhes.setForeground(TelaPrincipal.COR_TEXTO_MAIN);
        areaDetalhes.setBackground(TelaPrincipal.COR_CARD_SUAVE);
        areaDetalhes.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JScrollPane scrollDetalhes = TelaPrincipal.criarScrollPadrao(areaDetalhes);
        scrollDetalhes.getViewport().setBackground(TelaPrincipal.COR_CARD_SUAVE);

        JButton botaoFechar = TelaPrincipal.criarBotaoEstilizado("Fechar", TelaPrincipal.COR_CINZA);
        botaoFechar.addActionListener(e -> dispose());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rodape.setOpaque(false);
        rodape.add(botaoFechar);

        painel.add(topo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.setOpaque(false);
        centro.add(gridDados, BorderLayout.NORTH);
        centro.add(scrollDetalhes, BorderLayout.CENTER);

        painel.add(centro, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarMiniCard(String titulo, String valor, Color cor) {
        JPanel card = TelaPrincipal.criarPainelArredondado(TelaPrincipal.COR_CARD_SUAVE, 10, new Insets(10, 12, 10, 12));
        card.setLayout(new BorderLayout(0, 4));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(cor);

        JLabel lblValor = new JLabel("<html>" + valor + "</html>");
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblValor.setForeground(TelaPrincipal.COR_TEXTO_MAIN);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    private String valorOuPadrao(String valor) {
        if (valor == null || valor.isBlank()) {
            return "Não informado";
        }
        return valor;
    }

    private void carregarImagem() {
        String imagemUrl = serie.getImagemUrl();
        if (imagemUrl == null || imagemUrl.isBlank()) {
            labelImagem.setText("Imagem não disponível");
            return;
        }

        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                BufferedImage imagem = ImageIO.read(new URL(imagemUrl));
                if (imagem == null) {
                    return null;
                }

                Image imagemRedimensionada = imagem.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                return new ImageIcon(imagemRedimensionada);
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icone = get();
                    if (icone == null) {
                        labelImagem.setText("Imagem não disponível");
                    } else {
                        labelImagem.setText("");
                        labelImagem.setIcon(icone);
                    }
                } catch (Exception e) {
                    labelImagem.setText("Não foi possível carregar a imagem");
                }
            }
        };

        worker.execute();
    }
}
