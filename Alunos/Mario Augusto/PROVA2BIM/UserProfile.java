package com.tvtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    private String username;
    private List<Show> favorites  = new ArrayList<>();
    private List<Show> watched    = new ArrayList<>();
    private List<Show> watchlist  = new ArrayList<>();

    public UserProfile() {}
    public UserProfile(String username) { this.username = username; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public List<Show> getFavorites() { return favorites; }
    public void setFavorites(List<Show> v) { this.favorites = v; }
    public List<Show> getWatched() { return watched; }
    public void setWatched(List<Show> v) { this.watched = v; }
    public List<Show> getWatchlist() { return watchlist; }
    public void setWatchlist(List<Show> v) { this.watchlist = v; }

    public boolean addToFavorites(Show s) {
        if (favorites.contains(s)) return false;
        return favorites.add(s);
    }
    public boolean removeFromFavorites(Show s) { return favorites.remove(s); }

    public boolean addToWatched(Show s) {
        if (watched.contains(s)) return false;
        return watched.add(s);
    }
    public boolean removeFromWatched(Show s) { return watched.remove(s); }

    public boolean addToWatchlist(Show s) {
        if (watchlist.contains(s)) return false;
        return watchlist.add(s);
    }
    public boolean removeFromWatchlist(Show s) { return watchlist.remove(s); }
}