package com.example.moviematch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MatchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches);

        // Кнопка НАЗАД (возвращает к свайпам)
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Кнопка ОСТАНОВИТЬ ПОИСК (выходит из комнаты)
        View btnFinishSearch = findViewById(R.id.btn_finish_search);
        if (btnFinishSearch != null) {
            btnFinishSearch.setOnClickListener(v -> {
                Toast.makeText(this, "Поиск завершен. Выходим из комнаты.", Toast.LENGTH_SHORT).show();

                // Перекидываем пользователя обратно на вкладку "Друзья" (в лобби)
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
    }
}