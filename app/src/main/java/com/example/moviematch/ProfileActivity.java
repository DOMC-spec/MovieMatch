package com.example.moviematch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // 1. Достаем никнейм из памяти телефона
        SharedPreferences prefs = getSharedPreferences("MovieMatchPrefs", Context.MODE_PRIVATE);
        String nickname = prefs.getString("nickname", "Гость");

        // 2. Находим текстовые поля на экране
        TextView tvProfileName = findViewById(R.id.tv_profile_name);
        TextView tvProfileTag = findViewById(R.id.tv_profile_tag);

        // 3. Подставляем реальный ник
        if (tvProfileName != null && tvProfileTag != null) {
            tvProfileName.setText(nickname.toUpperCase()); // Делаем большими буквами, как в дизайне
            tvProfileTag.setText("@" + nickname.toLowerCase()); // Делаем маленькими с собачкой
        }

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        View navSwipe = findViewById(R.id.nav_swipe_action);
        View navProfile = findViewById(R.id.nav_profile);
        View navFriends = findViewById(R.id.nav_friends);

        if (navSwipe != null) {
            navSwipe.setOnClickListener(v -> {
                Intent intent = new Intent(this, SwipeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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