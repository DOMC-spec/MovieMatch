package com.example.moviematch;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseApi {
    // Этот запрос скачает все строки из таблицы "movies"
    @GET("movies")
    Call<List<MovieCard>> getMovies();

    // ПОЛУЧИТЬ пользователя по никнейму
    @GET("users")
    Call<List<User>> getUserByNickname(@Query("nickname") String nicknameQuery);

    // СОЗДАТЬ нового пользователя.
    // Заголовок "Prefer: return=representation" говорит серверу вернуть нам созданную строку,
    // чтобы мы могли получить сгенерированный базой user_id
    @POST("users")
    @Headers("Prefer: return=representation")
    Call<List<User>> createUser(@Body User user);
}