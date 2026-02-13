package com.example.moviematch;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SupabaseApi {

    // Этот запрос скачает все строки из таблицы "movies"
    @GET("movies")
    Call<List<MovieCard>> getMovies();

}