package com.example.playlistmaker2.settings.data

interface SettingsRepository {

    fun getThemeSettings(): Boolean
    fun updateThemeSetting(settings: Boolean)

}