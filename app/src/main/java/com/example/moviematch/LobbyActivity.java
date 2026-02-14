package com.example.moviematch;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);

        ImageButton btnBack = findViewById(R.id.btn_back);
        TextView tvRoomCode = findViewById(R.id.tv_room_code);
        View btnCopy = findViewById(R.id.btn_copy_code);

        String roomCode = getIntent().getStringExtra("ROOM_CODE");
        if (roomCode == null || roomCode.isEmpty()) {
            roomCode = generateRandomCode();
        }

        if (tvRoomCode != null) {
            tvRoomCode.setText(roomCode);
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnCopy != null) {
            String finalRoomCode = roomCode;
            btnCopy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Room Code", finalRoomCode);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Код скопирован!", Toast.LENGTH_SHORT).show();
            });
        }

        // Кнопка НАЧАТЬ ПОИСК (теперь она точно внутри onCreate!)
        View btnStartSearch = findViewById(R.id.btn_start_search);
        if (btnStartSearch != null) {
            btnStartSearch.setOnClickListener(v -> {
                Toast.makeText(this, "Поиск запущен! Начинаем игру...", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, SwipeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                finish();
            });
        }

        // Вызываем метод для настройки нашей аватарки
        setupCurrentUserSlot();
    } // <-- Вот здесь заканчивается onCreate

    // Метод для настройки аватарки (находится за пределами onCreate, но внутри класса)
    private void setupCurrentUserSlot() {
        View userSlot = findViewById(R.id.u1);

        if (userSlot != null) {
            View avatarBg = userSlot.findViewById(R.id.avatar_bg);
            TextView userName = userSlot.findViewById(R.id.user_name);
            TextView avatarInitials = userSlot.findViewById(R.id.avatar_initials);
            TextView adminBadge = userSlot.findViewById(R.id.admin_badge);

            if (userName != null) {
                userName.setText("Вы");
                userName.setTextColor(Color.WHITE);
                userName.setTypeface(null, Typeface.BOLD);
            }

            if (avatarBg != null) {
                avatarBg.setBackgroundResource(R.drawable.bg_avatar_you);
            }

            if (avatarInitials != null) {
                avatarInitials.setVisibility(View.VISIBLE);
                avatarInitials.setText("ВЫ");
            }

            if (adminBadge != null) {
                adminBadge.setVisibility(View.VISIBLE);
            }
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000);
        return String.valueOf(code);
    }
}