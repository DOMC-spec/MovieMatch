package com.example.moviematch;

import com.google.gson.annotations.SerializedName;

public class LibraryItemResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("personal_rating")
    private Integer personalRating;

    // Supabase вернет вложенный объект с данными фильма по связи (foreign key)
    @SerializedName("movies")
    private MovieCard movie;

    public String getStatus() { return status; }
    public Integer getPersonalRating() { return personalRating; }
    public MovieCard getMovie() { return movie; }
}