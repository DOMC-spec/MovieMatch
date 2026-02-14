package com.example.moviematch;

import com.google.gson.annotations.SerializedName;

public class LibraryItem {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("movie_id")
    private String movieId;

    @SerializedName("status")
    private String status;

    public LibraryItem(String userId, String movieId, String status) {
        this.userId = userId;
        this.movieId = movieId;
        this.status = status;
    }
}