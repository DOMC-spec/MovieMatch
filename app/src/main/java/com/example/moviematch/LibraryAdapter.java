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

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {

    private List<LibraryItemResponse> libraryItems;

    public LibraryAdapter(List<LibraryItemResponse> libraryItems) {
        this.libraryItems = libraryItems;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library_movie, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        LibraryItemResponse item = libraryItems.get(position);
        MovieCard movie = item.getMovie();

        if (movie != null) {
            holder.title.setText(movie.getTitle() != null ? movie.getTitle() : "Без названия");
            holder.meta.setText(movie.getYear() + " • IMDb: " + movie.getRating());

            Glide.with(holder.itemView.getContext())
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.bg_poster_placeholder)
                    .centerCrop()
                    .into(holder.poster);
        }

        // Если у пользователя уже стоит оценка - показываем её, если нет - кнопку "Оценить"
        if (item.getPersonalRating() != null && item.getPersonalRating() > 0) {
            holder.layoutRatingDone.setVisibility(View.VISIBLE);
            holder.layoutRatingAdd.setVisibility(View.GONE);
            holder.ratingValue.setText(String.valueOf(item.getPersonalRating()));
        } else {
            holder.layoutRatingDone.setVisibility(View.GONE);
            holder.layoutRatingAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return libraryItems.size();
    }

    static class LibraryViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, meta, ratingValue;
        View layoutRatingDone, layoutRatingAdd;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster);
            title = itemView.findViewById(R.id.movie_title);
            meta = itemView.findViewById(R.id.movie_meta);
            layoutRatingDone = itemView.findViewById(R.id.layout_rating_done);
            layoutRatingAdd = itemView.findViewById(R.id.layout_rating_add);
            ratingValue = itemView.findViewById(R.id.rating_value);
        }
    }
}