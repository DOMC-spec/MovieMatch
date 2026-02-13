package com.example.moviematch;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Для начала мы просто перенаправим запуск сразу на экран свайпов,
        // чтобы протестировать карточки, которые мы только что создали.
        Intent intent = new Intent(this, SwipeActivity.class);
        startActivity(intent);

        // Закрываем MainActivity, чтобы при нажатии "Назад" приложение закрывалось
        finish();
    }
}