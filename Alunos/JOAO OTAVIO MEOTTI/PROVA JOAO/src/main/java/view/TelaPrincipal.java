package view;

import controller.UsuarioController;
import model.Serie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaPrincipal extends JFrame {
    private final UsuarioController usuarioController;

    protected static final Color COR_FUNDO_TOPO       = new Color(245, 247, 250);
    protected static final Color COR_FUNDO_BASE       = new Color(235, 239, 245);
    protected static final Color COR_CARD_CLARO       = Color.WHITE;
    protected static final Color COR_CARD_ESCURO      = new Color(52, 64, 84);
    protected static final Color COR_CARD_SUAVE       = new Color(247, 249, 252);
    protected static final Color COR_TEXTO_MAIN       = new Color(31, 41, 55);
    protected static final Color COR_TEXTO_MUTED      = new Color(100, 116, 139);
    protected static final Color COR_TEXTO_CLARO      = Color.WHITE;
    protected static final Color COR_TEXTO_CLARO_MUTED= new Color(226, 232, 240);
    protected static final Color COR_BORDA            = new Color(203, 213, 225);
    protected static final Color COR_LILAS_CLARO      = new Color(239, 246, 255);
    protected static final Color COR_ROSA_CLARO       = new Color(252, 231, 243);
    protected static final Color COR_VERDE_CLARO      = new Color(220, 252, 231);
    protected static final Color COR_ROXO             = new Color(59, 130, 246);
    protected static final Color COR_ROSA             = new Color(219, 39, 119);
    protected static final Color COR_VERDE            = new Color(22, 163, 74);
    protected static final Color COR_AMARELO          = new Color(217, 119, 6);
    protected static final Color COR_VERMELHO         = new Color(220, 38, 38);
    protected static final Color COR_CINZA            = new Color(71, 85, 105);
    protected static final Color COR_AZUL_PADRAO      = COR_ROXO;

    public TelaPrincipal(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        configurarJanela();
        construirInterfaceGrafica();
    }

    private void configurarJanela() {
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exibirConfirmacaoSaida();
            }
        });
    }

    private void construirInterfaceGrafica() {
        JPanel painelPrincipal = criarPainelBase();
        painelPrincipal.setLayout(new BorderLayout());

        JPanel card = criarCardPrincipal();
        card.setLayout(new BorderLayout(0, 22));
        card.add(criarCabecalhoSimples(), BorderLayout.NORTH);
        card.add(criarResumoSimples(), BorderLayout.CENTER);
        card.add(criarPainelBotoes(), BorderLayout.SOUTH);

        painelPrincipal.add(card, BorderLayout.CENTER);
        add(painelPrincipal);
    }

    private JPanel criarCabecalhoSimples() {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel marca = new JLabel("");
        marca.setFont(new Font("Segoe UI", Font.BOLD, 32));
        marca.setForeground(COR_TEXTO_MAIN);
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel saudacao = new JLabel("Olá, " + valorOuPadrao(usuarioController.getUsuario().getNome()) + ". Escolha uma opção abaixo.");
        saudacao.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        saudacao.setForeground(COR_TEXTO_MUTED);
        saudacao.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(marca);
        painel.add(Box.createVerticalStrut(6));
        painel.add(saudacao);
        return painel;
    }

    private JPanel criarResumoSimples() {
        JPanel painel = criarPainelArredondado(COR_CARD_SUAVE, 14, new Insets(20, 22, 20, 22));
        painel.setLayout(new GridLayout(3, 1, 0, 10));

        painel.add(criarLinhaResumo("Favoritos", usuarioController.getUsuario().getFavoritos().size()));
        painel.add(criarLinhaResumo("Assistidos", usuarioController.getUsuario().getAssistidos().size()));
        painel.add(criarLinhaResumo("Quero ver", usuarioController.getUsuario().getQueroAssistir().size()));
        return painel;
    }

    private JPanel criarLinhaResumo(String titulo, int valor) {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(COR_TEXTO_MAIN);

        JLabel lblValor = new JLabel(String.valueOf(valor));
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValor.setForeground(COR_ROXO);

        linha.add(lblTitulo, BorderLayout.WEST);
        linha.add(lblValor, BorderLayout.EAST);
        return linha;
    }

    private JPanel criarPainelBotoes() {
        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 12, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.setPreferredSize(new Dimension(0, 60));

        JButton botaoAbrirBusca = criarBotaoEstilizado("Buscar filmes e séries", COR_ROXO);
        JButton botaoAbrirListas = criarBotaoEstilizado("Minhas listas", COR_VERDE);
        JButton botaoSairSistema = criarBotaoEstilizado("Salvar e sair", COR_CINZA);

        botaoAbrirBusca.addActionListener(e -> {
            new TelaBusca(usuarioController).setVisible(true);
            dispose();
        });
        botaoAbrirListas.addActionListener(e -> {
            new TelaListas(usuarioController).setVisible(true);
            dispose();
        });
        botaoSairSistema.addActionListener(e -> exibirConfirmacaoSaida());

        painelBotoes.add(botaoAbrirBusca);
        painelBotoes.add(botaoAbrirListas);
        painelBotoes.add(botaoSairSistema);

        return painelBotoes;
    }

    protected static JPanel criarPainelBase() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO_BASE);
        painel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        return painel;
    }

    protected static JPanel criarCardPrincipal() {
        JPanel card = criarPainelArredondado(COR_CARD_CLARO, 14, new Insets(28, 28, 28, 28));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        return card;
    }

    protected static JPanel criarPainelArredondado(Color cor, int raio, Insets padding) {
        JPanel painel = new JPanel();
        painel.setBackground(cor);
        painel.setOpaque(true);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right)
        ));
        return painel;
    }

    protected static JLabel criarBadge(String texto, Color fundo, Color frente) {
        JLabel badge = new JLabel(texto, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(frente);
        badge.setBackground(fundo);
        badge.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        return badge;
    }

    protected static JScrollPane criarScrollPadrao(Component componente) {
        JScrollPane scroll = new JScrollPane(componente);
        scroll.setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COR_CARD_CLARO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    public static JButton criarBotaoEstilizado(String texto, Color corBase) {
        return new BotaoArredondado(texto, corBase, Color.WHITE);
    }

    public static JButton criarBotaoClaro(String texto, Color corTexto) {
        return new BotaoArredondado(texto, new Color(245, 239, 252), corTexto);
    }

    protected static void configurarListaPadrao(JList<Serie> lista) {
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lista.setBackground(COR_CARD_CLARO);
        lista.setForeground(COR_TEXTO_MAIN);
        lista.setSelectionBackground(COR_LILAS_CLARO);
        lista.setSelectionForeground(COR_TEXTO_MAIN);
        lista.setFixedCellHeight(68);
        lista.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        lista.setOpaque(false);
    }

    private String valorOuPadrao(String valor) {
        return (valor == null || valor.isBlank()) ? "usuário" : valor;
    }

    private void exibirConfirmacaoSaida() {
        int resultado = JOptionPane.showConfirmDialog(this, "Deseja salvar e sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resultado == JOptionPane.YES_OPTION) {
            try {
                usuarioController.salvarDados();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    }

    private static class PainelGradiente extends JPanel {
        private final Color inicio;
        private final Color fim;

        private PainelGradiente(Color inicio, Color fim) {
            this.inicio = inicio;
            this.fim = fim;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, inicio, getWidth(), getHeight(), fim));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 84, 154, 34));
            g2.fillOval(-90, -110, 260, 260);
            g2.setColor(new Color(123, 77, 255, 48));
            g2.fillOval(getWidth() - 170, getHeight() - 170, 310, 310);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class PainelArredondado extends JPanel {
        private final Color cor;
        private final int raio;

        private PainelArredondado(Color cor, int raio) {
            this.cor = cor;
            this.raio = raio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 8, raio, raio);
            g2.setColor(cor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, raio, raio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class BotaoArredondado extends JButton {
        private final Color corBase;
        private final Color corHover;
        private final Color corTexto;
        private boolean hover;

        private BotaoArredondado(String texto, Color corBase, Color corTexto) {
            super(texto);
            this.corBase = corBase;
            this.corHover = clarear(corBase, 22);
            this.corTexto = corTexto;

            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(corTexto);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color corAtual = hover ? corHover : corBase;
            if (getModel().isPressed()) {
                corAtual = escurecer(corBase, 18);
            }
            g2.setColor(corAtual);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
            setForeground(corTexto);
        }

        private static Color clarear(Color cor, int valor) {
            return new Color(
                    Math.min(255, cor.getRed() + valor),
                    Math.min(255, cor.getGreen() + valor),
                    Math.min(255, cor.getBlue() + valor)
            );
        }

        private static Color escurecer(Color cor, int valor) {
            return new Color(
                    Math.max(0, cor.getRed() - valor),
                    Math.max(0, cor.getGreen() - valor),
                    Math.max(0, cor.getBlue() - valor)
            );
        }
    }
}
