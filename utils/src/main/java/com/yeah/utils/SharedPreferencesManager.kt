package com.yeah.utils

import android.content.Context
import android.content.SharedPreferences

interface ISharedPreferencesManager {
    fun saveAuthToken(token: String)
    fun getAuthToken(): String?
    fun clearAuthToken()
}

// Утилитный объект для работы с SharedPreferences
class SharedPreferencesManager(context: Context) :ISharedPreferencesManager {

    // Имя файла настроек SharedPreferences
    private val prefs: SharedPreferences = context.getSharedPreferences("DSClientPrefs", Context.MODE_PRIVATE)

    // Сохранение токена
    override fun saveAuthToken(token: String) {
        with(prefs.edit()) {
            putString("auth_token", token)
            apply()
        }
    }

    // Получение токена
    override fun getAuthToken(): String? {
        return prefs.getString("auth_token", null)
    }

    // Очистка токена
    override fun clearAuthToken() {
        with(prefs.edit()) {
            remove("auth_token")
            apply()
        }
    }
}
