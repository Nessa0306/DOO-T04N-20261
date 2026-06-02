package MySeries;

import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Show {
	//ArrayList<Show> show = new ArrayList<>();
	int id;
	String name;
	String language;
	ArrayList<String> genres = new ArrayList<>();
	ArrayList<Rating> rating = new ArrayList<>();
	String status;
	LocalDate premiered;
	LocalDate ended;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	ArrayList<Network> network = new ArrayList<>();

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

	public ArrayList<Rating> getRating() {
		return rating;
	}

	public void setRating(ArrayList<Rating> rating) {
		this.rating = rating;
	}

	public ArrayList<Network> getNetwork() {
		return network;
	}

	public void setNetwork(ArrayList<Network> network) {
		this.network = network;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String Emissora() {
		if(getNetwork()!=null) {
			return getNetwork().get(0).getEmissora().toString();
		} else {
			return "não televisionado";
		}
	}
	
	public String sla() {
		return "Nome: " + getName() + ", Idioma: " + getLanguage() + ", Genero: " + getGenres() + ", Avaliação: " + getRating().get(0).getAverage() 
				+ ", Status: " + getStatus() + ", Data de Estreia: " + getPremiered() + ", Data de Encerramento:" + getEnded() + ", Emissora: " + Emissora();
	}
}
