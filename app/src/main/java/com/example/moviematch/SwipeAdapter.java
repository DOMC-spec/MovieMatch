package com.example.moviematch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.ViewHolder> {

    private List<MovieCard> movies;

    public SwipeAdapter(List<MovieCard> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Загружаем наш XML макет карточки
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_swipe_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieCard movie = movies.get(position);

        // Устанавливаем текст
        holder.titleText.setText(movie.getTitle());
        holder.yearText.setText(movie.getYear());
        holder.ratingText.setText("IMDb " + movie.getRating());

        // Загружаем картинку с помощью Glide
        Glide.with(holder.posterImage.getContext())
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.bg_poster_placeholder) // Показываем серый фон, пока грузится
                .centerCrop()
                .into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public List<MovieCard> getMovies() {
        return movies;
    }

    // Класс, который хранит ссылки на элементы управления (Views)
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;
        TextView titleText;
        TextView yearText;
        TextView ratingText;

        ViewHolder(View view) {
            super(view);
            // Привязываем переменные к ID из item_swipe_card.xml
            posterImage = view.findViewById(R.id.movie_poster);
            titleText = view.findViewById(R.id.movie_title);
            yearText = view.findViewById(R.id.movie_year);
            ratingText = view.findViewById(R.id.movie_rating);
        }
    }
}