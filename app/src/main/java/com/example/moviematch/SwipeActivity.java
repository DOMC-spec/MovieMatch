package com.example.moviematch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeActivity extends AppCompatActivity implements CardStackListener {

    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private SwipeAdapter adapter;
    private List<MovieCard> movieList = new ArrayList<>();

    // Переменная для хранения ID текущего пользователя
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        // Достаем ID пользователя из памяти
        SharedPreferences prefs = getSharedPreferences("MovieMatchPrefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getString("user_id", null);

        cardStackView = findViewById(R.id.card_stack);
        manager = new CardStackLayoutManager(this, this);

        adapter = new SwipeAdapter(movieList);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        setupButtons();
        fetchMoviesFromDatabase();
    }

    private void fetchMoviesFromDatabase() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        api.getMovies().enqueue(new Callback<List<MovieCard>>() {
            @Override
            public void onResponse(Call<List<MovieCard>> call, Response<List<MovieCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList.clear();
                    movieList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SwipeActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MovieCard>> call, Throwable t) {
                Toast.makeText(SwipeActivity.this, "Проверьте интернет", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- МЕТОД СОХРАНЕНИЯ ---
    private void saveSwipeToDatabase(String movieId, String status) {
        if (currentUserId == null) return; // Защита от ошибок

        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        // Создаем запись с переданным статусом (PLANNED или DROPPED)
        LibraryItem item = new LibraryItem(currentUserId, movieId, status);

        api.upsertToLibrary(item).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MovieMatch", "Свайп сохранен со статусом: " + status);
                } else {
                    Log.e("MovieMatch", "Ошибка сохранения свайпа: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MovieMatch", "Ошибка сети при свайпе: " + t.getMessage());
            }
        });
    }

    private void setupButtons() {
        View btnLike = findViewById(R.id.btn_like);
        if (btnLike != null) {
            btnLike.setOnClickListener(v -> {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            });
        }

        View btnDislike = findViewById(R.id.btn_dislike);
        if (btnDislike != null) {
            btnDislike.setOnClickListener(v -> {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            });
        }

        View btnInfo = findViewById(R.id.btn_info);
        if (btnInfo != null) {
            btnInfo.setOnClickListener(v -> {
                // Узнаем, какая карточка сейчас находится на самом верху стопки
                int currentPosition = manager.getTopPosition();

                // Проверяем, остались ли еще фильмы
                if (currentPosition < adapter.getItemCount()) {
                    MovieCard currentMovie = adapter.getMovies().get(currentPosition);

                    // Передаем данные в окно деталей
                    Intent intent = new Intent(SwipeActivity.this, MovieDetailsActivity.class);
                    intent.putExtra("title", currentMovie.getTitle());
                    intent.putExtra("year", currentMovie.getYear());
                    intent.putExtra("rating", String.valueOf(currentMovie.getRating()));
                    intent.putExtra("description", currentMovie.getDescription());
                    intent.putExtra("posterUrl", currentMovie.getPosterUrl());

                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Фильмы закончились!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        View navProfile = findViewById(R.id.nav_profile);
        View navFriends = findViewById(R.id.nav_friends);
        View navSwipe = findViewById(R.id.nav_swipe_action);

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }

        if (navFriends != null) {
            navFriends.setOnClickListener(v -> {
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }

        if (navSwipe != null) {
            navSwipe.setOnClickListener(v -> {
                // Ничего не делаем, мы уже тут
            });
        }
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
            // Свайп вправо -> В планах
            saveSwipeToDatabase(swipedMovie.getId(), "PLANNED");

        } else if (direction == Direction.Left) {
            Log.d("SwipeActivity", "Дизлайк: " + swipedMovie.getTitle());
            // Свайп влево -> Дизлайк (или посмотрел и не понравилось)
            saveSwipeToDatabase(swipedMovie.getId(), "DROPPED");
        }

        if (manager.getTopPosition() == adapter.getItemCount()) {
            Toast.makeText(this, "Фильмы закончились!", Toast.LENGTH_SHORT).show();
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