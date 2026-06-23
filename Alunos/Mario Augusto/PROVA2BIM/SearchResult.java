package com.tvtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    private double score;
    private Show show;

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }
}