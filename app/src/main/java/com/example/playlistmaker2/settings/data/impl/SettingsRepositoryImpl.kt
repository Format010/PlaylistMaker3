package com.example.playlistmaker2.settings.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker2.SECRET_KEY
import com.example.playlistmaker2.settings.data.SettingsRepository

class SettingsRepositoryImpl(private val sharedPrefs : SharedPreferences) : SettingsRepository {

    override fun getThemeSettings(): Boolean {
    return sharedPrefs.getBoolean(SECRET_KEY, false)
    }

    override fun updateThemeSetting(settings: Boolean) {
        sharedPrefs.edit {
            putBoolean(SECRET_KEY, settings)
        }

    }
}