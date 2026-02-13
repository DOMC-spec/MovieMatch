package com.example.moviematch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private EditText inputNickname;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MovieMatchPrefs", Context.MODE_PRIVATE);

        // Проверяем, авторизован ли уже пользователь
        String savedUserId = sharedPreferences.getString("user_id", null);
        if (savedUserId != null) {
            // ИДЕМ В ПРОФИЛЬ, а не в свайпы!
            startActivity(new Intent(AuthActivity.this, ProfileActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_auth);

        inputNickname = findViewById(R.id.input_nickname);
        Button btnCreateProfile = findViewById(R.id.btn_create_profile);
        TextView tabRegister = findViewById(R.id.tab_register);
        TextView tabLogin = findViewById(R.id.tab_login);

        // --- ЛОГИКА ПЕРЕКЛЮЧЕНИЯ ВКЛАДОК ---
        tabLogin.setOnClickListener(v -> {
            tabLogin.setBackgroundResource(R.drawable.bg_auth_tab_active);
            tabLogin.setTextColor(Color.WHITE);

            tabRegister.setBackgroundResource(android.R.color.transparent);
            tabRegister.setTextColor(Color.parseColor("#64748B")); // цвет text_slate

            btnCreateProfile.setText("Войти");
        });

        tabRegister.setOnClickListener(v -> {
            tabRegister.setBackgroundResource(R.drawable.bg_auth_tab_active);
            tabRegister.setTextColor(Color.WHITE);

            tabLogin.setBackgroundResource(android.R.color.transparent);
            tabLogin.setTextColor(Color.parseColor("#64748B"));

            btnCreateProfile.setText("Создать профиль");
        });
        // ------------------------------------

        btnCreateProfile.setOnClickListener(v -> {
            String nickname = inputNickname.getText().toString().trim();
            if (nickname.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите Nickname", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nickname.length() < 3) {
                Toast.makeText(this, "Nickname должен быть не менее 3 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            loginOrRegisterUser(nickname);
        });
    }

    private void loginOrRegisterUser(String nickname) {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        String queryParam = "eq." + nickname;

        api.getUserByNickname(queryParam).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    if (!users.isEmpty()) {
                        User existingUser = users.get(0);
                        saveSessionAndProceed(existingUser.getId(), existingUser.getNickname());
                        Toast.makeText(AuthActivity.this, "С возвращением, " + nickname + "!", Toast.LENGTH_SHORT).show();
                    } else {
                        createNewUser(nickname, api);
                    }
                } else {
                    Toast.makeText(AuthActivity.this, "Ошибка проверки профиля", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewUser(String nickname, SupabaseApi api) {
        User newUser = new User(nickname);
        api.createUser(newUser).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    User createdUser = response.body().get(0);
                    saveSessionAndProceed(createdUser.getId(), createdUser.getNickname());
                    Toast.makeText(AuthActivity.this, "Профиль успешно создан!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AuthActivity.this, "Не удалось создать профиль", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Ошибка сети при создании", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSessionAndProceed(String userId, String nickname) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", userId);
        editor.putString("nickname", nickname);
        editor.apply();

        // ИДЕМ В ПРОФИЛЬ!
        startActivity(new Intent(AuthActivity.this, ProfileActivity.class));
        finish();
    }
}