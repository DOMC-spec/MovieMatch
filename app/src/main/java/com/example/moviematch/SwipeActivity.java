package com.example.moviematch;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity implements CardStackListener {

    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private SwipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        // Инициализация менеджера и карточек
        manager = new CardStackLayoutManager(this, this);
        adapter = new SwipeAdapter(createDummyMovies());

        cardStackView = findViewById(R.id.card_stack); // ID FrameLayout/CardStackView из activity_swipe.xml
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        // Настройка кнопок
        setupButtons();
    }

    private void setupButtons() {
        // Кнопка Лайк
        View btnLike = findViewById(R.id.btn_like); // Проверь ID в макете
        if (btnLike != null) {
            btnLike.setOnClickListener(v -> {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            });
        }

        // Кнопка Дизлайк (крестик)
        View btnDislike = findViewById(R.id.btn_dislike); // Проверь ID в макете
        if (btnDislike != null) {
            btnDislike.setOnClickListener(v -> {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            });
        }
    }

    // Создаем тестовые данные для проверки дизайна
    private List<MovieCard> createDummyMovies() {
        List<MovieCard> movies = new ArrayList<>();
        movies.add(new MovieCard("1", "Интерстеллар", "2014", "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MvrIdZ2O.jpg", 8.6));
        movies.add(new MovieCard("2", "Начало", "2010", "https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg", 8.8));
        movies.add(new MovieCard("3", "Матрица", "1999", "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", 8.7));
        return movies;
    }

    // --- Методы интерфейса CardStackListener ---

    @Override
    public void onCardDragging(Direction direction, float ratio) { }

    @Override
    public void onCardSwiped(Direction direction) {
        int position = manager.getTopPosition() - 1;
        MovieCard swipedMovie = adapter.getMovies().get(position);

        if (direction == Direction.Right) {
            Log.d("SwipeActivity", "Лайк: " + swipedMovie.getTitle());
            // TODO: Здесь будем отправлять лайк в Supabase
        } else if (direction == Direction.Left) {
            Log.d("SwipeActivity", "Дизлайк: " + swipedMovie.getTitle());
            // TODO: Здесь будем сохранять дизлайк
        }

        // Если карточки заканчиваются, можно загрузить новые
        if (manager.getTopPosition() == adapter.getItemCount()) {
            Log.d("SwipeActivity", "Карточки закончились!");
        }
    }

    @Override
    public void onCardRewound() { }

    @Override
    public void onCardCanceled() { }

    @Override
    public void onCardAppeared(View view, int position) { }

    @Override
    public void onCardDisappeared(View view, int position) { }
}