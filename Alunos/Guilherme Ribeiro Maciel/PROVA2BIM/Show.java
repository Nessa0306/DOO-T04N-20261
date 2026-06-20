package MySeries;

import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Show {
	//ArrayList<Show> show = new ArrayList<>();
	private Long id;
	private String name;
	private String language;
	private ArrayList<String> genres = new ArrayList<>();
	private Rating rating;
	private String status;
	private LocalDate premiered;
	private LocalDate ended;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Network network;
	private int lista;

	public int getLista() {
		return lista;
	}

	public void setLista(int lista) {
		this.lista = lista;
	}

	public Show() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}

	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getPremiered() {
		return premiered;
	}

	public void setPremiered(LocalDate premiered) {
		this.premiered = premiered;
	}

	public LocalDate getEnded() {
		return ended;
	}

	public void setEnded(LocalDate ended) {
		this.ended = ended;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String Emissora() {
		if(getNetwork()!=null) {
			return getNetwork().getEmissora().toString();
		} else {
			return "não televisionado";
		}
	}
	
	public String Lista() {
		switch(lista) {
			case 0:
				return "somente consulta";
			case 1:
				return "Favoritas";
			case 2:
				return "Series ja Assistidas";
			case 3:
				return "Series que Pretendo Assistir";
			default:
				return null;
		}
	}
	
	public String Generos() {
		return String.join(", ", getGenres());
	}
	
	public String sla() {
		return "ID: " + getId() + "Nome: " + getName() + ", Idioma: " + getLanguage() + ", Genero: " + getGenres() + ", Avaliação: " + getRating().getAverage() 
				+ ", Status: " + getStatus() + ", Data de Estreia: " + getPremiered() + ", Data de Encerramento: " + getEnded() + ", Emissora: " + Emissora();
	}
}
