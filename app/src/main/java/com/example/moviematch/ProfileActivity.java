package com.example.moviematch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Подключаем готовый макет профиля
        setContentView(R.layout.profile);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        // Ищем элементы нижней панели
        View navSwipe = findViewById(R.id.nav_swipe_action);
        View navProfile = findViewById(R.id.nav_profile);
        View navFriends = findViewById(R.id.nav_friends);

        // Кнопка возврата на экран свайпов (посередине)
        if (navSwipe != null) {
            navSwipe.setOnClickListener(v -> {
                Intent intent = new Intent(this, SwipeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Вытягиваем старый экран свайпов
                startActivity(intent);
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                Toast.makeText(this, "Ты уже в Профиле!", Toast.LENGTH_SHORT).show();
            });
        }

        if (navFriends != null) {
            navFriends.setOnClickListener(v -> {
                Toast.makeText(this, "Переход к Друзьям/Комнатам", Toast.LENGTH_SHORT).show();
            });
        }
    }
}