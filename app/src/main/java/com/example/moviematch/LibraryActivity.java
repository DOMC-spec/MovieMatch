package com.example.moviematch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LibraryActivity extends AppCompatActivity {

    private TextView textQueue, textWatched, textFolders;
    private TextView badgeQueue, badgeWatched, badgeFolders;
    private View indicatorQueue, indicatorWatched, indicatorFolders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Находим кнопку Назад
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Находим элементы вкладок
        textQueue = findViewById(R.id.text_queue);
        textWatched = findViewById(R.id.text_watched);
        textFolders = findViewById(R.id.text_folders);

        badgeQueue = findViewById(R.id.badge_queue);
        badgeWatched = findViewById(R.id.badge_watched);
        badgeFolders = findViewById(R.id.badge_folders);

        indicatorQueue = findViewById(R.id.indicator_queue);
        indicatorWatched = findViewById(R.id.indicator_watched);
        indicatorFolders = findViewById(R.id.indicator_folders);

        LinearLayout tabQueue = findViewById(R.id.tab_queue);
        LinearLayout tabWatched = findViewById(R.id.tab_watched);
        LinearLayout tabFolders = findViewById(R.id.tab_folders);

        // Настраиваем клики по самим вкладкам
        if (tabQueue != null) tabQueue.setOnClickListener(v -> selectTab(0));
        if (tabWatched != null) tabWatched.setOnClickListener(v -> selectTab(1));
        if (tabFolders != null) tabFolders.setOnClickListener(v -> selectTab(2));

        setupBottomNavigation();

        // Проверяем, с какой вкладкой нас просили открыться (по умолчанию открываем Просмотрено - индекс 1)
        int initialTab = getIntent().getIntExtra("TAB_INDEX", 1);
        selectTab(initialTab);
    }

    // Метод перекраски вкладок
    private void selectTab(int index) {
        // Сначала делаем ВСЕ вкладки неактивными (серыми)
        int colorInactive = Color.parseColor("#94A3B8");
        int colorActive = Color.WHITE;
        int colorBadgeTextActive = Color.parseColor("#6366F1");

        textQueue.setTextColor(colorInactive);
        badgeQueue.setBackgroundResource(R.drawable.bg_badge_inactive);
        badgeQueue.setTextColor(Color.parseColor("#CBD5E1"));
        indicatorQueue.setVisibility(View.INVISIBLE);

        textWatched.setTextColor(colorInactive);
        badgeWatched.setBackgroundResource(R.drawable.bg_badge_inactive);
        badgeWatched.setTextColor(Color.parseColor("#CBD5E1"));
        indicatorWatched.setVisibility(View.INVISIBLE);

        textFolders.setTextColor(colorInactive);
        badgeFolders.setBackgroundResource(R.drawable.bg_badge_inactive);
        badgeFolders.setTextColor(Color.parseColor("#CBD5E1"));
        indicatorFolders.setVisibility(View.INVISIBLE);

        // Теперь делаем активной только ВЫБРАННУЮ
        if (index == 0) {
            textQueue.setTextColor(colorActive);
            badgeQueue.setBackgroundResource(R.drawable.bg_badge_active);
            badgeQueue.setTextColor(colorBadgeTextActive);
            indicatorQueue.setVisibility(View.VISIBLE);
        } else if (index == 1) {
            textWatched.setTextColor(colorActive);
            badgeWatched.setBackgroundResource(R.drawable.bg_badge_active);
            badgeWatched.setTextColor(colorBadgeTextActive);
            indicatorWatched.setVisibility(View.VISIBLE);
        } else if (index == 2) {
            textFolders.setTextColor(colorActive);
            badgeFolders.setBackgroundResource(R.drawable.bg_badge_active);
            badgeFolders.setTextColor(colorBadgeTextActive);
            indicatorFolders.setVisibility(View.VISIBLE);
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
    }
}