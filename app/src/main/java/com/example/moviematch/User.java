package com.example.moviematch;

import com.google.gson.annotations.SerializedName;

public class User {
    // ID генерируется базой, поэтому при создании профиля мы его не передаем
    @SerializedName("user_id")
    private String id;

    @SerializedName("nickname")
    private String nickname;

    // Конструктор для отправки нового профиля
    public User(String nickname) {
        this.nickname = nickname;
    }

    public String getId() { return id; }
    public String getNickname() { return nickname; }
}