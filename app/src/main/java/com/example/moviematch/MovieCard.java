package com.example.moviematch;

public class MovieCard {
    private String id;
    private String title;
    private String year;
    private String posterUrl;
    private double rating;

    public MovieCard(String id, String title, String year, String posterUrl, double rating) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.posterUrl = posterUrl;
        this.rating = rating;
    }

    // Геттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getPosterUrl() { return posterUrl; }
    public double getRating() { return rating; }
}