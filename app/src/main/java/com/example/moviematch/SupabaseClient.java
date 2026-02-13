package com.example.moviematch;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {
    // ВАЖНО: URL должен обязательно заканчиваться на "/rest/v1/"
    private static final String BASE_URL = "https://pizdiuluwoecrmbagdnx.supabase.co/rest/v1/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBpemRpdWx1d29lY3JtYmFnZG54Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzA4ODQwODksImV4cCI6MjA4NjQ2MDA4OX0.9U4E4bOOcFkIFPFWV8TWGhmnWN2Q8oIMAWKEEwDn2lI";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Настраиваем перехватчик, чтобы добавлять ключ в каждый запрос
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("apikey", API_KEY)
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}