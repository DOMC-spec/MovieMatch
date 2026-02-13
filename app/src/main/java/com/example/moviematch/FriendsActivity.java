package com.example.moviematch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Подключаем твой макет друзей
        setContentView(R.layout.friends);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        // Ищем элементы нижней панели (предполагается, что в friends.xml у тебя та же панель, что и в profile.xml)
        View navSwipe = findViewById(R.id.nav_swipe_action);
        View navProfile = findViewById(R.id.nav_profile);
        View navFriends = findViewById(R.id.nav_friends);

        if (navSwipe != null) {
            navSwipe.setOnClickListener(v -> {
                Intent intent = new Intent(this, SwipeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Возвращаемся к свайпам
                startActivity(intent);
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Переходим в профиль
                startActivity(intent);
            });
        }

        if (navFriends != null) {
            navFriends.setOnClickListener(v -> {
                Toast.makeText(this, "Ты уже во вкладке Друзья!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}