package com.example.moviematch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        setupBottomNavigation();
        setupRoomButtons();
    }

    private void setupRoomButtons() {
        View btnCreateRoom = findViewById(R.id.btn_create_room);
        View btnJoinRoom = findViewById(R.id.btn_join_room);
        EditText inputRoomCode = findViewById(R.id.input_room_code);

        // Логика СОЗДАНИЯ новой комнаты
        if (btnCreateRoom != null) {
            btnCreateRoom.setOnClickListener(v -> {
                Intent intent = new Intent(FriendsActivity.this, LobbyActivity.class);
                startActivity(intent);
            });
        }

        // Логика ВХОДА в существующую комнату
        if (btnJoinRoom != null && inputRoomCode != null) {
            btnJoinRoom.setOnClickListener(v -> {
                String code = inputRoomCode.getText().toString().trim();
                if (code.length() < 5) {
                    Toast.makeText(this, "Введите корректный код (5 цифр)", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Передаем введенный код в лобби
                Intent intent = new Intent(FriendsActivity.this, LobbyActivity.class);
                intent.putExtra("ROOM_CODE", code);
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
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }

        if (navFriends != null) {
            navFriends.setOnClickListener(v -> {
                // Ничего не делаем, мы уже тут
            });
        }
    }
}