package com.example.moviematch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryActivity extends AppCompatActivity {

    private TextView textQueue, textWatched, textFolders;
    private TextView badgeQueue, badgeWatched, badgeFolders;
    private View indicatorQueue, indicatorWatched, indicatorFolders;

    private RecyclerView recyclerView;
    private LibraryAdapter adapter;
    private List<LibraryItemResponse> allItems = new ArrayList<>();
    private List<LibraryItemResponse> currentDisplayedItems = new ArrayList<>();

    private int currentTabIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        initTabs();
        setupBottomNavigation();

        // Настраиваем RecyclerView
        recyclerView = findViewById(R.id.recycler_library);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LibraryAdapter(currentDisplayedItems);
        recyclerView.setAdapter(adapter);

        currentTabIndex = getIntent().getIntExtra("TAB_INDEX", 1);
        selectTab(currentTabIndex);

        // Скачиваем данные!
        fetchUserLibrary();
    }

    private void fetchUserLibrary() {
        SharedPreferences prefs = getSharedPreferences("MovieMatchPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("user_id", "");

        if (userId.isEmpty()) return;

        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        api.getUserLibrary("eq." + userId).enqueue(new Callback<List<LibraryItemResponse>>() {
            @Override
            public void onResponse(Call<List<LibraryItemResponse>> call, Response<List<LibraryItemResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allItems = response.body();
                    updateBadges();
                    selectTab(currentTabIndex); // Перерисовываем список для активной вкладки
                } else {
                    Toast.makeText(LibraryActivity.this, "Ошибка загрузки библиотеки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LibraryItemResponse>> call, Throwable t) {
                Log.e("Library", "Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void updateBadges() {
        int plannedCount = 0;
        int watchedCount = 0;

        for (LibraryItemResponse item : allItems) {
            if ("PLANNED".equals(item.getStatus())) plannedCount++;
            else if ("WATCHED".equals(item.getStatus())) watchedCount++;
        }

        badgeQueue.setText(String.valueOf(plannedCount));
        badgeWatched.setText(String.valueOf(watchedCount));
    }

    private void selectTab(int index) {
        currentTabIndex = index;

        // 1. Меняем визуал вкладок
        int colorInactive = Color.parseColor("#94A3B8");
        int colorActive = Color.WHITE;
        int colorBadgeTextActive = Color.parseColor("#6366F1");

        textQueue.setTextColor(colorInactive); badgeQueue.setBackgroundResource(R.drawable.bg_badge_inactive); badgeQueue.setTextColor(Color.parseColor("#CBD5E1")); indicatorQueue.setVisibility(View.INVISIBLE);
        textWatched.setTextColor(colorInactive); badgeWatched.setBackgroundResource(R.drawable.bg_badge_inactive); badgeWatched.setTextColor(Color.parseColor("#CBD5E1")); indicatorWatched.setVisibility(View.INVISIBLE);
        textFolders.setTextColor(colorInactive); badgeFolders.setBackgroundResource(R.drawable.bg_badge_inactive); badgeFolders.setTextColor(Color.parseColor("#CBD5E1")); indicatorFolders.setVisibility(View.INVISIBLE);

        if (index == 0) {
            textQueue.setTextColor(colorActive); badgeQueue.setBackgroundResource(R.drawable.bg_badge_active); badgeQueue.setTextColor(colorBadgeTextActive); indicatorQueue.setVisibility(View.VISIBLE);
        } else if (index == 1) {
            textWatched.setTextColor(colorActive); badgeWatched.setBackgroundResource(R.drawable.bg_badge_active); badgeWatched.setTextColor(colorBadgeTextActive); indicatorWatched.setVisibility(View.VISIBLE);
        } else if (index == 2) {
            textFolders.setTextColor(colorActive); badgeFolders.setBackgroundResource(R.drawable.bg_badge_active); badgeFolders.setTextColor(colorBadgeTextActive); indicatorFolders.setVisibility(View.VISIBLE);
        }

        // 2. Фильтруем данные для списка
        currentDisplayedItems.clear();
        for (LibraryItemResponse item : allItems) {
            if (index == 0 && "PLANNED".equals(item.getStatus())) {
                currentDisplayedItems.add(item);
            } else if (index == 1 && "WATCHED".equals(item.getStatus())) {
                currentDisplayedItems.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initTabs() {
        textQueue = findViewById(R.id.text_queue); textWatched = findViewById(R.id.text_watched); textFolders = findViewById(R.id.text_folders);
        badgeQueue = findViewById(R.id.badge_queue); badgeWatched = findViewById(R.id.badge_watched); badgeFolders = findViewById(R.id.badge_folders);
        indicatorQueue = findViewById(R.id.indicator_queue); indicatorWatched = findViewById(R.id.indicator_watched); indicatorFolders = findViewById(R.id.indicator_folders);

        LinearLayout tabQueue = findViewById(R.id.tab_queue); LinearLayout tabWatched = findViewById(R.id.tab_watched); LinearLayout tabFolders = findViewById(R.id.tab_folders);
        if (tabQueue != null) tabQueue.setOnClickListener(v -> selectTab(0));
        if (tabWatched != null) tabWatched.setOnClickListener(v -> selectTab(1));
        if (tabFolders != null) tabFolders.setOnClickListener(v -> selectTab(2));
    }

    private void setupBottomNavigation() {
        View navSwipe = findViewById(R.id.nav_swipe_action); View navProfile = findViewById(R.id.nav_profile); View navFriends = findViewById(R.id.nav_friends);
        if (navSwipe != null) navSwipe.setOnClickListener(v -> { Intent intent = new Intent(this, SwipeActivity.class); intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); startActivity(intent); });
        if (navProfile != null) navProfile.setOnClickListener(v -> { Intent intent = new Intent(this, ProfileActivity.class); intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); startActivity(intent); });
        if (navFriends != null) navFriends.setOnClickListener(v -> { Intent intent = new Intent(this, FriendsActivity.class); intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); startActivity(intent); });
    }
}