package MySeries;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class main {

	static Arquivo jsonArquivo = new Arquivo();
	static File arquivo = new File("Series.json");
	//caminho do arquivo json é guardado la nas variaveis de ambiente do computador para nao ter que ficar trocando a string caso precise usar outro pc
	static String caminho = System.getenv("arquivoJson");
	static Path caminhoArquivo = Paths.get(caminho + arquivo);
	static Series[] series;
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		ConsultarArquivo();
		if(jsonArquivo.getUsuario().isEmpty() || jsonArquivo.getUsuario()==null) {
			TelaIniciar();
		} else {
			TelaPrincipal();
		}
	}

	private static void TelaPrincipal() {
		SwingUtilities.invokeLater(() -> {
			//janela principal do sistema
			JFrame principal = new JFrame("MySeries");
			principal.setSize(1500, 750);
			principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//menu de interação do botão direito do mouse
			JPopupMenu popupMenu = new JPopupMenu();
			JMenuItem fav = new JMenuItem("Salvar como Favorita");
			JMenuItem assistidas = new JMenuItem("Salvar como Serie ja Assistida");
			JMenuItem witchlist = new JMenuItem("Salvar como Pretendo Assistir");
			JMenuItem del = new JMenuItem("Excluir Serie da lista");
			popupMenu.add(fav);
			popupMenu.add(assistidas);
			popupMenu.add(witchlist);
			popupMenu.add(del);
			
			//painel do menu superior
			JPanel menu = new JPanel();
			JButton pesquisar = new JButton("Pesquisar Series");
			JButton favoritos = new JButton("Series Favoritadas");
			JButton assistidos = new JButton("Series Assistidas");
			JButton watchlist = new JButton("Series que Pretendo Assistir");
			menu.add(pesquisar);
			menu.add(favoritos);
			menu.add(watchlist);
			menu.add(assistidos);
			
			//painel inicial
			JPanel inicial = new JPanel();
			JLabel bvnd = new JLabel("Seja Bem Vindo/a/e, " + jsonArquivo.getUsuario());
			inicial.add(bvnd, BorderLayout.CENTER);
			
			//painel da tela de pesquisa de series 
			JPanel pesquisa = new JPanel();
			JTextField campo = new JTextField();
			campo.setPreferredSize(new Dimension(100, 25));
			JButton search = new JButton("Pesquisar");
			JLabel info = new JLabel("Digite o Nome de Serie aqui!(somente Ingles)");
			pesquisa.add(info);
			pesquisa.add(campo);
			pesquisa.add(search);
			
			//painel referente aos resultados da pesquisa das series
			JPanel resultado = new JPanel();
			resultado.setLayout(new BorderLayout());
			String[] colunas = {"ID", "Nome", "Idioma", "Generos", "Nota Geral", "Status Atual", "Data de Estreia", "Data de Termino", "Emissora"};
			DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
			JTable tabela = new JTable(modelo);
			JScrollPane resultados = new JScrollPane(tabela);
			resultado.add(resultados, BorderLayout.CENTER);
			
			//painel referente as listas de series salvas pelo usuario
			JPanel listas = new JPanel();
			String[] colunas1 = {"ID", "Nome", "Idioma", "Generos", "Nota Geral", "Status Atual", "Data de Estreia", "Data de Termino", "Emissora"};
			DefaultTableModel modelo1 = new DefaultTableModel(colunas1, 0);
			JTable tabela1 = new JTable(modelo1);
			JScrollPane listadeseries = new JScrollPane(tabela1);
			listadeseries.setPreferredSize(new Dimension(1450, 500));
			listas.add(listadeseries, BorderLayout.CENTER);
			
			//controlador de paineis dentro do programa
			CardLayout cardControl = new CardLayout();
			
			//tela pai que ira mostrar as telas de pesquisa, resultado e listas
			JPanel telas = new JPanel(cardControl);
			telas.add(pesquisa, "Pesquisa");
			telas.add(resultado, "Resultado");
			telas.add(listas, "Listas");
			telas.add(inicial, "Inicial");
			
			principal.add(menu, BorderLayout.NORTH);
			principal.add(telas, BorderLayout.CENTER);
			cardControl.show(telas, "Inicial");
			principal.setVisible(true);
			
			tabela.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if(e.isPopupTrigger() && e.getComponent() instanceof JTable) {
						JTable source = (JTable) e.getComponent();
						int row = source.rowAtPoint(e.getPoint());
						int column = source.columnAtPoint(e.getPoint());
						
						if (!source.isRowSelected(row)) {
                            source.changeSelection(row, column, false, false);
                        }
						
						fav.setEnabled(true);
						assistidas.setEnabled(true);
						witchlist.setEnabled(true);
						del.setEnabled(false);
                        
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			});
			
			tabela1.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if(e.isPopupTrigger() && e.getComponent() instanceof JTable) {
						JTable source = (JTable) e.getComponent();
						int row = source.rowAtPoint(e.getPoint());
						int column = source.columnAtPoint(e.getPoint());
						
						if (!source.isRowSelected(row)) {
                            source.changeSelection(row, column, false, false);
                        }
						
						fav.setEnabled(false);
						assistidas.setEnabled(false);
						witchlist.setEnabled(false);
						del.setEnabled(true);
                        
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			});
			
			fav.addActionListener(e -> {
				int linhaSelecionada = tabela.getSelectedRow();
				
				if (linhaSelecionada != -1) {
					int serieDados = tabela.convertRowIndexToModel(linhaSelecionada);
					
					Long id = (Long) modelo.getValueAt(serieDados, 0);
					
					SalvarSerie(id, 1);
				}
			});
			
			assistidas.addActionListener(e -> {
				int linhaSelecionada = tabela.getSelectedRow();
				
				if (linhaSelecionada != -1) {
					int serieDados = tabela.convertRowIndexToModel(linhaSelecionada);
					
					Long id = (Long) modelo.getValueAt(serieDados, 0);
					
					SalvarSerie(id, 2);
				}
			});
			
			witchlist.addActionListener(e -> {
				int linhaSelecionada = tabela.getSelectedRow();
				
				if (linhaSelecionada != -1) {
					int serieDados = tabela.convertRowIndexToModel(linhaSelecionada);
					
					Long id = (Long) modelo.getValueAt(serieDados, 0);
					
					SalvarSerie(id, 3);
				}
			});
			
			pesquisar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cardControl.show(telas, "Pesquisa");
				}
			});
			
			favoritos.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelo1.setRowCount(0);
					
					for(int i = 0; i < jsonArquivo.getSeries().size(); i++) {
						if (jsonArquivo.getSeries().get(i).getLista() == 1) {
							modelo1.addRow(new Object[] {
									jsonArquivo.getSeries().get(i).getId(),
									jsonArquivo.getSeries().get(i).getName(),
									jsonArquivo.getSeries().get(i).getLanguage(),
									jsonArquivo.getSeries().get(i).Generos(),
									jsonArquivo.getSeries().get(i).getRating().getAverage(),
									jsonArquivo.getSeries().get(i).getStatus(),
									jsonArquivo.getSeries().get(i).getPremiered(),
									jsonArquivo.getSeries().get(i).getEnded(),
									jsonArquivo.getSeries().get(i).Emissora(),
							});
						}
					}
					
					cardControl.show(telas, "Listas");
					//puxar a lista de series salvas como 1-favoritas
					
					telas.revalidate();
			        telas.repaint();
				}
			});
			
			assistidos.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelo1.setRowCount(0);
					
					for(int i = 0; i < jsonArquivo.getSeries().size(); i++) {
						if (jsonArquivo.getSeries().get(i).getLista() == 2) {
							modelo1.addRow(new Object[] {
									jsonArquivo.getSeries().get(i).getId(),
									jsonArquivo.getSeries().get(i).getName(),
									jsonArquivo.getSeries().get(i).getLanguage(),
									jsonArquivo.getSeries().get(i).Generos(),
									jsonArquivo.getSeries().get(i).getRating().getAverage(),
									jsonArquivo.getSeries().get(i).getStatus(),
									jsonArquivo.getSeries().get(i).getPremiered(),
									jsonArquivo.getSeries().get(i).getEnded(),
									jsonArquivo.getSeries().get(i).Emissora(),
							});
						}
					}
					
					cardControl.show(telas, "Listas");
					//puxar a lista de series salvas como 2-series assistidas
				}
			});
			
			watchlist.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelo1.setRowCount(0);
					
					for(int i = 0; i < jsonArquivo.getSeries().size(); i++) {
						if (jsonArquivo.getSeries().get(i).getLista() == 3) {
							modelo1.addRow(new Object[] {
									jsonArquivo.getSeries().get(i).getId(),
									jsonArquivo.getSeries().get(i).getName(),
									jsonArquivo.getSeries().get(i).getLanguage(),
									jsonArquivo.getSeries().get(i).Generos(),
									jsonArquivo.getSeries().get(i).getRating().getAverage(),
									jsonArquivo.getSeries().get(i).getStatus(),
									jsonArquivo.getSeries().get(i).getPremiered(),
									jsonArquivo.getSeries().get(i).getEnded(),
									jsonArquivo.getSeries().get(i).Emissora(),
							});
						}
					}
					cardControl.show(telas, "Listas");
					//puxar a lista de series salvas como 3-pretendo assistir
				}
			});
			
			search.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelo.setRowCount(0);
					ConsultaSeries(campo.getText());
					if (series != null) {
						for(int i = 0; i < series.length; i++) {
							modelo.addRow(new Object[] {
									series[i].show.get(0).getId(), 
									series[i].show.get(0).getName(), 
									series[i].show.get(0).getLanguage(), 
									series[i].show.get(0).Generos(), 
									series[i].show.get(0).getRating().getAverage(),
									series[i].show.get(0).getStatus(), 
									series[i].show.get(0).getPremiered(), 
									series[i].show.get(0).getEnded(), 
									series[i].show.get(0).Emissora()
							});
						}
					}
					
					cardControl.show(telas, "Resultado");
					
					telas.revalidate();
			        telas.repaint();
				}
			});
		});
	}
	
	private static void ConsultaSeries(String serie) {
		try {
			series = null;
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
			HttpClient client = HttpClient.newHttpClient();
			
			String params = URLEncoder.encode(serie);
			URI url = new URI("https://api.tvmaze.com/search/shows?q=" + params);
		
			HttpRequest request = HttpRequest.newBuilder(url)
					.GET()
					.build();
			
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			
			if(response.statusCode() == 200) {
				System.out.println(response.body());
				String json = response.body();
				//por estarmos recebendo uma array de shows precisamos que a desserialização ocorra dentro de uma array tambem
				series = mapper.readValue(json, Series[].class);
				/*
				for(int i = 0; i < series.length; i++) {
					System.out.println(series[i].resumo());
				}
				*/
			}
		} catch (URISyntaxException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			ErrorMessage(e.getMessage());
		}
	}
	
	private static void SalvarSerie(Long serie, int lista) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			
			HttpClient client = HttpClient.newHttpClient();
			
			URI url = new URI("https://api.tvmaze.com/shows/" + serie);
			
			HttpRequest request = HttpRequest.newBuilder(url)
					.GET()
					.build();
			
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			
			if(response.statusCode() == 200) {
				String json = response.body();
				//como estamos recebendo apenas o show em si podemos desserializa-lo diretamente no objeto Show
				Show serie1 = mapper.readValue(json, Show.class);
				serie1.setLista(lista);
				
				boolean jaExiste = jsonArquivo.getSeries().stream()
				        .anyMatch(s -> s.getId().equals(serie1.getId()));
				
				if(jaExiste) {
					ErrorMessage("essa serie ja foi salva nessa lista");
				} else {
					jsonArquivo.setSeries1(serie1);
					String jsonArchive = mapper.writeValueAsString(jsonArquivo);
					Files.writeString(caminhoArquivo, jsonArchive);
					System.out.println("serie foi adicionada a lista");
				}
			}
		} catch (URISyntaxException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			ErrorMessage(e.getMessage());
		}
	}

	private static void TelaIniciar() {
		JFrame iniciando = new JFrame("Iniciando MySeries...");
		iniciando.setSize(new Dimension(500, 250));
		
		JPanel dados = new JPanel();
		JTextField nome = new JTextField();
		nome.setPreferredSize(new Dimension(100, 25));
		JLabel info = new JLabel("Insira seu nome aqui:");
		JButton iniciar = new JButton("Iniciar");
		dados.add(info);
		dados.add(nome);
		dados.add(iniciar);

		iniciando.add(dados);
		iniciando.setVisible(true);
		
		iniciar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e1) {
				
				if(!nome.getText().isEmpty()) {
					try {
						PegarNome(nome.getText());
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					TelaPrincipal();
					iniciando.dispose();
				} else {
					ErrorMessage("Digite um Nome de Usario Valido");
				}
			}
		});
		
	}
	
	private static void ConsultarArquivo() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			FileReader reader = new FileReader(caminho + arquivo);
			jsonArquivo = mapper.readValue(reader, Arquivo.class);
			//System.out.println(jsonArquivo.resumo());
		} catch (FileNotFoundException e) {
			TelaIniciar();
		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			ErrorMessage(e.getMessage());
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			ErrorMessage(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ErrorMessage(e.getMessage());
		}
		
	}

	private static void PegarNome(String nome) throws JsonProcessingException {
		jsonArquivo.setUsuario(nome);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.registerModule(new JavaTimeModule());
		String json = mapper.writeValueAsString(jsonArquivo);
		try {
			Files.writeString(caminhoArquivo, json);
		}catch (IOException e) {
			ErrorMessage(e.getMessage());
		}
	}
	
	private static void ErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Erro não esperado", JOptionPane.ERROR_MESSAGE);
	}
}
