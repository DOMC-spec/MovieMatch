package com.example.moviematch;

import com.google.gson.annotations.SerializedName;

public class MovieCard {

    // Аннотация @SerializedName указывает точное имя колонки в БД Supabase
    @SerializedName("movie_id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("year")
    private int year; // В БД год хранится как число

    @SerializedName("poster_url")
    private String posterUrl;

    @SerializedName("global_rating")
    private double rating;

    // Геттеры
    public String getId() { return id; }
    public String getTitle() { return title; }

    // Возвращаем год как строку, чтобы адаптеру было проще вставлять его в TextView
    public String getYear() { return String.valueOf(year); }
    public String getPosterUrl() { return posterUrl; }
    public double getRating() { return rating; }
}