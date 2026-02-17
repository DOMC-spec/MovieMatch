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

        SharedPreferences prefs = getSharedPreferences("MovieMatchPrefs", Context.MODE_PRIVATE);
        String nickname = prefs.getString("nickname", "Гость");

        TextView tvProfileName = findViewById(R.id.tv_profile_name);
        TextView tvProfileTag = findViewById(R.id.tv_profile_tag);

        if (tvProfileName != null && tvProfileTag != null) {
            tvProfileName.setText(nickname.toUpperCase());
            tvProfileTag.setText("@" + nickname.toLowerCase());
        }

        setupBottomNavigation();
        setupStats(); // Вызываем настройку статистики
    }

    private void setupStats() {
        // Находим 4 кружочка по ID
        View statFollowers = findViewById(R.id.stat_followers);
        View statFollowing = findViewById(R.id.stat_following);
        View statWatched = findViewById(R.id.stat_watched);
        View statPlanned = findViewById(R.id.stat_planned);

        // Настраиваем "Подписчики"
        if (statFollowers != null) {
            ((TextView) statFollowers.findViewById(R.id.stat_number)).setText("42");
            ((TextView) statFollowers.findViewById(R.id.stat_label)).setText("Подписчики");
        }

        // Настраиваем "Подписки"
        if (statFollowing != null) {
            ((TextView) statFollowing.findViewById(R.id.stat_number)).setText("15");
            ((TextView) statFollowing.findViewById(R.id.stat_label)).setText("Подписки");
        }

        // Настраиваем "Просмотрено"
        if (statWatched != null) {
            ((TextView) statWatched.findViewById(R.id.stat_number)).setText("128");
            ((TextView) statWatched.findViewById(R.id.stat_label)).setText("Просмотрено");

            // Клик открывает Библиотеку (вкладка 1)
            statWatched.setOnClickListener(v -> {
                Intent intent = new Intent(this, LibraryActivity.class);
                intent.putExtra("TAB_INDEX", 1);
                startActivity(intent);
            });
        }

        // Настраиваем "В планах"
        if (statPlanned != null) {
            ((TextView) statPlanned.findViewById(R.id.stat_number)).setText("56");
            ((TextView) statPlanned.findViewById(R.id.stat_label)).setText("В планах");

            // Клик открывает Библиотеку (вкладка 0)
            statPlanned.setOnClickListener(v -> {
                Intent intent = new Intent(this, LibraryActivity.class);
                intent.putExtra("TAB_INDEX", 0);
                startActivity(intent);
            });
        }
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
                // Мы уже в профиле
            });
        }
        if (navFriends != null) {
            navFriends.setOnClickListener(v -> {
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }
    }
}