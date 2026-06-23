package com.tvtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Show {
    private int id;
    private String name;
    private String language;
    private List<String> genres;
    private String status;
    private String premiered;
    private String ended;
    private Rating rating;
    private Network network;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        private Double average;
        public Double getAverage() { return average; }
        public void setAverage(Double average) { this.average = average; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPremiered() { return premiered; }
    public void setPremiered(String premiered) { this.premiered = premiered; }
    public String getEnded() { return ended; }
    public void setEnded(String ended) { this.ended = ended; }
    public Rating getRating() { return rating; }
    public void setRating(Rating rating) { this.rating = rating; }
    public Network getNetwork() { return network; }
    public void setNetwork(Network network) { this.network = network; }

    public double getRatingAverage() {
        if (rating != null && rating.getAverage() != null) return rating.getAverage();
        return 0.0;
    }

    public String getNetworkName() {
        if (network != null && network.getName() != null) return network.getName();
        return "N/A";
    }

    public String getGenresString() {
        if (genres == null || genres.isEmpty()) return "N/A";
        return String.join(", ", genres);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Show)) return false;
        return id == ((Show) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }

    @Override
    public String toString() { return name != null ? name : "Unknown"; }
}