package com.example.playlistmaker2.settings.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker2.HISTORY_KEY
import com.example.playlistmaker2.settings.data.SettingsRepository



class SettingsRepositoryImpl(private val sharedPrefs : SharedPreferences) : SettingsRepository {

    override fun getThemeSettings(): Boolean {
    return sharedPrefs.getBoolean(HISTORY_KEY, false)
    }

    override fun updateThemeSetting(settings: Boolean) {
        sharedPrefs.edit {
            putBoolean(HISTORY_KEY, settings)
        }

    }
}