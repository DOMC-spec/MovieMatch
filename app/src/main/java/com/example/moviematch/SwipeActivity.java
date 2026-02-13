package com.example.moviematch;

import android.content.Intent;
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
    // Создаем пустой список, который позже заполним данными из БД
    private List<MovieCard> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        cardStackView = findViewById(R.id.card_stack);
        manager = new CardStackLayoutManager(this, this);

        // Сначала передаем адаптеру пустой список
        adapter = new SwipeAdapter(movieList);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        setupButtons();

        // Запускаем скачивание фильмов с сервера
        fetchMoviesFromDatabase();
    }

    // --- МЕТОД ДЛЯ РАБОТЫ С БАЗОЙ ДАННЫХ ---
    private void fetchMoviesFromDatabase() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);

        // enqueue выполняет сетевой запрос асинхронно (в фоновом потоке),
        // чтобы приложение не зависло во время скачивания
        api.getMovies().enqueue(new Callback<List<MovieCard>>() {
            @Override
            public void onResponse(Call<List<MovieCard>> call, Response<List<MovieCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Очищаем старый список и добавляем скачанные фильмы
                    movieList.clear();
                    movieList.addAll(response.body());

                    // Говорим адаптеру: "Эй, данные обновились, перерисуй карточки!"
                    adapter.notifyDataSetChanged();

                    Log.d("Supabase", "Успешно загружено фильмов: " + movieList.size());
                } else {
                    Log.e("Supabase", "Ошибка ответа сервера: " + response.code());
                    Toast.makeText(SwipeActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MovieCard>> call, Throwable t) {
                Log.e("Supabase", "Сетевая ошибка: " + t.getMessage());
                Toast.makeText(SwipeActivity.this, "Проверьте интернет", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtons() {
        // Кнопка Лайк
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

        // Кнопка Дизлайк (крестик)
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

        // Кнопка Инфо (посередине)
        View btnInfo = findViewById(R.id.btn_info);
        if (btnInfo != null) {
            btnInfo.setOnClickListener(v -> {
                Toast.makeText(this, "Открытие деталей фильма...", Toast.LENGTH_SHORT).show();
            });
        }

        // Кнопки нижней панели (Bottom Navigation)
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
                Toast.makeText(this, "Переход к Друзьям/Комнатам", Toast.LENGTH_SHORT).show();
            });
        }

        if (navSwipe != null) {
            navSwipe.setOnClickListener(v -> {
                Toast.makeText(this, "Ты уже на экране свайпов!", Toast.LENGTH_SHORT).show();
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
            // TODO: В будущем добавим сохранение лайка в личную библиотеку
        } else if (direction == Direction.Left) {
            Log.d("SwipeActivity", "Дизлайк: " + swipedMovie.getTitle());
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