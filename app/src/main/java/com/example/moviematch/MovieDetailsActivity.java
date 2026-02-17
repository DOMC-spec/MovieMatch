package com.example.moviematch;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // 1. Находим элементы на экране по их ID из твоего макета
        ImageView backdrop = findViewById(R.id.movie_backdrop);
        ImageButton btnBack = findViewById(R.id.btn_back);
        TextView title = findViewById(R.id.detail_title);
        TextView synopsis = findViewById(R.id.detail_synopsis); // Это ID для описания в твоем макете
        ImageButton btnDislike = findViewById(R.id.btn_dislike_detail);
        ImageButton btnLike = findViewById(R.id.btn_like_detail);

        // 2. Достаем переданные данные из Intent
        String movieTitle = getIntent().getStringExtra("title");
        String movieDesc = getIntent().getStringExtra("description");
        String moviePosterUrl = getIntent().getStringExtra("posterUrl");
        // Год и рейтинг пока не подставляем, так как в твоем макете у чипсов (Chip) нет ID.
        // Если хочешь их тоже динамически менять, добавь им ID в activity_movie_details.xml!

        // 3. Подставляем данные в элементы
        if (title != null) {
            title.setText(movieTitle != null ? movieTitle : "Без названия");
        }

        if (synopsis != null) {
            synopsis.setText(movieDesc != null ? movieDesc : "Описание пока недоступно.");
        }

        // 4. Загружаем картинку постера через Glide
        if (backdrop != null && moviePosterUrl != null) {
            Glide.with(this)
                    .load(moviePosterUrl)
                    .placeholder(R.drawable.bg_poster_placeholder) // Показываем заглушку, пока грузится
                    .centerCrop()
                    .into(backdrop);
        }

        // 5. Настраиваем кнопки
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish()); // Закрыть экран
        }

        if (btnDislike != null) {
            btnDislike.setOnClickListener(v -> {
                // При дизлайке просто возвращаемся назад
                Toast.makeText(this, "Возвращаемся к выбору", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        if (btnLike != null) {
            btnLike.setOnClickListener(v -> {
                // При лайке тоже пока просто возвращаемся.
                // Позже можно будет добавить тут логику свайпа верхней карточки.
                Toast.makeText(this, "Отличный выбор!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}